package scene.light

import math.vector.Vector3
import java.io.Serializable

class AmbientLight : Serializable {
    var color = Vector3(1.0)
    var strength = 0.1

    fun getFull(): Vector3 {
        return color * strength
    }
}
