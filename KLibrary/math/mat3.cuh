#pragma once

#include "math/float3.cuh"


namespace kl {
	class mat3 {
	public:
		float data[9] = {};

		ALL mat3() {
			setIdentity();
		}

		// Getters
		ALL float& operator[](int i) {
			return data[i];
		}
		ALL const float& operator[](int i) const {
			return data[i];
		}

		// Addition
		ALL kl::mat3 add(const kl::mat3& obj) const {
			kl::mat3 temp;
			for (int i = 0; i < 9; i++) {
				temp[i] = data[i] + obj.data[i];
			}
			return temp;
		}
		ALL kl::mat3 operator+(const kl::mat3& obj) const {
			return add(obj);
		}
		ALL void operator+=(const kl::mat3& obj) {
			for (int i = 0; i < 9; i++) {
				data[i] += obj.data[i];
			}
		}

		// Subtraction
		ALL kl::mat3 sub(const kl::mat3& obj) const {
			kl::mat3 temp;
			for (int i = 0; i < 9; i++) {
				temp[i] = data[i] - obj.data[i];
			}
			return temp;
		}
		ALL kl::mat3 operator-(const kl::mat3& obj) const {
			return sub(obj);
		}
		ALL void operator-=(const kl::mat3& obj) {
			for (int i = 0; i < 9; i++) {
				data[i] -= obj.data[i];
			}
		}

		// Multiplication
		ALL kl::mat3 mul(float a) const {
			kl::mat3 temp;
			for (int i = 0; i < 9; i++) {
				temp[i] = data[i] * a;
			}
			return temp;
		}
		ALL kl::mat3 operator*(float a) const {
			return mul(a);
		}
		ALL void operator*=(float a) {
			for (int i = 0; i < 9; i++) {
				data[i] *= a;
			}
		}
		ALL kl::mat3 mul(const kl::mat3& obj) const {
			kl::mat3 temp;
			temp[0] = data[0] * obj.data[0] + data[1] * obj.data[3] + data[2] * obj.data[6];
			temp[1] = data[0] * obj.data[1] + data[1] * obj.data[4] + data[2] * obj.data[7];
			temp[2] = data[0] * obj.data[2] + data[1] * obj.data[5] + data[2] * obj.data[8];
			temp[3] = data[3] * obj.data[0] + data[4] * obj.data[3] + data[5] * obj.data[6];
			temp[4] = data[3] * obj.data[1] + data[4] * obj.data[4] + data[5] * obj.data[7];
			temp[5] = data[3] * obj.data[2] + data[4] * obj.data[5] + data[5] * obj.data[8];
			temp[6] = data[6] * obj.data[0] + data[7] * obj.data[3] + data[8] * obj.data[6];
			temp[7] = data[6] * obj.data[1] + data[7] * obj.data[4] + data[8] * obj.data[7];
			temp[8] = data[6] * obj.data[2] + data[7] * obj.data[5] + data[8] * obj.data[8];
			return temp;
		}
		ALL kl::mat3 operator*(const kl::mat3& obj) const {
			return mul(obj);
		}
		ALL void operator*=(const kl::mat3& obj) {
			*this = mul(obj);
		}
		ALL kl::float3 mul(const kl::float3& obj) const {
			kl::float3 temp;
			temp.x = data[0] * obj.x + data[1] * obj.y + data[2] * obj.z;
			temp.y = data[3] * obj.x + data[4] * obj.y + data[5] * obj.z;
			temp.z = data[6] * obj.x + data[7] * obj.y + data[8] * obj.z;
			return temp;
		}
		ALL kl::float3 operator*(const kl::float3& obj) const {
			return mul(obj);
		}

		// Division
		ALL kl::mat3 div(float a) const {
			return mul(1.0f / a);
		}
		ALL kl::mat3 operator/(float a) const {
			return div(a);
		}
		ALL void operator/=(float a) {
			operator*=(1.0f / a);
		}

		// Comparison
		ALL bool equals(const kl::mat3& obj) const {
			for (int i = 0; i < 9; i++) {
				if (data[i] != obj.data[i]) {
					return false;
				}
			}
			return true;
		}
		ALL bool operator==(const kl::mat3& obj) const {
			return equals(obj);
		}
		ALL bool operator!=(const kl::mat3& obj) const {
			return !equals(obj);
		}

		// Returns pointer to raw data
		ALL float* pointer() const {
			return (float*)data;
		}

		// Loads the identity matrix
		ALL void setIdentity() {
			data[0] = 1; data[1] = 0; data[2] = 0;
			data[3] = 0; data[4] = 1; data[5] = 0;
			data[6] = 0; data[7] = 0; data[8] = 1;
		}

		// Returns the inverse matrix
		ALL kl::mat3 inverse() const {
			kl::mat3 inv;
			inv[0] = data[4] * data[8] - data[7] * data[5];
			inv[1] = data[2] * data[7] - data[1] * data[8];
			inv[2] = data[1] * data[5] - data[2] * data[4];
			inv[3] = data[5] * data[6] - data[3] * data[8];
			inv[4] = data[0] * data[8] - data[2] * data[6];
			inv[5] = data[3] * data[2] - data[0] * data[5];
			inv[6] = data[3] * data[7] - data[6] * data[4];
			inv[7] = data[6] * data[1] - data[0] * data[7];
			inv[8] = data[0] * data[4] - data[3] * data[1];

			// Det calc
			const float det = data[0] * (data[4] * data[8] - data[7] * data[5]) -
				data[1] * (data[3] * data[8] - data[5] * data[6]) +
				data[2] * (data[3] * data[7] - data[4] * data[6]);

			// Return
			return inv.div(det);
		}

		// Returns a translation matrix
		ALL static kl::mat3 translate(const kl::float2& translation) {
			kl::mat3 temp;
			temp[2] = translation.x;
			temp[5] = translation.y;
			return temp;
		}

		// Returns a rotation matrix
		ALL static kl::mat3 rotate(float rotation) {
			// Computing trig
			const float zSin = sin(rotation * 0.01745329251f);
			const float zCos = cos(rotation * 0.01745329251f);

			// Generating the mat
			kl::mat3 temp;
			temp[0] = zCos;
			temp[1] = -zSin;
			temp[3] = zSin;
			temp[4] = zCos;
			return temp;
		}

		// Returns a scaling matrix
		ALL static kl::mat3 scale(const kl::float2& size) {
			kl::mat3 temp;
			temp[0] = size.x;
			temp[4] = size.y;
			return temp;
		}
	};
}
