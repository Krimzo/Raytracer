package math

import math.matrix.Matrix2x2
import math.matrix.Matrix3x3
import math.matrix.Matrix4x4
import math.vector.Vector2
import math.vector.Vector3
import math.vector.Vector4
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

// Double
fun toRadians(value: Double): Double {
    return Math.toRadians(value)
}

fun toDegrees(value: Double): Double {
    return Math.toDegrees(value)
}

fun lerp(a: Double, b: Double, ratio: Double): Double {
    val ratio = ratio.coerceAtLeast(0.0).coerceAtMost(1.0)
    return (a * (1.0 - ratio) + b * ratio)
}

// Vector2
fun abs(vec: Vector2): Vector2 {
    return Vector2(abs(vec.x), abs(vec.y))
}

fun normalize(vec: Vector2): Vector2 {
    return vec / vec.length
}

fun toRadians(vec: Vector2): Vector2 {
    return Vector2(toRadians(vec.x), toRadians(vec.y))
}

fun toDegrees(vec: Vector2): Vector2 {
    return Vector2(toDegrees(vec.x), toDegrees(vec.y))
}

fun lerp(a: Vector2, b: Vector2, ratio: Double): Vector2 {
    return Vector2(lerp(a.x, b.x, ratio), lerp(a.y, b.y, ratio))
}

fun angle(first: Vector2, second: Vector2): Double {
    return toDegrees(acos(normalize(first) * normalize(second)))
}

fun rotate(vec: Vector2, angle: Double): Vector2 {
    val sinA = sin(toRadians(angle))
    val cosA = cos(toRadians(angle))
    return Vector2(cosA * vec.x - sinA * vec.y, sinA * vec.x + cosA * vec.y)
}

// Vector3
fun abs(vec: Vector3): Vector3 {
    return Vector3(abs(vec.x), abs(vec.y), abs(vec.z))
}

fun normalize(vec: Vector3): Vector3 {
    return vec / vec.length
}

fun toRadians(vec: Vector3): Vector3 {
    return Vector3(toRadians(vec.x), toRadians(vec.y), toRadians(vec.z))
}

fun toDegrees(vec: Vector3): Vector3 {
    return Vector3(toDegrees(vec.x), toDegrees(vec.y), toDegrees(vec.z))
}

fun lerp(a: Vector3, b: Vector3, ratio: Double): Vector3 {
    return Vector3(lerp(a.x, b.x, ratio), lerp(a.y, b.y, ratio), lerp(a.z, b.z, ratio))
}

fun angle(first: Vector3, second: Vector3): Double {
    return toDegrees(acos(normalize(first) * normalize(second)))
}

fun rotate(vec: Vector3, axis: Vector3, angle: Double): Vector3 {
    val angleSin = sin(toRadians(angle * 0.5))
    val angleCos = cos(toRadians(angle * 0.5))
    val qx = axis.x * angleSin
    val qy = axis.y * angleSin
    val qz = axis.z * angleSin
    val x2 = qx * qx
    val y2 = qy * qy
    val z2 = qz * qz
    val w2 = angleCos * angleCos
    val xy = qx * qy
    val xz = qx * qz
    val yz = qy * qz
    val xw = qx * angleCos
    val yw = qy * angleCos
    val zw = qz * angleCos

    val result = Vector3()
    result.x = (w2 + x2 - z2 - y2) * vec.x + (-zw + xy - zw + xy) * vec.y + (yw + xz + xz + yw) * vec.z
    result.y = (xy + zw + zw + xy) * vec.x + (+y2 - z2 + w2 - x2) * vec.y + (yz + yz - xw - xw) * vec.z
    result.z = (xz - yw + xz - yw) * vec.x + (+yz + yz + xw + xw) * vec.y + (z2 - y2 - x2 + w2) * vec.z
    return result
}

fun reflect(vec: Vector3, normal: Vector3): Vector3 {
    val normal = normalize(normal)
    return (vec - (normal * (vec * normal * 2.0)))
}

// Vector4
fun abs(vec: Vector4): Vector2 {
    return Vector2(abs(vec.x), abs(vec.y))
}

fun normalize(vec: Vector4): Vector4 {
    return (vec / vec.length)
}

fun toRadians(vec: Vector4): Vector4 {
    return Vector4(toRadians(vec.x), toRadians(vec.y), toDegrees(vec.z), toDegrees(vec.w))
}

fun toDegrees(vec: Vector4): Vector4 {
    return Vector4(toDegrees(vec.x), toDegrees(vec.y), toDegrees(vec.z), toDegrees(vec.w))
}

fun lerp(a: Vector4, b: Vector4, ratio: Double): Vector4 {
    return Vector4(lerp(a.x, b.x, ratio), lerp(a.y, b.y, ratio), lerp(a.z, b.z, ratio), lerp(a.w, b.w, ratio))
}

fun angle(first: Vector4, second: Vector4): Double {
    return toDegrees(acos(normalize(first) * normalize(second)))
}

// Matrix2x2
fun abs(mat: Matrix2x2): Matrix2x2 {
    val result = Matrix2x2()
    for (i in 0 until 4) {
        result.data[i] = abs(mat[i])
    }
    return result
}

fun inverse(mat: Matrix2x2): Matrix2x2 {
    val determinant = mat.determinant()

    val result = Matrix2x2()
    result.data[0] = +mat[3]
    result.data[1] = -mat[1]
    result.data[2] = -mat[2]
    result.data[3] = +mat[0]
    return (result * determinant)
}

fun transpose(mat: Matrix2x2): Matrix2x2 {
    val result = Matrix2x2()
    for (y in 0 until 2) {
        for (x in 0 until 2) {
            result.data[x + y * 2] = mat(y, x)
        }
    }
    return result
}

// Matrix3x3
fun abs(mat: Matrix3x3): Matrix3x3 {
    val result = Matrix3x3()
    for (i in 0 until 9) {
        result.data[i] = abs(mat[i])
    }
    return result
}

fun inverse(mat: Matrix3x3): Matrix3x3 {
    val determinant = mat.determinant()

    val result = Matrix3x3()
    result.data[0] = (mat(1, 1) * mat(2, 2) - mat(2, 1) * mat(1, 2)) * determinant
    result.data[3] = (mat(0, 2) * mat(2, 1) - mat(0, 1) * mat(2, 2)) * determinant
    result.data[6] = (mat(0, 1) * mat(1, 2) - mat(0, 2) * mat(1, 1)) * determinant
    result.data[1] = (mat(1, 2) * mat(2, 0) - mat(1, 0) * mat(2, 2)) * determinant
    result.data[4] = (mat(0, 0) * mat(2, 2) - mat(0, 2) * mat(2, 0)) * determinant
    result.data[7] = (mat(1, 0) * mat(0, 2) - mat(0, 0) * mat(1, 2)) * determinant
    result.data[2] = (mat(1, 0) * mat(2, 1) - mat(2, 0) * mat(1, 1)) * determinant
    result.data[5] = (mat(2, 0) * mat(0, 1) - mat(0, 0) * mat(2, 1)) * determinant
    result.data[8] = (mat(0, 0) * mat(1, 1) - mat(1, 0) * mat(0, 1)) * determinant
    return result
}

fun transpose(mat: Matrix3x3): Matrix3x3 {
    val result = Matrix3x3()
    for (y in 0 until 3) {
        for (x in 0 until 3) {
            result.data[x + y * 3] = mat(y, x)
        }
    }
    return result
}

// Matrix4x4
fun abs(mat: Matrix4x4): Matrix4x4 {
    val result = Matrix4x4()
    for (i in 0 until 16) {
        result.data[i] = abs(mat[i])
    }
    return result
}

fun inverse(mat: Matrix4x4): Matrix4x4 {
    val determinant = mat.determinant()

    val a2323 = mat(2, 2) * mat(3, 3) - mat(3, 2) * mat(2, 3)
    val a1323 = mat(1, 2) * mat(3, 3) - mat(3, 2) * mat(1, 3)
    val a1223 = mat(1, 2) * mat(2, 3) - mat(2, 2) * mat(1, 3)
    val a0323 = mat(0, 2) * mat(3, 3) - mat(3, 2) * mat(0, 3)
    val a0223 = mat(0, 2) * mat(2, 3) - mat(2, 2) * mat(0, 3)
    val a0123 = mat(0, 2) * mat(1, 3) - mat(1, 2) * mat(0, 3)
    val a2313 = mat(2, 1) * mat(3, 3) - mat(3, 1) * mat(2, 3)
    val a1313 = mat(1, 1) * mat(3, 3) - mat(3, 1) * mat(1, 3)
    val a1213 = mat(1, 1) * mat(2, 3) - mat(2, 1) * mat(1, 3)
    val a2312 = mat(2, 1) * mat(3, 2) - mat(3, 1) * mat(2, 2)
    val a1312 = mat(1, 1) * mat(3, 2) - mat(3, 1) * mat(1, 2)
    val a1212 = mat(1, 1) * mat(2, 2) - mat(2, 1) * mat(1, 2)
    val a0313 = mat(0, 1) * mat(3, 3) - mat(3, 1) * mat(0, 3)
    val a0213 = mat(0, 1) * mat(2, 3) - mat(2, 1) * mat(0, 3)
    val a0312 = mat(0, 1) * mat(3, 2) - mat(3, 1) * mat(0, 2)
    val a0212 = mat(0, 1) * mat(2, 2) - mat(2, 1) * mat(0, 2)
    val a0113 = mat(0, 1) * mat(1, 3) - mat(1, 1) * mat(0, 3)
    val a0112 = mat(0, 1) * mat(1, 2) - mat(1, 1) * mat(0, 2)

    val result = Matrix4x4()
    result.data[ 0] = +(mat(1, 1) * a2323 - mat(2, 1) * a1323 + mat(3, 1) * a1223) * determinant
    result.data[ 1] = -(mat(1, 0) * a2323 - mat(2, 0) * a1323 + mat(3, 0) * a1223) * determinant
    result.data[ 2] = +(mat(1, 0) * a2313 - mat(2, 0) * a1313 + mat(3, 0) * a1213) * determinant
    result.data[ 3] = -(mat(1, 0) * a2312 - mat(2, 0) * a1312 + mat(3, 0) * a1212) * determinant
    result.data[ 4] = -(mat(0, 1) * a2323 - mat(2, 1) * a0323 + mat(3, 1) * a0223) * determinant
    result.data[ 5] = +(mat(0, 0) * a2323 - mat(2, 0) * a0323 + mat(3, 0) * a0223) * determinant
    result.data[ 6] = -(mat(0, 0) * a2313 - mat(2, 0) * a0313 + mat(3, 0) * a0213) * determinant
    result.data[ 7] = +(mat(0, 0) * a2312 - mat(2, 0) * a0312 + mat(3, 0) * a0212) * determinant
    result.data[ 8] = +(mat(0, 1) * a1323 - mat(1, 1) * a0323 + mat(3, 1) * a0123) * determinant
    result.data[ 9] = -(mat(0, 0) * a1323 - mat(1, 0) * a0323 + mat(3, 0) * a0123) * determinant
    result.data[10] = +(mat(0, 0) * a1313 - mat(1, 0) * a0313 + mat(3, 0) * a0113) * determinant
    result.data[11] = -(mat(0, 0) * a1312 - mat(1, 0) * a0312 + mat(3, 0) * a0112) * determinant
    result.data[12] = -(mat(0, 1) * a1223 - mat(1, 1) * a0223 + mat(2, 1) * a0123) * determinant
    result.data[13] = +(mat(0, 0) * a1223 - mat(1, 0) * a0223 + mat(2, 0) * a0123) * determinant
    result.data[14] = -(mat(0, 0) * a1213 - mat(1, 0) * a0213 + mat(2, 0) * a0113) * determinant
    result.data[15] = +(mat(0, 0) * a1212 - mat(1, 0) * a0212 + mat(2, 0) * a0112) * determinant
    return result
}

fun transpose(mat: Matrix4x4): Matrix4x4 {
    val result = Matrix4x4()
    for (y in 0 until 4) {
        for (x in 0 until 4) {
            result.data[x + y * 4] = mat(y, x)
        }
    }
    return result
}
