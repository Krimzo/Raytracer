#pragma once

#include "cuda/kcuda.cuh"


namespace kl {
	class int2 {
	public:
		union {
			struct {
				int x, y;
			};
			int data[2];
		};

		// Constr
		ALL int2() : x(0), y(0) {}
		ALL int2(int a) : x(a), y(a) {}
		ALL int2(int x, int y) : x(x), y(y) {}

		// Getters
		ALL int& operator[](int i) {
			return data[i];
		}
		ALL const int& operator[](int i) const {
			return data[i];
		}

		// Addition
		ALL kl::int2 add(const kl::int2& obj) const {
			return kl::int2(x + obj.x, y + obj.y);
		}
		ALL kl::int2 operator+(const kl::int2& obj) const {
			return add(obj);
		}
		ALL void operator+=(const kl::int2& obj) {
			x += obj.x; y += obj.y;
		}

		// Subtraction
		ALL kl::int2 sub(const kl::int2& obj) const {
			return kl::int2(x - obj.x, y - obj.y);
		}
		ALL kl::int2 operator-(const kl::int2& obj) const {
			return sub(obj);
		}
		ALL void operator-=(const kl::int2& obj) {
			x -= obj.x; y -= obj.y;
		}

		// Multiplication
		ALL kl::int2 mul(int a) const {
			return kl::int2(x * a, y * a);
		}
		ALL kl::int2 operator*(int a) const {
			return mul(a);
		}
		ALL void operator*=(int a) {
			x *= a; y *= a;
		}
		ALL kl::int2 mul(const kl::int2& obj) const {
			return kl::int2(x * obj.x, y * obj.y);
		}
		ALL kl::int2 operator*(const kl::int2& obj) const {
			return mul(obj);
		}
		ALL void operator*=(const kl::int2& obj) {
			x *= obj.x; y *= obj.y;
		}

		// Division
		ALL kl::int2 div(int a) const {
			return kl::int2(x / a, y / a);
		}
		ALL kl::int2 operator/(int a) const {
			return div(a);
		}
		ALL void operator/=(int a) {
			x /= a; y /= a;
		}
		ALL kl::int2 div(const kl::int2& obj) const {
			return kl::int2(x / obj.x, y / obj.y);
		}
		ALL kl::int2 operator/(const kl::int2& obj) const {
			return div(obj);
		}
		ALL void operator/=(const kl::int2& obj) {
			x /= obj.x; y /= obj.y;
		}

		// Comparison
		ALL bool equals(const kl::int2& obj) const {
			return x == obj.x && y == obj.y;
		}
		ALL bool operator==(const kl::int2& obj) const {
			return equals(obj);
		}
		ALL bool operator!=(const kl::int2& obj) const {
			return !equals(obj);
		}

		// Returns a negated vec
		ALL kl::int2 negate() const {
			return mul(-1);
		}

		// Returns an absolute vec
		ALL kl::int2 abso() const {
			return kl::int2(abs(x), abs(y));
		}
	};
}
