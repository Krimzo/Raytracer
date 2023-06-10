package utility

import math.normalize
import math.vector.Vector3

object Random {
    private val random = java.util.Random()

    fun vector3(minValue: Double, maxValue: Double): Vector3 {
        return Vector3(
            random.nextDouble(minValue, maxValue),
            random.nextDouble(minValue, maxValue),
            random.nextDouble(minValue, maxValue),
        )
    }

    fun unitSphere(): Vector3 {
        return normalize(vector3(-1.0, 1.0))
    }
}
