#pragma once

#include "math/int2.cuh"


namespace kl {
	class int3 {
	public:
		union {
			struct {
				int x, y, z;
			};
			int data[3];
		};

		// Constr
		ALL int3() : x(0), y(0), z(0) {}
		ALL int3(int a) : x(a), y(a), z(a) {}
		ALL int3(int x, int y, int z) : x(x), y(y), z(z) {}
		ALL int3(const kl::int2& v, int z) : x(v.x), y(v.y), z(z) {}
		ALL int3(int x, const kl::int2& v) : x(x), y(v.x), z(v.y) {}

		// Getters
		ALL int& operator[](int i) {
			return data[i];
		}
		ALL const int& operator[](int i) const {
			return data[i];
		}

		// Addition
		ALL kl::int3 add(const kl::int3& obj) const {
			return kl::int3(x + obj.x, y + obj.y, z + obj.z);
		}
		ALL kl::int3 operator+(const kl::int3& obj) const {
			return add(obj);
		}
		ALL void operator+=(const kl::int3& obj) {
			x += obj.x; y += obj.y; z += obj.z;
		}

		// Subtraction
		ALL kl::int3 sub(const kl::int3& obj) const {
			return kl::int3(x - obj.x, y - obj.y, z - obj.z);
		}
		ALL kl::int3 operator-(const kl::int3& obj) const {
			return sub(obj);
		}
		ALL void operator-=(const kl::int3& obj) {
			x -= obj.x; y -= obj.y; z -= obj.z;
		}

		// Multiplication
		ALL kl::int3 mul(int a) const {
			return kl::int3(x * a, y * a, z * a);
		}
		ALL kl::int3 operator*(int a) const {
			return mul(a);
		}
		ALL void operator*=(int a) {
			x *= a; y *= a; z *= a;
		}
		ALL kl::int3 mul(const kl::int3& obj) const {
			return kl::int3(x * obj.x, y * obj.y, z * obj.z);
		}
		ALL kl::int3 operator*(const kl::int3& obj) const {
			return mul(obj);
		}
		ALL void operator*=(const kl::int3& obj) {
			x *= obj.x; y *= obj.y; z *= obj.z;
		}

		// Division
		ALL kl::int3 div(int a) const {
			return kl::int3(x / a, y / a, z / a);
		}
		ALL kl::int3 operator/(int a) const {
			return div(a);
		}
		ALL void operator/=(int a) {
			x /= a; y /= a; z /= a;
		}
		ALL kl::int3 div(const kl::int3& obj) const {
			return kl::int3(x / obj.x, y / obj.y, z / obj.z);
		}
		ALL kl::int3 operator/(const kl::int3& obj) const {
			return div(obj);
		}
		ALL void operator/=(const kl::int3& obj) {
			x /= obj.x; y /= obj.y; z /= obj.z;
		}

		// Comparison
		ALL bool equals(const kl::int3& obj) const {
			return x == obj.x && y == obj.y && z == obj.z;
		}
		ALL bool operator==(const kl::int3& obj) const {
			return equals(obj);
		}
		ALL bool operator!=(const kl::int3& obj) const {
			return !equals(obj);
		}

		// Returns a negated vec
		ALL kl::int3 negate() const {
			return mul(-1);
		}

		// Returns an absolute vec
		ALL kl::int3 abso() const {
			return kl::int3(abs(x), abs(y), abs(z));
		}
	};
}
