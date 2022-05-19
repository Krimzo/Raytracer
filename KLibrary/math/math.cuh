#pragma once

#include <windows.h>
#include "math/float2.cuh"
#include "math/float3.cuh"
#include "math/float4.cuh"
#include "utility/convert.cuh"

#include "cuda/kcuda.cuh"


namespace kl {
	namespace math {
		inline const float pi = 3.14159265358979f;

		inline ALL float lineX(const kl::float2& a, const kl::float2& b, float y) {
			return ((y - a.y) * (b.x - a.x)) / (b.y - a.y) + a.x;
		}

		inline ALL float lineY(const kl::float2& a, const kl::float2& b, float x) {
			return ((b.y - a.y) * (x - a.x)) / (b.x - a.x) + a.y;
		}

		inline ALL kl::float4 eulToQuat(const kl::float3& eul) {
			const float cr = cos(kl::convert::toRadians(eul.x) * 0.5f);
			const float sr = sin(kl::convert::toRadians(eul.x) * 0.5f);
			const float cp = cos(kl::convert::toRadians(eul.y) * 0.5f);
			const float sp = sin(kl::convert::toRadians(eul.y) * 0.5f);
			const float cy = cos(kl::convert::toRadians(eul.z) * 0.5f);
			const float sy = sin(kl::convert::toRadians(eul.z) * 0.5f);

			kl::float4 quat;
			quat.x = sr * cp * cy - cr * sp * sy;
			quat.y = cr * sp * cy + sr * cp * sy;
			quat.z = cr * cp * sy - sr * sp * cy;
			quat.w = cr * cp * cy + sr * sp * sy;
			return quat;
		}
		inline ALL kl::float3 quatToEul(const kl::float4& quat) {
			const float sinp = 2.0f * (quat.w * quat.y - quat.z * quat.x);
			const float sinrCosp = 2.0f * (quat.w * quat.x + quat.y * quat.z);
			const float cosrCosp = 1.0f - 2.0f * (quat.x * quat.x + quat.y * quat.y);
			const float sinyCosp = 2.0f * (quat.w * quat.z + quat.x * quat.y);
			const float cosyCosp = 1.0f - 2.0f * (quat.y * quat.y + quat.z * quat.z);

			kl::float3 eul;
			eul.x = kl::convert::toDegrees(std::atan2(sinrCosp, cosrCosp));
			eul.y = kl::convert::toDegrees((std::abs(sinp) >= 1.0f) ? std::copysign(1.57079632679f, sinp) : std::asin(sinp));
			eul.z = kl::convert::toDegrees(std::atan2(sinyCosp, cosyCosp));
			return eul;
		}
	};
}
