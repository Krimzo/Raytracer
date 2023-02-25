package scene.light

import math.vector.Float3
import java.io.Serializable

class AmbientLight : Serializable {
    var color = Float3(1f)
    var strength = 0.1f

    fun getFull(): Float3 {
        return color * strength
    }
}
