package entity.material

import math.vector.Float2
import math.vector.Float3
import java.awt.Color
import java.io.File
import java.io.Serializable
import javax.imageio.ImageIO

class Texture : Serializable {
    val width: Int
    val height: Int
    val data: Array<Color>

    constructor(width: Int, height: Int, data: Array<Color>) {
        this.width = width
        this.height = height
        this.data = data
    }

    constructor(filepath: String) {
        ImageIO.read(File(filepath)).let { image ->
            width = image.width; height = image.height
            data = Array(image.width * image.height) {
                Color(image.getRGB(it % image.width, it / image.width))
            }
        }
    }

    fun isValidPosition(x: Int, y: Int): Boolean {
        return (x >= 0 && y >= 0 && x < width && y < height)
    }

    fun sample(x: Int, y: Int): Color {
        if (isValidPosition(x, y)) {
            return data[x + y * width]
        }
        return Color.BLACK
    }

    fun sample(uv: Float2): Float3 {
        val u = (uv.x - uv.x.toInt())
        val v = (uv.y - uv.y.toInt())
        val x = (u * (width - 1f)).toInt().coerceAtLeast(0).coerceAtMost(width - 1)
        val y = (v * (height - 1f)).toInt().coerceAtLeast(0).coerceAtMost(height - 1)
        return Float3(sample(x, height - 1 - y))
    }
}
