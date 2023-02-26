package window

import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import java.io.File
import javax.imageio.ImageIO

class FrameBuffer(width: Int, height: Int) : BufferedImage(width, height, TYPE_INT_ARGB) {
    private val data = (raster.dataBuffer as DataBufferInt).data

    init {
        clear(Color.BLACK)
    }

    fun clear(color: Color) {
        graphics.let {
            it.color = color
            it.fillRect(0, 0, width, height)
            it.dispose()
        }
    }

    fun isValidPosition(x: Int, y: Int): Boolean {
        return (x >= 0 && y >= 0 && x < width && y < height)
    }

    fun setPixel(x: Int, y: Int, color: Color) {
        if (isValidPosition(x, y)) {
            data[x + y * width] = color.rgb
        }
    }

    fun saveToFile(filepath: String) {
        ImageIO.write(this, "png", File(filepath))
    }
}
