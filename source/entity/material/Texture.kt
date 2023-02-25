package entity.material

import math.vector.Float2
import math.vector.Float3
import math.vector.Int2
import java.awt.Color
import java.io.File
import java.io.Serializable
import javax.imageio.ImageIO

class Texture : Serializable {
    var size: Int2
        get() = Int2(field)
        private set

    val data: Array<Color>

    constructor(size: Int2, data: Array<Color>) {
        this.size = Int2(size)
        this.data = data
    }

    constructor(filepath: String) {
        val image = ImageIO.read(File(filepath))

        size = Int2(image.width, image.height)
        data = Array(image.width * image.height) {
            Color(image.getRGB(it % image.width, it / image.width))
        }
    }

    fun sample(uv: Float2): Float3 {
        val u = uv.x - uv.x.toInt()
        val v = uv.y - uv.y.toInt()
        val x = (u * (size.x - 1f)).toInt().coerceAtLeast(0).coerceAtMost(size.x - 1)
        val y = (v * (size.y - 1f)).toInt().coerceAtLeast(0).coerceAtMost(size.y - 1)
        return Float3(data[y * size.x + x])
    }
}
