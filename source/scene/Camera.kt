package scene

import math.matrix.Matrix4x4
import math.normalize
import math.vector.Vector2
import math.vector.Vector3
import math.*
import java.io.Serializable
import kotlin.math.abs

class Camera : Serializable {
    var aspect = 16.0 / 9.0
    var fov = 60.0

    var sensitivity = 0.1
    var speed = 2.0

    var position = Vector3()
    var forward = Vector3.FORWARD
        set(forward) { field = normalize(forward) }
    var up = Vector3.UP
        set(up) { field = normalize(up) }
    val right: Vector3
        get() = up x forward

    fun moveForward(delta: Double) {
        position += forward * speed * delta
    }

    fun moveBack(delta: Double) {
        position -= forward * speed * delta
    }

    fun moveRight(delta: Double) {
        position += right * speed * delta
    }

    fun moveLeft(delta: Double) {
        position -= right * speed * delta
    }

    fun moveUp(delta: Double) {
        position += up * speed * delta
    }

    fun moveDown(delta: Double) {
        position -= up * speed * delta
    }

    fun rotate(position: Vector2, center: Vector2, verticalLimit: Double = 85.0) {
        val rotation = (position - center) * sensitivity
        val forwardVert = rotate(forward, right, rotation.y)
        if (abs(angle(forwardVert, up) - 90.0) <= verticalLimit) {
            forward = forwardVert
        }
        forward = rotate(forward, up, rotation.x)
    }

    fun matrix(): Matrix4x4 {
        val view = Matrix4x4.lookAt(position, position + forward, Vector3.UP)
        val projection = Matrix4x4.perspective(fov, aspect, 1e-3, 1e3)
        return (projection * view)
    }
}
