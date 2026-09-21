#!/usr/bin/env bash
#
# Builds, validates and stages a content bundle for the CDN.
#
# Emits two files into dist/:
#   content_v<hash>.json  immutable, content-addressed, cached at the edge forever
#   latest.json           the mutable pointer, purged on every publish
#
# It does not upload. Upload ordering is the one part of this pipeline that can hurt a family,
# so it lives in the workflow where the ordering is explicit and reviewable.
set -euo pipefail

VERSION="${CONTENT_VERSION:-${GITHUB_RUN_NUMBER:-}}"
if [[ -z "$VERSION" ]]; then
  echo "CONTENT_VERSION or GITHUB_RUN_NUMBER must be set." >&2
  echo "It must be monotonic: the client refuses any bundle not newer than the one it holds." >&2
  exit 1
fi

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
DIST="$ROOT/dist"
rm -rf "$DIST"; mkdir -p "$DIST"

echo "==> Generating and validating the bundle"
# The exporter runs inside the test suite, which is also what asserts the bundle satisfies the
# client contract. A failure here must stop the publish: an invalid bundle that reaches the CDN
# is downloaded and rejected by every device, which costs families on metered data and shows up
# only in telemetry.
( cd "$ROOT" && ./gradlew :app:testDebugUnitTest \
    --tests '*ContentBundleTest*' \
    --tests '*ContentValidatorTest*' \
    --console=plain )

SRC="$ROOT/app/build/ableys-content-v1.json"
[[ -f "$SRC" ]] || { echo "Exporter produced no bundle at $SRC" >&2; exit 1; }

echo "==> Canonicalising"
# The bundle is content-addressed, so its bytes must depend only on its content. A timestamp or
# a run number inside the file would give identical content a new hash on every publish: a new
# immutable object at the edge, cached forever, for no change at all. The version lives in the
# pointer, which is the half that is meant to be mutable.
python3 -c "
import json, sys
src = sys.argv[1]
bundle = json.load(open(src))
bundle['version'] = 0
bundle['generatedAt'] = ''
json.dump(bundle, open(src, 'w'), ensure_ascii=False, sort_keys=True, separators=(',', ':'))
" "$SRC"

HASH="$(sha256sum "$SRC" | cut -c1-12)"
BUNDLE="content_v${HASH}.json"
cp "$SRC" "$DIST/$BUNDLE"

# The pointer. Its shape is asserted against the client parser in PublishPointerTest, so the two
# cannot drift apart without a red build.
cat > "$DIST/latest.json" <<JSON
{"version": ${VERSION}, "bundle": "${BUNDLE}"}
JSON

echo "==> Staged"
ls -lh "$DIST"
echo
echo "Content hash: $HASH"
echo "Identical content re-publishes under the same filename, so an unchanged CMS publish"
echo "costs one pointer write and nothing at the edge."
echo
echo "Upload order matters: bundle first, pointer second. See the workflow."
