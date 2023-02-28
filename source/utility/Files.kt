package utility

import java.io.File

fun File.pathWithChangedExtension(newExtension: String): String {
    val absoultePath = this.absolutePath

    val finalDot = absoultePath.lastIndexOf('.')
    if (finalDot < 0) {
        return "$absoultePath.$newExtension"
    }

    val currentExtension = absolutePath.substring(finalDot)
    if (currentExtension == ".$newExtension") {
        return absoultePath
    }

    return "${absoultePath.substring(0, finalDot)}.$newExtension"
}
