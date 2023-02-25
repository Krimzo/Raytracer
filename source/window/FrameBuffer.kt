package window

import math.vector.Int2
import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import java.io.File
import javax.imageio.ImageIO

class FrameBuffer(size: Int2) : BufferedImage(size.x, size.y, TYPE_INT_ARGB) {
    private val data = (raster.dataBuffer as DataBufferInt).data

    val size: Int2
        get() = Int2(width, height)

    fun setPixel(cords: Int2, color: Color) {
        if (cords.x >= 0 && cords.y >= 0 && cords.x < width && cords.y < height) {
            data[cords.y * width + cords.x] = color.rgb
        }
    }

    fun saveToFile(filepath: String) {
        ImageIO.write(this, "png", File(filepath))
    }
}
