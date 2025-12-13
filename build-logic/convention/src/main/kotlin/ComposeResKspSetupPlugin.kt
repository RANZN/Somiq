import org.gradle.api.Plugin
import org.gradle.api.Project

class ComposeResKspSetupPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        gradle.projectsEvaluated {
            configureAndroidKsp()
            configureIosKsp()
            configureJvmKsp()
        }
    }

    private fun Project.configureAndroidKsp() {
        val androidExt = extensions.findByName("android") ?: return
        val buildTypes = androidExt.javaClass.methods
            .firstOrNull { it.name == "getBuildTypes" }
            ?.invoke(androidExt) as? Iterable<*> ?: return

        buildTypes.forEach { buildType ->
            val buildTypeName = getBuildTypeName(buildType) ?: return@forEach
            val cap = buildTypeName.replaceFirstChar(Char::titlecase)
            
            configureKspTask("ksp${cap}KotlinAndroid", listOf(
                "generateResourceAccessorsForAndroid$cap",
                "generateResourceAccessorsForAndroidMain",
                "generateActualResourceCollectorsForAndroidMain",
                "generateComposeResClass",
                "generateResourceAccessorsForCommonMain",
                "generateExpectResourceCollectorsForCommonMain"
            ))
            
            configureKspTask("ksp${cap}UnitTestKotlinAndroid", listOf(
                "generateResourceAccessorsForAndroidUnitTest$cap",
                "generateResourceAccessorsForAndroidUnitTest",
                "generateResourceAccessorsForCommonTest",
                "generateResourceAccessorsForCommonMain",
                "generateComposeResClass",
                "generateExpectResourceCollectorsForCommonMain"
            ))
        }
    }

    private fun Project.configureIosKsp() {
        listOf("IosSimulatorArm64", "IosArm64").forEach { iosTarget ->
            configureKspTask("kspKotlin$iosTarget", listOf(
                "generateResourceAccessorsFor${iosTarget}Main",
                "generateActualResourceCollectorsFor${iosTarget}Main",
                "generateComposeResClass",
                "generateResourceAccessorsForCommonMain",
                "generateExpectResourceCollectorsForCommonMain",
                "generateResourceAccessorsForIosMain"
            ))
        }
    }

    private fun Project.configureJvmKsp() {
        listOf("kspKotlinJvm", "kspKotlinJvmMain", "kspKotlinJvmDesktop", "kspKotlinDesktop").forEach { kspName ->
            configureKspTask(kspName, listOf(
                "generateResourceAccessorsForJvmMain",
                "generateActualResourceCollectorsForJvmMain",
                "generateComposeResClass",
                "generateResourceAccessorsForCommonMain",
                "generateExpectResourceCollectorsForCommonMain"
            ))
        }
    }

    private fun Project.configureKspTask(taskName: String, depNames: List<String>) {
        try {
            tasks.named(taskName) {
                depNames.forEach { depName ->
                    try {
                        dependsOn(tasks.named(depName))
                    } catch (e: Exception) {
                        // Try findByName as fallback
                        tasks.findByName(depName)?.let { dependsOn(it) }
                    }
                }
            }
        } catch (e: Exception) {
            // Task doesn't exist, skip
        }
    }

    private fun getBuildTypeName(buildType: Any?): String? {
        val nameProp = buildType?.javaClass?.methods?.firstOrNull { it.name == "getName" }
        return nameProp?.let { prop ->
            val result: Any? = prop.invoke(buildType)
            result as? String
        }
    }
}
