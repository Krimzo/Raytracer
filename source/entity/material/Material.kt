package entity.material

import logging.Logger
import math.vector.Vector2
import math.vector.Vector3
import java.io.Serializable

class Material : Serializable {
    var color = Vector3(1.0)
    var colorMap: Texture? = null

    var roughness = 1.0
    var roughnessMap: Texture? = null

    var metallic = 0.0
    var metallicMap: Texture? = null

    init {
        Logger.log("Created material")
    }

    fun computeColor(uv: Vector2): Vector3 {
        colorMap?.let {
            return it.sample(uv)
        }
        return color
    }

    fun computeRoughness(uv: Vector2): Double {
        roughnessMap?.let {
            return it.sample(uv).x
        }
        return roughness
    }

    fun computeMetallic(uv: Vector2): Double {
        metallicMap?.let {
            return it.sample(uv).x
        }
        return metallic
    }
}
