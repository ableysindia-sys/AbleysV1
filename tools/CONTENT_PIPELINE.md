# Content pipeline — provisioning runbook

What `publish-content.yml` needs to exist before it can run, and the three places the obvious
setup is wrong.

The design: a CMS publish builds a content-addressed bundle, uploads it, verifies it is live,
then moves a small pointer. The app polls the pointer with an ETag, downloads only when the
version is newer, validates before replacing anything, and falls back to what it already has if
the download is bad. Nothing a family is doing depends on the network.

---

## The three corrections

### 1. Cloudflare does not cache JSON by default

This is the one that quietly undoes the whole design. Cloudflare's default cached-extension list
covers images, media, CSS, JS, fonts and archives. **JSON is not on it**, and both files here are
`.json`.

Without a cache rule, `content_v<hash>.json` is fetched from origin by every device on every
first launch after a publish. The immutable caching this pipeline is built around simply does not
happen, and you pay origin egress for 284 KB per install.

Add a Cache Rule (Caching → Cache Rules):

| | |
|---|---|
| Name | `Immutable content bundles` |
| Expression | `(http.host eq "cdn.ableys.in" and starts_with(http.request.uri.path, "/app-content/content_v"))` |
| Cache eligibility | Eligible for cache |
| Edge TTL | Use cache-control header if present, otherwise 1 year |
| Browser TTL | Use cache-control header if present |

Do **not** add a rule for `latest.json`. Leaving it uncached is exactly right: the conditional
request has to reach the origin for the ETag check to mean anything.

### 2. `CONTENT_BUCKET` is the bare bucket name

The workflow builds `s3://<name>/app-content/` itself. Setting the secret to `s3://ableys-content-prod`
would produce `s3://s3://...`. The workflow now strips a leading `s3://` or `gs://` and a trailing
slash defensively, but set it as the bare name: `ableys-content-prod`.

### 3. The S3 website endpoint is HTTP-only

If you CNAME `cdn.ableys.in` at an S3 *website* endpoint, that origin speaks HTTP only, and making
it work means setting Cloudflare SSL to Flexible — which encrypts browser-to-Cloudflare and leaves
Cloudflare-to-origin in the clear. Do not do that for content going to children's devices.

Options, best first:

- **Cloudflare R2 with a custom domain.** S3-compatible, so `aws s3 cp` works unchanged with an
  endpoint override. Bind `cdn.ableys.in` to the bucket in the R2 dashboard: no CNAME, no origin
  certificate, no SSL mode to get wrong, and zero egress to Cloudflare. Given the CDN is already
  Cloudflare, this removes a whole category of problem.
- **S3 REST endpoint** (`<bucket>.s3.<region>.amazonaws.com`), not the website endpoint. Speaks
  HTTPS, so Cloudflare SSL can be Full (strict).
- **GCS** via `c.storage.googleapis.com`, which also speaks HTTPS.

Whichever you pick, the orange cloud must be **on** or none of the caching or header behaviour
applies.

---

## Secrets

| Secret | Value |
|---|---|
| `CONTENT_BUCKET` | Bare bucket name, e.g. `ableys-content-prod` |
| `CONTENT_PUBLISH_ROLE` | IAM Role ARN for OIDC. Scope to `s3:PutObject` and `s3:GetObject` on `arn:aws:s3:::<bucket>/app-content/*` and nothing else |
| `CF_ZONE_ID` | Cloudflare dashboard → the domain → Overview, right sidebar |
| `CF_API_TOKEN` | My Profile → API Tokens → Custom token, permission **Zone · Cache Purge · Purge**, restricted to this zone. Never the Global API Key |

OIDC rather than static keys is the right call. Trust policy must pin the repository and
ideally the ref, or any workflow in any repo that can assume the role can publish clinical
content:

```json
"Condition": {
  "StringEquals": {
    "token.actions.githubusercontent.com:aud": "sts.amazonaws.com",
    "token.actions.githubusercontent.com:sub": "repo:ableysindia-sys/AbleysV1:ref:refs/heads/main"
  }
}
```

For R2, use an R2 API token with Object Read & Write scoped to the one bucket, and set
`AWS_ENDPOINT_URL` to the R2 S3 endpoint.

---

## The one secret the blueprint misses

The `repository_dispatch: cms-publish` trigger needs something to fire it. A CMS webhook cannot
call GitHub anonymously. Create a fine-grained PAT or a GitHub App installation token with
**Contents: read and write** on this repository only, store it in the CMS, and have it POST:

```
POST https://api.github.com/repos/ableysindia-sys/AbleysV1/dispatches
Authorization: Bearer <token>
Accept: application/vnd.github+json

{"event_type": "cms-publish"}
```

Until that exists, publish with **Run workflow** in the Actions tab. That is a perfectly good
starting point — a human pressing a button before clinical content reaches families is not
obviously worse than a webhook.

---

## First run

There is no `latest.json` yet. The client handles that: the pointer fetch fails, the sync reports
unavailable, and the app carries on with the catalogue compiled into it. Nothing breaks and
nobody sees anything. Publish once and the loop starts.

## Rollback

The client refuses any version not newer than the one it holds, so **a rollback is a new version
number pointing at an old bundle**. Run the workflow with `rollback_bundle` set to the older
`content_v<hash>.json` filename. It writes only a pointer and uploads nothing, because the old
bundle is still at the edge and immutable.

## Verifying a publish

```bash
curl -sI https://cdn.ableys.in/app-content/latest.json | grep -i 'cf-cache-status\|cache-control\|etag'
# expect: cache-control: public, max-age=0, must-revalidate

curl -s https://cdn.ableys.in/app-content/latest.json
# expect: {"version": N, "bundle": "content_v<hash>.json"}

curl -sI "https://cdn.ableys.in/app-content/$(curl -s https://cdn.ableys.in/app-content/latest.json | python3 -c 'import sys,json;print(json.load(sys.stdin)["bundle"])')" \
  | grep -i 'cf-cache-status\|cache-control'
# expect: cache-control: public, max-age=31536000, immutable
# expect: cf-cache-status: HIT on the second request. MISS every time means the cache rule
#         in section 1 is missing.
```

That last check is the one worth running twice.
