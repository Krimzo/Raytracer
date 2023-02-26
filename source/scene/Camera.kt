package scene

import math.matrix.Matrix4x4
import math.normalize
import math.vector.Vector3
import java.io.Serializable

class Camera : Serializable {
    var fov = 60.0
    var aspect = 16.0 / 9.0

    var near = 0.01
    var far = 50.0

    var position = Vector3()
    var direction = Vector3.FORWARD
        set(direction) { field = normalize(direction) }

    var background = Vector3(0.1)

    fun matrix(): Matrix4x4 {
        return Matrix4x4.perspective(fov, aspect, near, far) * Matrix4x4.lookAt(position, position + direction, Vector3.UP)
    }
}
