package raytracer

import math.vector.Vector2
import math.vector.Vector3
import java.awt.Dimension
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import java.io.File
import javax.imageio.ImageIO

class FrameBuffer(dimension: Dimension) : BufferedImage(dimension.width, dimension.height, TYPE_INT_ARGB) {
    private val pixelData = (raster.dataBuffer as DataBufferInt).data
    private val lightData = Array(pixelData.size) { Vector3() }

    private var counter = 0
    private var invCounter = 0.0

    val size: Dimension
        get() = Dimension(width, height)

    fun addLight(x: Int, y: Int, light: Vector3) {
        val index = x + y * width
        lightData[index] += light
        pixelData[index] = (lightData[index] * invCounter).color.rgb
    }

    fun newFrame() {
        invCounter = 1.0 / ++counter
    }

    fun getNDC(x: Double, y: Double): Vector2 {
        val result = Vector2()
        val y = (height - 1.0 - y)
        result.x = (x / (width - 1)) * 2 - 1
        result.y = (y / (height - 1)) * 2 - 1
        return result
    }

    fun saveAsPNG(filepath: String) {
        ImageIO.write(this, "png", File(filepath))
    }
}
