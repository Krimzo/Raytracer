package scene

import math.vector.Vector3
import java.io.Serializable

class Material : Serializable {
    var albedo = Vector3(1.0)
    var roughness = 1.0
    var metallic = 0.0

    var emissionColor = Vector3()
    var emissionPower = 0.0

    fun totalEmission(): Vector3 {
        return emissionColor * emissionPower
    }
}
