#pragma once

#include "math/float3.cuh"
#include "math/int4.cuh"


namespace kl {
	class float4 {
	public:
		union {
			struct {
				float x, y, z, w;
			};
			struct {
				float r, g, b, a;
			};
			float data[4];
		};

		// Constr
		ALL float4() : x(0.0f), y(0.0f), z(0.0f), w(0.0f) {}
		ALL float4(float a) : x(a), y(a), z(a), w(a) {}
		ALL float4(float x, float y, float z, float w) : x(x), y(y), z(z), w(w) {}
		ALL float4(const kl::int4& v) : x(float(v.x)), y(float(v.y)), z(float(v.z)), w(float(v.w)) {}
		ALL float4(const kl::float2& v, float z, float w) : x(v.x), y(v.y), z(z), w(w) {}
		ALL float4(float x, const kl::float2& v, float w) : x(x), y(v.x), z(v.y), w(w) {}
		ALL float4(float x, float y, const kl::float2& v) : x(x), y(y), z(v.x), w(v.y) {}
		ALL float4(const kl::float2& v1, const kl::float2& v2) : x(v1.x), y(v1.y), z(v2.x), w(v2.y) {}
		ALL float4(const kl::float3& v) : x(v.x), y(v.y), z(v.z), w(1.0f) {}
		ALL float4(const kl::float3& v, float w) : x(v.x), y(v.y), z(v.z), w(w) {}
		ALL float4(float x, const kl::float3& v) : x(x), y(v.x), z(v.y), w(v.z) {}
		ALL float4(const kl::color& c) : x(c.r * 0.00392156862f), y(c.g * 0.00392156862f), z(c.b * 0.00392156862f), w(c.a * 0.00392156862f) {}

		// Getters
		ALL kl::float3 xyz() const {
			return kl::float3(x, y, z);
		}
		ALL float& operator[](int i) {
			return data[i];
		}
		ALL const float& operator[](int i) const {
			return data[i];
		}

		// Addition
		ALL kl::float4 add(const kl::float4& obj) const {
			return kl::float4(x + obj.x, y + obj.y, z + obj.z, w + obj.w);
		}
		ALL kl::float4 operator+(const kl::float4& obj) const {
			return add(obj);
		}
		ALL void operator+=(const kl::float4& obj) {
			x += obj.x; y += obj.y; z += obj.z; w += obj.w;
		}

		// Subtraction
		ALL kl::float4 sub(const kl::float4& obj) const {
			return kl::float4(x - obj.x, y - obj.y, z - obj.z, w - obj.w);
		}
		ALL kl::float4 operator-(const kl::float4& obj) const {
			return sub(obj);
		}
		ALL void operator-=(const kl::float4& obj) {
			x -= obj.x; y -= obj.y; z -= obj.z; w -= obj.w;
		}

		// Multiplication
		ALL kl::float4 mul(float a) const {
			return kl::float4(x * a, y * a, z * a, w * a);
		}
		ALL kl::float4 operator*(float a) const {
			return mul(a);
		}
		ALL void operator*=(float a) {
			x *= a; y *= a; z *= a; w *= a;
		}
		ALL kl::float4 mul(const kl::float4& obj) const {
			return kl::float4(x * obj.x, y * obj.y, z * obj.z, w * obj.w);
		}
		ALL kl::float4 operator*(const kl::float4& obj) const {
			return mul(obj);
		}
		ALL void operator*=(const kl::float4& obj) {
			x *= obj.x; y *= obj.y; z *= obj.z; w *= obj.w;
		}

		// Division
		ALL kl::float4 div(float a) const {
			return mul(1.0f / a);
		}
		ALL kl::float4 operator/(float a) const {
			return div(a);
		}
		ALL void operator/=(float a) {
			operator*=(1.0f / a);
		}
		ALL kl::float4 div(const kl::float4& obj) const {
			return kl::float4(x / obj.x, y / obj.y, z / obj.z, w / obj.w);
		}
		ALL kl::float4 operator/(const kl::float4& obj) const {
			return div(obj);
		}
		ALL void operator/=(const kl::float4& obj) {
			x /= obj.x; y /= obj.y; z /= obj.z; w /= obj.w;
		}

		// Comparison
		ALL bool equals(const kl::float4& obj) const {
			return x == obj.x && y == obj.y && z == obj.z && w == obj.w;
		}
		ALL bool operator==(const kl::float4& obj) const {
			return equals(obj);
		}
		ALL bool operator!=(const kl::float4& obj) const {
			return !equals(obj);
		}

		// Returns a negated vector
		ALL kl::float4 negate() const {
			return mul(-1.0f);
		}

		// Returns an absolute vector
		ALL kl::float4 abso() const {
			return kl::float4(abs(x), abs(y), abs(z), abs(w));
		}

		// Returns the vectors length
		ALL float length() const {
			return sqrt(x * x + y * y + z * z + w * w);
		}

		// Retruns a normalized vector
		ALL kl::float4 normalize() const {
			return div(length());
		}

		// Returns the dot product
		ALL float dot(const kl::float4& a) const {
			return x * a.x + y * a.y + z * a.z + w * a.w;
		}

		// Returns the angle between the given vector and self
		ALL float angle(const kl::float4& a) const {
			return acos(normalize().dot(a.normalize())) * 57.2957795131f;
		}
	};
}
