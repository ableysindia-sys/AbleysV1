package com.example

import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

/**
 * Keeps the publish pipeline and the client parser from drifting apart.
 *
 * The pointer is written by a shell script in tools/ and parsed by Kotlin in the app. Nothing
 * makes anyone edit those two together, and the failure is quiet in the worst way: the pipeline
 * goes green, the CDN serves a valid-looking file, and every device silently stops seeing
 * content updates because a field was renamed.
 *
 * So the shape is asserted from both ends here, and the script is read as a file rather than
 * restated, so renaming a key in one place fails the build.
 */
@RunWith(RobolectricTestRunner::class)
class PublishPointerTest {

    private val script = File("../tools/publish-content.sh")
    private val workflow = File("../.github/workflows/publish-content.yml")

    /** Exactly the parse ContentSync performs on latest.json. */
    private fun parseAsClient(json: String): Pair<Int, String> {
        val obj = JSONObject(json)
        return obj.optInt("version", -1) to obj.optString("bundle", "")
    }

    @Test
    fun theClientParsesThePointerThePipelineWrites() {
        val pointer = """{"version": 42, "bundle": "content_v0a1b2c3d4e5f.json"}"""
        val (version, bundle) = parseAsClient(pointer)
        assertEquals(42, version)
        assertEquals("content_v0a1b2c3d4e5f.json", bundle)
    }

    @Test
    fun aMalformedPointerIsRejectedRatherThanMisread() {
        // A truncated or empty file must not parse into something that looks usable.
        listOf("""{"version": 0, "bundle": ""}""", """{"bundle": "x.json"}""", """{}""")
            .forEach { bad ->
                val (version, bundle) = parseAsClient(bad)
                assertTrue(
                    "'$bad' should not yield a usable pointer",
                    version <= 0 || bundle.isBlank()
                )
            }
    }

    @Test
    fun thePublishScriptWritesTheKeysTheClientReads() {
        assertTrue("publish-content.sh is missing", script.exists())
        val text = script.readText()
        assertTrue("script no longer writes a 'version' key", text.contains("\"version\""))
        assertTrue("script no longer writes a 'bundle' key", text.contains("\"bundle\""))
        assertTrue(
            "script no longer content-addresses the bundle filename",
            text.contains("content_v\${HASH}.json")
        )
    }

    @Test
    fun thePublishedBundleCarriesNoVersionSoItsHashIsStable() {
        val text = script.readText()
        assertTrue(
            "The bundle must be canonicalised before hashing, or identical content produces a " +
                "new immutable object at the edge on every publish",
            text.contains("bundle['version'] = 0")
        )
        assertTrue(
            "Keys must be sorted, or map ordering alone changes the hash",
            text.contains("sort_keys=True")
        )
        assertTrue(
            "generatedAt must be cleared before hashing",
            text.contains("bundle['generatedAt'] = ''")
        )
    }

    @Test
    fun theScriptRefusesToPublishWithoutAMonotonicVersion() {
        val text = script.readText()
        assertTrue(
            "The version must come from a monotonic source, never a timestamp",
            text.contains("GITHUB_RUN_NUMBER")
        )
        assertTrue(
            "The script must fail rather than guess a version",
            text.contains("must be set")
        )
    }

    @Test
    fun theWorkflowUploadsTheBundleBeforeThePointer() {
        assertTrue("publish workflow is missing", workflow.exists())
        val text = workflow.readText()
        val bundleUpload = text.indexOf("Upload the bundle")
        val verify = text.indexOf("Verify the bundle is live")
        val pointerUpload = text.indexOf("Upload the pointer")
        assertTrue("bundle upload step missing", bundleUpload >= 0)
        assertTrue("verification step missing", verify >= 0)
        assertTrue("pointer upload step missing", pointerUpload >= 0)
        assertTrue(
            "The pointer must not be uploaded before the bundle it names is live",
            bundleUpload < verify && verify < pointerUpload
        )
    }

    @Test
    fun theBucketSecretToleratesAUriRatherThanABareName() {
        val text = workflow.readText()
        assertTrue(
            "The natural way to fill CONTENT_BUCKET in is to paste an s3:// URI; the workflow " +
                "must strip it rather than build s3://s3://",
            text.contains("NAME=\"\${RAW#s3://}\"")
        )
    }

    @Test
    fun theProvisioningRunbookExistsAndCoversTheJsonCachingTrap() {
        val runbook = File("../tools/CONTENT_PIPELINE.md")
        assertTrue("provisioning runbook is missing", runbook.exists())
        val text = runbook.readText()
        assertTrue(
            "The runbook must cover that Cloudflare does not cache JSON by default -- without " +
                "a cache rule the immutable bundle is fetched from origin every time",
            text.contains("does not cache JSON by default")
        )
        assertTrue("runbook should explain the rollback direction", text.contains("new version"))
        assertTrue("runbook should list every secret the workflow reads", 
            listOf("CONTENT_BUCKET", "CONTENT_PUBLISH_ROLE", "CF_ZONE_ID", "CF_API_TOKEN")
                .all { text.contains(it) })
    }

    @Test
    fun everySecretTheWorkflowReadsIsDocumented() {
        val used = Regex("secrets\\.([A-Z_]+)").findAll(workflow.readText())
            .map { it.groupValues[1] }.toSet()
        val runbook = File("../tools/CONTENT_PIPELINE.md").readText()
        val undocumented = used.filter { !runbook.contains(it) }
        assertTrue("Secrets used but not documented: $undocumented", undocumented.isEmpty())
    }

    @Test
    fun cachingHeadersMatchTheDesign() {
        val text = workflow.readText()
        assertTrue(
            "The hashed bundle must be immutable at the edge",
            text.contains("max-age=31536000, immutable")
        )
        assertTrue(
            "The pointer must revalidate, or the ETag check never reaches the origin",
            text.contains("max-age=0, must-revalidate")
        )
    }

    @Test
    fun onlyThePointerIsPurged() {
        val text = workflow.readText()
        assertTrue("purge step missing", text.contains("purge_cache"))
        assertTrue(
            "The purge must name latest.json specifically; purging everything throws away " +
                "immutable bundles the edge should keep",
            text.contains("app-content/latest.json\"]")
        )
    }

    @Test
    fun rollbackGoesForwardsInVersionAndBackwardsInContent() {
        val text = workflow.readText()
        assertTrue("rollback input missing", text.contains("rollback_bundle"))
        assertTrue(
            "Rollback must reuse the run number, not a lower version -- the client refuses " +
                "anything not newer than what it holds",
            text.contains("github.run_number") && text.contains("rollback")
        )
    }
}
