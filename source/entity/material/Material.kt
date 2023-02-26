package entity.material

import math.vector.Vector2
import math.vector.Vector3
import java.io.Serializable

class Material : Serializable {
    var color = Vector3(1.0)
    var colorMap: Texture? = null
    var colorRatio = 0.5
        set(colorRatio) { field = colorRatio.coerceAtLeast(0.0).coerceAtMost(1.0) }

    var roughness = 0.5
        set(roughness) { field = roughness.coerceAtLeast(0.0).coerceAtMost(1.0) }
    var roughnessMap: Texture? = null

    var metallic = 0.5
        set(metallic) { field = metallic.coerceAtLeast(0.0).coerceAtMost(1.0) }
    var metallicMap: Texture? = null

    var normalMap: Texture? = null

    fun getColor(uv: Vector2): Vector3 {
        colorMap?.let {
            return blend(color, it.sample(uv), colorRatio)
        }
        return color
    }

    fun getRoughness(uv: Vector2): Double {
        roughnessMap?.let {
            return it.sample(uv).x
        }
        return roughness
    }

    fun getMetallic(uv: Vector2): Double {
        metallicMap?.let {
            return it.sample(uv).x
        }
        return metallic
    }

    private fun blend(a: Vector3, b: Vector3, ratio: Double): Vector3 {
        val invRatio = (1 - ratio)
        return Vector3(
            (a.x * invRatio) + (b.x * ratio),
            (a.y * invRatio) + (b.y * ratio),
            (a.z * invRatio) + (b.z * ratio),
        )
    }
}
