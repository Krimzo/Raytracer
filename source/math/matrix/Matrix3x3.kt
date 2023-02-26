package math.matrix

import math.toRadians
import math.vector.Vector2
import math.vector.Vector3
import java.io.Serializable
import kotlin.math.cos
import kotlin.math.sin

class Matrix3x3 : Serializable {
    val data: DoubleArray = DoubleArray(9) {
        if (it % 3 == it / 3) 1.0 else 0.0
    }

    constructor()

    constructor(other: Matrix3x3) {
        System.arraycopy(other.data, 0, data, 0, 9)
    }

    // Get
    operator fun get(index: Int): Double {
        return data[index]
    }

    operator fun invoke(x: Int, y: Int): Double {
        return data[x + y * 3]
    }

    // Compare
    override fun equals(other: Any?): Boolean {
        if (other !is Matrix3x3) { return false }

        for (i in 0 until 9) {
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
    operator fun plus(other: Matrix3x3): Matrix3x3 {
        val result = Matrix3x3(this)
        for (i in 0 until 9) {
            result.data[i] += other[i]
        }
        return result
    }

    operator fun plusAssign(other: Matrix3x3) {
        for (i in 0 until 9) {
            data[i] += other[i]
        }
    }

    operator fun minus(other: Matrix3x3): Matrix3x3 {
        val result = Matrix3x3(this)
        for (i in 0 until 9) {
            result.data[i] -= other[i]
        }
        return result
    }

    operator fun minusAssign(other: Matrix3x3) {
        for (i in 0 until 9) {
            data[i] -= other[i]
        }
    }

    operator fun times(value: Double): Matrix3x3 {
        val result = Matrix3x3(this)
        for (i in 0 until 9) {
            result.data[i] *= value
        }
        return result
    }

    operator fun timesAssign(value: Double) {
        for (i in 0 until 9) {
            data[i] *= value
        }
    }

    operator fun times(other: Matrix3x3): Matrix3x3 {
        val result = Matrix3x3()
        for (y in 0 until 3) {
            for (x in 0 until 3) {
                result.data[x + y * 3] = 0.0
                for (i in 0 until 3) {
                    result.data[x + y * 3] += this(i, y) * other(x, i)
                }
            }
        }
        return result
    }

    operator fun timesAssign(other: Matrix3x3) {
        val result = this * other
        System.arraycopy(result.data, 0, data, 0, 9)
    }

    operator fun times(vec: Vector3): Vector3 {
        val input = vec.array
        val result = DoubleArray(3)
        for (y in 0 until 3) {
            for (i in 0 until 3) {
                result[y] += this(i, y) * input[i]
            }
        }
        return Vector3(result)
    }

    fun determinant(): Double {
        val result = this(0, 0) * (this(1, 1) * this(2, 2) - this(2, 1) * this(1, 2)) -
                this(0, 1) * (this(1, 0) * this(2, 2) - this(1, 2) * this(2, 0)) +
                this(0, 2) * (this(1, 0) * this(2, 1) - this(1, 1) * this(2, 0))
        return (1.0 / result)
    }

    companion object {
        fun translation(translation: Vector2): Matrix3x3 {
            val result = Matrix3x3()
            result.data[2] = translation.x
            result.data[5] = translation.y
            return result
        }

        fun rotation(rotation: Double): Matrix3x3 {
            val zSin = sin(toRadians(rotation))
            val zCos = cos(toRadians(rotation))

            val result = Matrix3x3()
            result.data[0] = zCos
            result.data[1] = -zSin
            result.data[3] = zSin
            result.data[4] = zCos
            return result
        }

        fun scaling(scale: Vector2): Matrix3x3 {
            val result = Matrix3x3()
            result.data[0] = scale.x
            result.data[4] = scale.y
            return result
        }
    }
}
