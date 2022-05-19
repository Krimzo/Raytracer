#pragma once

#include "math/int3.cuh"


namespace kl {
	class int4 {
	public:
		union {
			struct {
				int x, y, z, w;
			};
			int data[4];
		};

		// Constr
		ALL int4() : x(0), y(0), z(0), w(0) {}
		ALL int4(int a) : x(a), y(a), z(a), w(a) {}
		ALL int4(int x, int y, int z, int w) : x(x), y(y), z(z), w(w) {}
		ALL int4(const kl::int2& v, int z, int w) : x(v.x), y(v.y), z(z), w(w) {}
		ALL int4(int x, const kl::int2& v, int w) : x(x), y(v.x), z(v.y), w(w) {}
		ALL int4(int x, int y, const kl::int2& v) : x(x), y(y), z(v.x), w(v.y) {}
		ALL int4(const kl::int2& v1, const kl::int2& v2) : x(v1.x), y(v1.y), z(v2.x), w(v2.y) {}
		ALL int4(const kl::int3& v, int w) : x(v.x), y(v.y), z(v.z), w(w) {}
		ALL int4(int x, const kl::int3& v) : x(x), y(v.x), z(v.y), w(v.z) {}

		// Getters
		ALL int& operator[](int i) {
			return data[i];
		}
		ALL const int& operator[](int i) const {
			return data[i];
		}

		// Addition
		ALL kl::int4 add(const kl::int4& obj) const {
			return kl::int4(x + obj.x, y + obj.y, z + obj.z, w + obj.w);
		}
		ALL kl::int4 operator+(const kl::int4& obj) const {
			return add(obj);
		}
		ALL void operator+=(const kl::int4& obj) {
			x += obj.x; y += obj.y; z += obj.z; w += obj.w;
		}

		// Subtraction
		ALL kl::int4 sub(const kl::int4& obj) const {
			return kl::int4(x - obj.x, y - obj.y, z - obj.z, w - obj.w);
		}
		ALL kl::int4 operator-(const kl::int4& obj) const {
			return sub(obj);
		}
		ALL void operator-=(const kl::int4& obj) {
			x -= obj.x; y -= obj.y; z -= obj.z; w -= obj.w;
		}

		// Multiplication
		ALL kl::int4 mul(int a) const {
			return kl::int4(x * a, y * a, z * a, w * a);
		}
		ALL kl::int4 operator*(int a) const {
			return mul(a);
		}
		ALL void operator*=(int a) {
			x *= a; y *= a; z *= a; w *= a;
		}
		ALL kl::int4 mul(const kl::int4& obj) const {
			return kl::int4(x * obj.x, y * obj.y, z * obj.z, w * obj.w);
		}
		ALL kl::int4 operator*(const kl::int4& obj) const {
			return mul(obj);
		}
		ALL void operator*=(const kl::int4& obj) {
			x *= obj.x; y *= obj.y; z *= obj.z; w *= obj.w;
		}

		// Division
		ALL kl::int4 div(int a) const {
			return kl::int4(x / a, y / a, z / a, w / a);
		}
		ALL kl::int4 operator/(int a) const {
			return div(a);
		}
		ALL void operator/=(int a) {
			x /= a; y /= a; z /= a; w /= a;
		}
		ALL kl::int4 div(const kl::int4& obj) const {
			return kl::int4(x / obj.x, y / obj.y, z / obj.z, w / obj.w);
		}
		ALL kl::int4 operator/(const kl::int4& obj) const {
			return div(obj);
		}
		ALL void operator/=(const kl::int4& obj) {
			x /= obj.x; y /= obj.y; z /= obj.z; w /= obj.w;
		}

		// Comparison
		ALL bool equals(const kl::int4& obj) const {
			return x == obj.x && y == obj.y && z == obj.z && w == obj.w;
		}
		ALL bool operator==(const kl::int4& obj) const {
			return equals(obj);
		}
		ALL bool operator!=(const kl::int4& obj) const {
			return !equals(obj);
		}

		// Returns a negated vec
		ALL kl::int4 negate() const {
			return mul(-1);
		}

		// Returns an absolute vec
		ALL kl::int4 abso() const {
			return kl::int4(abs(x), abs(y), abs(z), abs(w));
		}
	};
}
