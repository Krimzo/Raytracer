package math.matrix

import math.normalize
import math.toRadians
import math.vector.Vector3
import math.vector.Vector4
import java.io.Serializable
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class Matrix4x4 : Serializable {
    val data: DoubleArray = DoubleArray(16) {
        if (it % 4 == it / 4) 1.0 else 0.0
    }

    constructor()

    constructor(other: Matrix4x4) {
        System.arraycopy(other.data, 0, data, 0, 16)
    }

    // Get
    operator fun get(index: Int): Double {
        return data[index]
    }

    operator fun invoke(x: Int, y: Int): Double {
        return data[x + y * 4]
    }

    // Compare
    override fun equals(other: Any?): Boolean {
        if (other !is Matrix4x4) { return false }

        for (i in 0 until 16) {
            if (other[i] != data[i]) {
                return false
            }
        }
        return true
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }

    // Math
    operator fun plus(other: Matrix4x4): Matrix4x4 {
        val result = Matrix4x4(this)
        for (i in 0 until 16) {
            result.data[i] += other[i]
        }
        return result
    }

    operator fun plusAssign(other: Matrix4x4) {
        for (i in 0 until 16) {
            data[i] += other[i]
        }
    }

    operator fun minus(other: Matrix4x4): Matrix4x4 {
        val result = Matrix4x4(this)
        for (i in 0 until 16) {
            result.data[i] -= other[i]
        }
        return result
    }

    operator fun minusAssign(other: Matrix4x4) {
        for (i in 0 until 16) {
            data[i] -= other[i]
        }
    }

    operator fun times(value: Double): Matrix4x4 {
        val result = Matrix4x4(this)
        for (i in 0 until 16) {
            result.data[i] *= value
        }
        return result
    }

    operator fun timesAssign(value: Double) {
        for (i in 0 until 16) {
            data[i] *= value
        }
    }

    operator fun times(other: Matrix4x4): Matrix4x4 {
        val result = Matrix4x4()
        for (y in 0 until 4) {
            for (x in 0 until 4) {
                result.data[x + y * 4] = 0.0
                for (i in 0 until 4) {
                    result.data[x + y * 4] += this(i, y) * other(x, i)
                }
            }
        }
        return result
    }

    operator fun timesAssign(other: Matrix4x4) {
        val result = this * other
        System.arraycopy(result.data, 0, data, 0, 16)
    }

    operator fun times(vec: Vector4): Vector4 {
        val input = vec.array
        val result = DoubleArray(4)
        for (y in 0 until 4) {
            for (i in 0 until 4) {
                result[y] += this(i, y) * input[i]
            }
        }
        return Vector4(result)
    }

    fun determinant(): Double {
        val a2323 = this(2, 2) * this(3, 3) - this(3, 2) * this(2, 3)
        val a1323 = this(1, 2) * this(3, 3) - this(3, 2) * this(1, 3)
        val a1223 = this(1, 2) * this(2, 3) - this(2, 2) * this(1, 3)
        val a0323 = this(0, 2) * this(3, 3) - this(3, 2) * this(0, 3)
        val a0223 = this(0, 2) * this(2, 3) - this(2, 2) * this(0, 3)
        val a0123 = this(0, 2) * this(1, 3) - this(1, 2) * this(0, 3)

        val result = this(0, 0) * (this(1, 1) * a2323 - this(2, 1) * a1323 + this(3, 1) * a1223) -
                this(1, 0) * (this(0, 1) * a2323 - this(2, 1) * a0323 + this(3, 1) * a0223) +
                this(2, 0) * (this(0, 1) * a1323 - this(1, 1) * a0323 + this(3, 1) * a0123) -
                this(3, 0) * (this(0, 1) * a1223 - this(1, 1) * a0223 + this(2, 1) * a0123)
        return (1.0 / result)
    }

    companion object {
        fun translation(translation: Vector3): Matrix4x4 {
            val result = Matrix4x4()
            result.data[3] = translation.x
            result.data[7] = translation.y
            result.data[11] = translation.z
            return result
        }

        fun rotation(rotation: Vector3): Matrix4x4 {
            // X
            val xRad = toRadians(rotation.x)
            val xSin = sin(xRad)
            val xCos = cos(xRad)

            val xRot = Matrix4x4()
            xRot.data[5] = xCos
            xRot.data[6] = -xSin
            xRot.data[9] = xSin
            xRot.data[10] = xCos

            // Y
            val yRad = toRadians(rotation.y)
            val ySin = sin (yRad)
            val yCos = cos (yRad)

            val yRot = Matrix4x4()
            yRot.data[0] = yCos
            yRot.data[2] = ySin
            yRot.data[8] = -ySin
            yRot.data[10] = yCos

            // Z
            val zRad = toRadians(rotation.z)
            val zSin = sin (zRad)
            val zCos = cos (zRad)

            val zRot = Matrix4x4()
            zRot.data[0] = zCos
            zRot.data[1] = -zSin
            zRot.data[4] = zSin
            zRot.data[5] = zCos

            // Result
            return (zRot * yRot * xRot)
        }

        fun scaling(scale: Vector3): Matrix4x4 {
            val result = Matrix4x4()
            result.data[0] = scale.x
            result.data[5] = scale.y
            result.data[10] = scale.z
            return result
        }

        fun perspective(fov: Double, aspect: Double, near: Double, far: Double): Matrix4x4 {
            val tanHalf = 1.0 / tan(toRadians(fov * 0.5))

            val result = Matrix4x4()
            result.data[0] = tanHalf / aspect
            result.data[5] = tanHalf
            result.data[10] = (-far - near) / (near - far)
            result.data[11] = (2.0 * near * far) / (near - far)
            result.data[14] = 1.0
            result.data[15] = 0.0
            return result
        }

        fun orthographic(left: Double, right: Double, bottom: Double, top: Double, nearPlane: Double, farPlane: Double): Matrix4x4 {
            val result = Matrix4x4()
            result.data[0] = 2.0 / (right - left)
            result.data[5] = 2.0 / (top - bottom)
            result.data[10] = 2.0 / (farPlane - nearPlane)
            result.data[3] = -(right + left) / (right - left)
            result.data[7] = -(top + bottom) / (top - bottom)
            result.data[11] = -(farPlane + nearPlane) / (farPlane - nearPlane)
            return result
        }

        fun lookAt(position: Vector3, target: Vector3, up: Vector3): Matrix4x4 {
            val f = normalize(target - position)
            val s = normalize(up x f)
            val u = f x s

            val result = Matrix4x4()
            result.data[0] = s.x
            result.data[1] = s.y
            result.data[2] = s.z
            result.data[3] = -(s * position)
            result.data[4] = u.x
            result.data[5] = u.y
            result.data[6] = u.z
            result.data[7] = -(u * position)
            result.data[8] = f.x
            result.data[9] = f.y
            result.data[10] = f.z
            result.data[11] = -(f * position)
            return result
        }
    }
}
