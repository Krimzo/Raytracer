package scene

import math.matrix.Float4x4
import math.normalize
import math.vector.Float3
import java.io.Serializable

class Camera : Serializable {
    var fov = 60f
    var aspect = 16f / 9f

    var near = 0.01f
    var far = 50f

    var position = Float3()
    var direction = Float3(0f, 0f, -1f)
        set(dir) { field = normalize(dir) }

    var background = Float3(0.1f)

    fun matrix(): Float4x4 {
        return Float4x4.perspective(fov, aspect, near, far) * Float4x4.lookAt(position, position + direction, Float3.posY)
    }
}
