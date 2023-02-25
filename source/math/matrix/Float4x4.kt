package math.matrix

import math.normalize
import math.toRadians
import math.vector.Float3
import math.vector.Float4
import java.io.Serializable
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class Float4x4() : Matrix(4, 4), Serializable {
    constructor(data: FloatArray) : this() {
        System.arraycopy(data, 0, this.data, 0, this.data.size)
    }

    constructor(matrix: Matrix) : this(matrix.data)

    operator fun plus(mat: Float4x4): Float4x4 {
        return Float4x4(super.add(mat))
    }

    operator fun minus(mat: Float4x4): Float4x4 {
        return Float4x4(super.subtract(mat))
    }

    operator fun times(value: Float): Float4x4 {
        return Float4x4(super.multiply(value))
    }

    operator fun times(vec: Float4): Float4 {
        return Float4(super.multiply(vec.array, 1, 4).data)
    }

    operator fun times(mat: Float4x4): Float4x4 {
        return Float4x4(super.multiply(mat))
    }

    operator fun unaryMinus(): Float4x4 {
        return Float4x4(super.negate())
    }

    fun abs(): Float4x4 {
        return Float4x4(super.absolute())
    }

    fun tran(): Float4x4 {
        return Float4x4(super.transpose())
    }

    fun inv(): Float4x4 {
        return Float4x4(super.inverse())
    }

    companion object {
        fun translation(translation: Float3): Float4x4 {
            val result = Float4x4()
            result.data[3] = translation.x
            result.data[7] = translation.y
            result.data[11] = translation.z
            return result
        }

        fun rotation(rotation: Float3): Float4x4 {
            val xSin = sin(toRadians(rotation.x))
            val xCos = cos(toRadians(rotation.x))
            val xRot = Float4x4()
            xRot.data[5] = xCos
            xRot.data[6] = -xSin
            xRot.data[9] = xSin
            xRot.data[10] = xCos

            val ySin = sin(toRadians(rotation.y))
            val yCos = cos(toRadians(rotation.y))
            val yRot = Float4x4()
            yRot.data[0] = yCos
            yRot.data[2] = ySin
            yRot.data[8] = -ySin
            yRot.data[10] = yCos

            val zSin = sin(toRadians(rotation.z))
            val zCos = cos(toRadians(rotation.z))
            val zRot = Float4x4()
            zRot.data[0] = zCos
            zRot.data[1] = -zSin
            zRot.data[4] = zSin
            zRot.data[5] = zCos

            return zRot * yRot * xRot
        }

        fun scaling(size: Float3): Float4x4 {
            val result = Float4x4()
            result.data[0] = size.x
            result.data[5] = size.y
            result.data[10] = size.z
            return result
        }

        fun perspective(fov: Float, aspect: Float, zNear: Float, zFar: Float): Float4x4 {
            val xScale = 1f / tan(toRadians(fov * 0.5f))
            val yScale = xScale * aspect
            val result = Float4x4()

            result.data[0] = xScale
            result.data[5] = yScale
            result.data[10] = zFar / (zNear - zFar)
            result.data[11] = zNear * zFar / (zNear - zFar)
            result.data[14] = -1f
            result.data[15] = 0f
            return result
        }

        fun orthographic(width: Float, height: Float, zNear: Float, zFar: Float): Float4x4 {
            val result = Float4x4()
            result.data[0] = 2 / width
            result.data[5] = 2 / height
            result.data[10] = 1 / (zNear - zFar)
            result.data[11] = zNear / (zNear - zFar)
            return result
        }

        fun lookAt(pos: Float3, tar: Float3, up: Float3): Float4x4 {
            val f = normalize(pos - tar)
            val s = normalize(up x f)
            val u = f x s

            val result = Float4x4()
            result.data[0] = s.x
            result.data[1] = s.y
            result.data[2] = s.z
            result.data[3] = -(s * pos)
            result.data[4] = u.x
            result.data[5] = u.y
            result.data[6] = u.z
            result.data[7] = -(u * pos)
            result.data[8] = f.x
            result.data[9] = f.y
            result.data[10] = f.z
            result.data[11] = -(f * pos)
            return result
        }
    }
}