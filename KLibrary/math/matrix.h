#pragma once

#include <array>
#include <functional>
#include <iomanip>

#include "math/vector4.h"


namespace kl {
	template<typename T, uint64 N> struct buffer {
		T data[N] = {};

		ALL buffer() {}

		ALL T& operator[](uint64 i) {
			return data[i];
		}
		ALL const T& operator[](uint64 i) const {
			return data[i];
		}
	};
}

namespace kl {
	template<typename T, uint64 W, uint64 H> struct matrix : public kl::buffer<T, W* H> {

		ALL matrix() : kl::buffer<T, W* H>() {
			for (uint64 i = 0; i < (W * H); i += (W + 1)) {
				(*this)[i] = 1;
			}
		}

		// Addition
		ALL void add(const kl::matrix<T, W, H>& obj, kl::matrix<T, W, H>& out) const {
			for (uint64 i = 0; i < (W * H); i++) {
				out[i] = (*this)[i] + obj[i];
			}
		}
		ALL kl::matrix<T, W, H> operator+(const kl::matrix<T, W, H>& obj) const {
			kl::matrix<T, W, H> temp;
			add(obj, temp);
			return temp;
		}
		ALL void operator+=(const kl::matrix<T, W, H>& obj) {
			add(obj, *this);
		}

		// Subtraction
		ALL void subtract(const kl::matrix<T, W, H>& obj, kl::matrix<T, W, H>& out) const {
			for (uint64 i = 0; i < (W * H); i++) {
				out[i] = (*this)[i] - obj[i];
			}
		}
		ALL kl::matrix<T, W, H> operator-(const kl::matrix<T, W, H>& obj) const {
			kl::matrix<T, W, H> temp;
			subtract(obj, temp);
			return temp;
		}
		ALL void operator-=(const kl::matrix<T, W, H>& obj) {
			subtract(obj, *this);
		}

		// Multiplication
		ALL void multiply(const T& val, kl::matrix<T, W, H>& out) const {
			for (uint64 i = 0; i < (W * H); i++) {
				out[i] = (*this)[i] * val;
			}
		}
		ALL kl::matrix<T, W, H> operator*(const T& val) const {
			kl::matrix<T, W, H> temp;
			multiply(val, temp);
			return temp;
		}
		ALL void operator*=(const T& val) {
			multiply(val, *this);
		}
		template<uint64 S> ALL void multiply(const kl::matrix<T, S, W>& obj, kl::matrix<T, S, H>& out) const {
			for (uint64 y = 0; y < H; y++) {
				for (uint64 x = 0; x < S; x++) {
					out[y * S + x] = {};
					for (uint64 i = 0; i < W; i++) {
						out[y * S + x] += (*this)[y * W + i] * obj[i * S + x];
					}
				}
			}
		}
		template<uint64 S> ALL kl::matrix<T, S, H> operator*(const kl::matrix<T, S, W>& obj) const {
			kl::matrix<T, S, H> temp;
			multiply(obj, temp);
			return temp;
		}
		template<uint64 S> ALL void operator*=(const kl::matrix<T, S, W>& obj) {
			*this = (*this) * obj;
		}
		ALL bool multiply(const kl::vector2<T>& obj, kl::vector2<T>& out) const {
			if constexpr (W == 2 && H == 2) {
				for (uint64 y = 0; y < 2; y++) {
					T sum = {};
					for (uint64 i = 0; i < 2; i++) {
						sum += (*this)[y * 2 + i] * obj[i];
					}
					out[y] = sum;
				}
				return true;
			}
			return false;
		}
		ALL kl::vector2<T> operator*(const kl::vector2<T>& obj) const {
			kl::vector2<T> temp;
			multiply(obj, temp);
			return temp;
		}
		ALL bool multiply(const kl::vector3<T>& obj, kl::vector3<T>& out) const {
			if constexpr (W == 3 && H == 3) {
				for (uint64 y = 0; y < 3; y++) {
					T sum = {};
					for (uint64 i = 0; i < 3; i++) {
						sum += (*this)[y * 3 + i] * obj[i];
					}
					out[y] = sum;
				}
				return true;
			}
			return false;
		}
		ALL kl::vector3<T> operator*(const kl::vector3<T>& obj) const {
			kl::vector3<T> temp;
			multiply(obj, temp);
			return temp;
		}
		ALL bool multiply(const kl::vector4<T>& obj, kl::vector4<T>& out) const {
			if constexpr (W == 4 && H == 4) {
				for (uint64 y = 0; y < 4; y++) {
					T sum = {};
					for (uint64 i = 0; i < 4; i++) {
						sum += (*this)[y * 4 + i] * obj[i];
					}
					out[y] = sum;
				}
				return true;
			}
			return false;
		}
		ALL kl::vector4<T> operator*(const kl::vector4<T>& obj) const {
			kl::vector4<T> temp;
			multiply(obj, temp);
			return temp;
		}

		// Comparison
		ALL bool equals(const kl::matrix<T, W, H>& obj) const {
			for (uint64 i = 0; i < (W * H); i++) {
				if ((*this)[i] != obj[i]) {
					return false;
				}
			}
			return true;
		}
		ALL bool operator==(const kl::matrix<T, W, H>& obj) const {
			return equals(obj);
		}
		ALL bool operator!=(const kl::matrix<T, W, H>& obj) const {
			return !equals(obj);
		}

		// Sign change
		ALL void absolute(kl::matrix<T, W, H>& out) const {
			for (uint64 i = 0; i < (W * H); i++) {
				out[i] = std::abs((*this)[i]);
			}
		}
		ALL kl::matrix<T, W, H> absolute() const {
			kl::matrix<T, W, H> temp;
			absolute(temp);
			return temp;
		}
		ALL void negate(kl::matrix<T, W, H>& out) const {
			multiply(-1.0f, out);
		}
		ALL kl::matrix<T, W, H> negate() const {
			kl::matrix<T, W, H> temp;
			negate(temp);
			return temp;
		}

		// Transpose
		ALL void transpose(kl::matrix<T, H, W>& out) const {
			for (uint64 y = 0; y < H; y++) {
				for (uint64 x = 0; x < W; x++) {
					out[x * H + y] = (*this)[y * W + x];
				}
			}
		}
		ALL kl::matrix<T, H, W> transpose() const {
			kl::matrix<T, H, W> temp;
			transpose(temp);
			return temp;
		}

		// Cofactor
		ALL bool cofactor(uint64 ind, kl::matrix<T, W - 1, H - 1>& out) const {
			if constexpr (W == H) {
				if (ind < (W * H)) {
					uint64 counter = 0;
					const uint64 xInd = ind % W;
					const uint64 yInd = ind / W;
					for (uint64 y = 0; y < H; y++) {
						for (uint64 x = 0; x < W; x++) {
							if (x != xInd && y != yInd) {
								out[counter++] = (*this)[y * W + x];
							}
						}
					}
					return true;
				}
			}
			return false;
		}
		ALL kl::matrix<T, W - 1, H - 1> cofactor(uint64 ind) const {
			kl::matrix<T, W - 1, H - 1> temp;
			cofactor(ind, temp);
			return temp;
		}
		ALL bool cofactor(kl::matrix<T, W, H>& out) const {
			if constexpr (W == H) {
				for (uint64 y = 0; y < H; y++) {
					for (uint64 x = 0; x < W; x++) {
						out[y * W + x] = (((y + x + 2) % 2) ? -1 : 1) * cofactor(y * W + x).determinant();
					}
				}
				return true;
			}
			return false;
		}
		ALL kl::matrix<T, W, H> cofactor() const {
			kl::matrix<T, W, H> temp;
			cofactor(temp);
			return temp;
		}

		// Determinant
		ALL T determinant() const {
			if constexpr (W == H) {
				if constexpr (W == 2) {
					return (*this)[0] * (*this)[3] - (*this)[1] * (*this)[2];
				}
				if constexpr (W > 2) {
					T val = {};
					int multi = -1;
					for (uint64 i = 0; i < W; i++) {
						val += (multi *= -1) * (*this)[i] * cofactor(i).determinant();
					}
					return val;
				}
			}
			return T(0);
		}

		// Adjoint
		ALL bool adjoint(kl::matrix<T, W, H>& out) const {
			if constexpr (W == H) {
				out = cofactor().transpose();
				return true;
			}
			return false;
		}
		ALL kl::matrix<T, W, H> adjoint() const {
			kl::matrix<T, W, H> temp;
			adjoint(temp);
			return temp;
		}

		// Inverse
		ALL bool inverse(kl::matrix<T, W, H>& out) const {
			if constexpr (W == H) {
				const T det = determinant();
				if (det) {
					out = adjoint() * T(1.0 / det);
					return true;
				}
			}
			return false;
		}
		ALL kl::matrix<T, W, H> inverse() const {
			kl::matrix<T, W, H> temp;
			inverse(temp);
			return temp;
		}
	};

	// std::cout
	template<typename T, uint64 W, uint64 H> inline std::ostream& operator<<(std::ostream& stream, const kl::matrix<T, W, H>& mat) {
		stream << std::fixed << std::setprecision(2);
		for (uint64 y = 0; y < H; y++) {
			stream << ((y == 0) ? char(218) : (y == (H - 1) ? char(192) : char(179)));
			for (uint64 x = 0; x < (W - 1); x++) {
				stream << std::setw(6) << mat[y * W + x] << " ";
			}
			stream << std::setw(6) << mat[y * W + (W - 1)] << ((y == 0) ? char(191) : (y == (H - 1) ? char(217) : char(179))) << '\n';
		}
		return stream;
	}
}
