package entity.material

import math.vector.Float2
import math.vector.Float3
import java.io.Serializable

class Material : Serializable {
    var color = Float3(1f)
    var colorMap: Texture? = null
    var colorRatio = 0.5f
        set(blend) { field = blend.coerceAtLeast(0f).coerceAtMost(1f) }

    var roughness = 0.5f
    var roughnessMap: Texture? = null

    var normalMap: Texture? = null

    fun getColor(uv: Float2): Float3 {
        colorMap?.let {
            return blend(color, it.sample(uv), colorRatio)
        }
        return color
    }

    fun getRoughness(uv: Float2): Float {
        roughnessMap?.let {
            return it.sample(uv).x
        }
        return roughness
    }

    private fun blend(a: Float3, b: Float3, ratio: Float): Float3 {
        val invRatio = (1f - ratio)
        return Float3(
            (a.x * invRatio) + (b.x * ratio),
            (a.y * invRatio) + (b.y * ratio),
            (a.z * invRatio) + (b.z * ratio),
        )
    }
}
