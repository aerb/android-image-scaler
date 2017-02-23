package ca.adamerb.scaler

import java.io.File

val densities = mapOf(
    "ldpi" to 120.0,
    "mdpi" to 160.0,
    "hdpi" to  240.0,
    "@2x"  to 326.0,
    "xhdpi" to 320.0,
    "xxhdpi" to 480.0,
    "@3x" to 489.0,
    "xxxhdpi" to 640.0
)

fun main(args: Array<String>) {
    val res = args.getOrNull(0) ?: throw IllegalArgumentException("Requires path to android /res directory.")

    val xxxhdpi = File(res, "drawable-xxxhdpi")
    val xxhdpi = File(res, "drawable-xxhdpi")
    val xhdpi = File(res, "drawable-xhdpi")

    val highResImages = xxxhdpi.listFiles().filter { f -> f.isFile && (f.path.endsWith(".png") || f.path.endsWith(".gif")) }
    fun scaleTo(density: String, intoDir: File) {
        val scaleBy = densities[density]!! / densities["xxxhdpi"]!! * 100
        for(sourceFile in highResImages) {
            val targetFile = File(intoDir, sourceFile.name)

            val command = listOf("convert", sourceFile.canonicalPath, "-resize", scaleBy.toString() + "%", targetFile.canonicalPath)

            command.forEach { print(it + " ") }
            println()

            ProcessBuilder(command).start().waitFor()
        }
    }

    scaleTo(density = "xxhdpi", intoDir = xxhdpi)
    scaleTo(density = "xhdpi", intoDir = xhdpi)
}

