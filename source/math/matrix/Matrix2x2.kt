package math.matrix

import math.vector.Vector2
import java.io.Serializable

class Matrix2x2 : Serializable {
    val data: DoubleArray = DoubleArray(4) {
        if (it % 2 == it / 2) 1.0 else 0.0
    }

    constructor()

    constructor(other: Matrix2x2) {
        System.arraycopy(other.data, 0, data, 0, 4)
    }

    // Get
    operator fun get(index: Int): Double {
        return data[index]
    }

    operator fun invoke(x: Int, y: Int): Double {
        return data[x + y * 2]
    }

    // Compare
    override fun equals(other: Any?): Boolean {
        if (other !is Matrix2x2) { return false }

        for (i in 0 until 4) {
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
    operator fun plus(other: Matrix2x2): Matrix2x2 {
        val result = Matrix2x2(this)
        for (i in 0 until 4) {
            result.data[i] += other[i]
        }
        return result
    }

    operator fun plusAssign(other: Matrix2x2) {
        for (i in 0 until 4) {
            data[i] += other[i]
        }
    }

    operator fun minus(other: Matrix2x2): Matrix2x2 {
        val result = Matrix2x2(this)
        for (i in 0 until 4) {
            result.data[i] -= other[i]
        }
        return result
    }

    operator fun minusAssign(other: Matrix2x2) {
        for (i in 0 until 4) {
            data[i] -= other[i]
        }
    }

    operator fun times(value: Double): Matrix2x2 {
        val result = Matrix2x2(this)
        for (i in 0 until 4) {
            result.data[i] *= value
        }
        return result
    }

    operator fun timesAssign(value: Double) {
        for (i in 0 until 4) {
            data[i] *= value
        }
    }

    operator fun times(other: Matrix2x2): Matrix2x2 {
        val result = Matrix2x2()
        for (y in 0 until 2) {
            for (x in 0 until 2) {
                result.data[x + y * 2] = 0.0
                for (i in 0 until 2) {
                    result.data[x + y * 2] += this(i, y) * other(x, i)
                }
            }
        }
        return result
    }

    operator fun timesAssign(other: Matrix2x2) {
        val result = this * other
        System.arraycopy(result.data, 0, data, 0, 4)
    }

    operator fun times(vec: Vector2): Vector2 {
        val input = vec.array
        val result = DoubleArray(2)
        for (y in 0 until 2) {
            for (i in 0 until 2) {
                result[y] += this(i, y) * input[i]
            }
        }
        return Vector2(result)
    }

    fun determinant(): Double {
        val result = (data[0] * data[3] - data[1] * data[2])
        return (1.0 / result)
    }
}
