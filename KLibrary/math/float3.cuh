#pragma once

#include "math/float2.cuh"
#include "math/int3.cuh"
#include "graphics/color.cuh"


namespace kl {
	class float3 {
	public:
		union {
			struct {
				float x, y, z;
			};
			struct {
				float r, g, b;
			};
			float data[3];
		};

		// Constr
		ALL float3() : x(0.0f), y(0.0f), z(0.0f) {}
		ALL float3(float a) : x(a), y(a), z(a) {}
		ALL float3(float x, float y, float z) : x(x), y(y), z(z) {}
		ALL float3(const kl::int3& v) : x(float(v.x)), y(float(v.y)), z(float(v.z)) {}
		ALL float3(const kl::float2& v, float z) : x(v.x), y(v.y), z(z) {}
		ALL float3(float x, const kl::float2& v) : x(x), y(v.x), z(v.y) {}
		ALL float3(const kl::color& c) : x(c.r * 0.00392156862f), y(c.g * 0.00392156862f), z(c.b * 0.00392156862f) {}

		// Getters
		ALL kl::float2 xy() const {
			return kl::float2(x, y);
		}
		ALL float& operator[](int i) {
			return data[i];
		}
		ALL const float& operator[](int i) const {
			return data[i];
		}

		// Addition
		ALL kl::float3 add(const kl::float3& obj) const {
			return kl::float3(x + obj.x, y + obj.y, z + obj.z);
		}
		ALL kl::float3 operator+(const kl::float3& obj) const {
			return add(obj);
		}
		ALL void operator+=(const kl::float3& obj) {
			x += obj.x; y += obj.y; z += obj.z;
		}

		// Subtraction
		ALL kl::float3 sub(const kl::float3& obj) const {
			return kl::float3(x - obj.x, y - obj.y, z - obj.z);
		}
		ALL kl::float3 operator-(const kl::float3& obj) const {
			return sub(obj);
		}
		ALL void operator-=(const kl::float3& obj) {
			x -= obj.x; y -= obj.y; z -= obj.z;
		}

		// Multiplication
		ALL kl::float3 mul(float a) const {
			return kl::float3(x * a, y * a, z * a);
		}
		ALL kl::float3 operator*(float a) const {
			return mul(a);
		}
		ALL void operator*=(float a) {
			x *= a; y *= a; z *= a;
		}
		ALL kl::float3 mul(const kl::float3& obj) const {
			return kl::float3(x * obj.x, y * obj.y, z * obj.z);
		}
		ALL kl::float3 operator*(const kl::float3& obj) const {
			return mul(obj);
		}
		ALL void operator*=(const kl::float3& obj) {
			x *= obj.x; y *= obj.y; z *= obj.z;
		}

		// Division
		ALL kl::float3 div(float a) const {
			return mul(1.0f / a);
		}
		ALL kl::float3 operator/(float a) const {
			return div(a);
		}
		ALL void operator/=(float a) {
			operator*=(1.0f / a);
		}
		ALL kl::float3 div(const kl::float3& obj) const {
			return kl::float3(x / obj.x, y / obj.y, z / obj.z);
		}
		ALL kl::float3 operator/(const kl::float3& obj) const {
			return div(obj);
		}
		ALL void operator/=(const kl::float3& obj) {
			x /= obj.x; y /= obj.y; z /= obj.z;
		}

		// Comparison
		ALL bool equals(const kl::float3& obj) const {
			return x == obj.x && y == obj.y && z == obj.z;
		}
		ALL bool operator==(const kl::float3& obj) const {
			return equals(obj);
		}
		ALL bool operator!=(const kl::float3& obj) const {
			return !equals(obj);
		}

		// Returns a negated vector
		ALL kl::float3 negate() const {
			return mul(-1.0f);
		}

		// Returns an absolute vector
		ALL kl::float3 abso() const {
			return kl::float3(abs(x), abs(y), abs(z));
		}

		// Returns the vectors length
		ALL float length() const {
			return sqrt(x * x + y * y + z * z);
		}

		// Retruns a normalized vector
		ALL kl::float3 normalize() const {
			return div(length());
		}

		// Returns the dot product
		ALL float dot(const kl::float3& a) const {
			return x * a.x + y * a.y + z * a.z;
		}

		// Returns the cross product
		ALL kl::float3 cross(const kl::float3& a) const {
			return kl::float3(y * a.z - z * a.y, z * a.x - x * a.z, x * a.y - y * a.x);
		}

		// Returns the angle between the given vector and self
		ALL float angle(const kl::float3& a) const {
			return acos(normalize().dot(a.normalize())) * 57.2957795131f;
		}

		// Returns a rotated vector around the given axis
		ALL kl::float3 rotate(float angle, const kl::float3& axis) const {
			// Calculating trig funcs
			const float angleSin = sin(angle * 0.00872664625f);
			const float angleCos = cos(angle * 0.00872664625f);

			// Calculating quaternion consts
			const float qx = axis.x * angleSin;
			const float qy = axis.y * angleSin;
			const float qz = axis.z * angleSin;
			const float x2 = qx * qx;
			const float y2 = qy * qy;
			const float z2 = qz * qz;
			const float w2 = angleCos * angleCos;
			const float xy = qx * qy;
			const float xz = qx * qz;
			const float yz = qy * qz;
			const float xw = qx * angleCos;
			const float yw = qy * angleCos;
			const float zw = qz * angleCos;

			// Calculating the rotated vector
			kl::float3 temp;
			temp.x = (w2 + x2 - z2 - y2) * x + (-zw + xy - zw + xy) * y + (yw + xz + xz + yw) * z;
			temp.y = (xy + zw + zw + xy) * x + (y2 - z2 + w2 - x2) * y + (yz + yz - xw - xw) * z;
			temp.z = (xz - yw + xz - yw) * x + (yz + yz + xw + xw) * y + (z2 - y2 - x2 + w2) * z;
			return temp;
		}

		// Reflects the vector
		ALL kl::float3 reflect(const kl::float3 vec) const {
			const kl::float3 normal = vec.normalize();
			return sub(normal * dot(normal) * 2.0f);
		}
	};
}
