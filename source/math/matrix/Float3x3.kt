package math.matrix

import math.toRadians
import math.vector.Float2
import math.vector.Float3
import java.io.Serializable
import kotlin.math.cos
import kotlin.math.sin

class Float3x3() : Matrix(3, 3), Serializable {
    constructor(data: FloatArray) : this() {
        System.arraycopy(data, 0, this.data, 0, this.data.size)
    }

    constructor(matrix: Matrix) : this(matrix.data)

    operator fun plus(mat: Float3x3): Float3x3 {
        return Float3x3(super.add(mat))
    }

    operator fun minus(mat: Float3x3): Float3x3 {
        return Float3x3(super.subtract(mat))
    }

    operator fun times(value: Float): Float3x3 {
        return Float3x3(super.multiply(value))
    }

    operator fun times(vec: Float3): Float3 {
        return Float3(super.multiply(vec.array, 1, 3).data)
    }

    operator fun times(mat: Float3x3): Float3x3 {
        return Float3x3(super.multiply(mat))
    }

    operator fun unaryMinus(): Float3x3 {
        return Float3x3(super.negate())
    }

    fun abs(): Float3x3 {
        return Float3x3(super.absolute())
    }

    fun tran(): Float3x3 {
        return Float3x3(super.transpose())
    }

    fun inv(): Float3x3 {
        return Float3x3(super.inverse())
    }

    companion object {
        fun translation(translation: Float2): Float3x3 {
            val result = Float3x3()
            result.data[2] = translation.x
            result.data[5] = translation.y
            return result
        }

        fun rotation(rotation: Float): Float3x3 {
            val zSin = sin(toRadians(rotation))
            val zCos = cos(toRadians(rotation))
            val result = Float3x3()
            result.data[0] = zCos
            result.data[1] = -zSin
            result.data[3] = zSin
            result.data[4] = zCos
            return result
        }

        fun scaling(size: Float2): Float3x3 {
            val result = Float3x3()
            result.data[0] = size.x
            result.data[4] = size.y
            return result
        }
    }
}