#pragma once

#include "math/int2.cuh"


namespace kl {
	class float2 {
	public:
		union {
			struct {
				float x, y;
			};
			struct {
				float r, g;
			};
			float data[2];
		};

		ALL float2() : x(0.0f), y(0.0f) {}
		ALL float2(float a) : x(a), y(a) {}
		ALL float2(float x, float y) : x(x), y(y) {}
		ALL float2(const kl::int2& v) : x(float(v.x)), y(float(v.y)) {}

		// Getters
		ALL float& operator[](int i) {
			return data[i];
		}
		ALL const float& operator[](int i) const {
			return data[i];
		}

		// Addition
		ALL kl::float2 add(const kl::float2& obj) const {
			return kl::float2(x + obj.x, y + obj.y);
		}
		ALL kl::float2 operator+(const kl::float2& obj) const {
			return add(obj);
		}
		ALL void operator+=(const kl::float2& obj) {
			x += obj.x; y += obj.y;
		}

		// Subtraction
		ALL kl::float2 sub(const kl::float2& obj) const {
			return kl::float2(x - obj.x, y - obj.y);
		}
		ALL kl::float2 operator-(const kl::float2& obj) const {
			return sub(obj);
		}
		ALL void operator-=(const kl::float2& obj) {
			x -= obj.x; y -= obj.y;
		}

		// Multiplication
		ALL kl::float2 mul(float a) const {
			return kl::float2(x * a, y * a);
		}
		ALL kl::float2 operator*(float a) const {
			return mul(a);
		}
		ALL void operator*=(float a) {
			x *= a; y *= a;
		}
		ALL kl::float2 mul(const kl::float2& obj) const {
			return kl::float2(x * obj.x, y * obj.y);
		}
		ALL kl::float2 operator*(const kl::float2& obj) const {
			return mul(obj);
		}
		ALL void operator*=(const kl::float2& obj) {
			x *= obj.x; y *= obj.y;
		}

		// Division
		ALL kl::float2 div(float a) const {
			return mul(1.0f / a);
		}
		ALL kl::float2 operator/(float a) const {
			return div(a);
		}
		ALL void operator/=(float a) {
			operator*=(1.0f / a);
		}
		ALL kl::float2 div(const kl::float2& obj) const {
			return kl::float2(x / obj.x, y / obj.y);
		}
		ALL kl::float2 operator/(const kl::float2& obj) const {
			return div(obj);
		}
		ALL void operator/=(const kl::float2& obj) {
			x /= obj.x; y /= obj.y;
		}

		// Comparison
		ALL bool equals(const kl::float2& obj) const {
			return x == obj.x && y == obj.y;
		}
		ALL bool operator==(const kl::float2& obj) const {
			return equals(obj);
		}
		ALL bool operator!=(const kl::float2& obj) const {
			return !equals(obj);
		}

		// Returns a negated vector
		ALL kl::float2 negate() const {
			return operator*(-1);
		}

		// Returns an absolute vector
		ALL kl::float2 abso() const {
			return kl::float2(abs(x), abs(y));
		}

		// Returns the vectors length
		ALL float length() const {
			return sqrt(x * x + y * y);
		}

		// Retruns a normalized vector
		ALL kl::float2 normalize() const {
			return div(length());
		}

		// Returns the dot product
		ALL float dot(const float2& a) const {
			return x * a.x + y * a.y;
		}

		// Returns the angle between the given vector and self
		ALL float angle(const kl::float2& a, bool full) const {
			if (full) {
				return atan2(x * a.y - y * a.x, x * a.x + y * a.y) * 57.2957795131f;
			}
			return acos(normalize().dot(a.normalize())) * 57.2957795131f;
		}

		// Returns a rotated vector around the given point
		ALL kl::float2 rotate(float angle) const {
			const float sinA = sin(angle * 0.01745329251f);
			const float cosA = cos(angle * 0.01745329251f);
			return kl::float2(cosA * x - sinA * y, sinA * x + cosA * y);
		}
	};
}
