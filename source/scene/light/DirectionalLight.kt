package scene.light

import math.normalize
import math.vector.Float3
import java.io.Serializable

class DirectionalLight : Serializable {
    var color = Float3(1f)
    var direction = Float3(0f, -1f, 0f)
        set(dir) { field = normalize(dir) }

    fun getFull(normal: Float3): Float3 {
        val factor = (-direction * normal).coerceAtLeast(0f)
        return color * factor
    }
}
