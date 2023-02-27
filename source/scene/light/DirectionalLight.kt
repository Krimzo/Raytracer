package scene.light

import logging.Logger
import math.normalize
import math.vector.Vector3
import java.io.Serializable

class DirectionalLight : Serializable {
    var color = Vector3(1.0)
    var direction = Vector3.DOWN
        set(direction) { field = normalize(direction) }

    init {
        Logger.log("Created directional light")
    }

    fun getFull(normal: Vector3): Vector3 {
        val factor = (-direction * normal).coerceAtLeast(0.0)
        return (color * factor)
    }
}
