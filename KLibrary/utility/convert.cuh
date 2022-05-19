#pragma once
#define _CRT_SECURE_NO_WARNINGS

#include <string>

#include "libspec/using.cuh"
#include "math/float2.cuh"
#include "math/float3.cuh"
#include "math/float4.cuh"
#include "graphics/color.cuh"

#include "cuda/kcuda.cuh"


namespace kl {
	namespace convert {
		inline ALL float toRadians(float degrees) {
			return degrees * 0.01745329251f;
		}
		inline ALL kl::float2 toRadians(const kl::float2& degrees) {
			return kl::float2(toRadians(degrees.x), toRadians(degrees.y));
		}
		inline ALL kl::float3 toRadians(const kl::float3& degrees) {
			return kl::float3(toRadians(degrees.x), toRadians(degrees.y), toRadians(degrees.z));
		}

		inline ALL float toDegrees(float radians) {
			return radians * 57.2957795131f;
		}
		inline ALL kl::float2 toDegrees(const kl::float2& radians) {
			return kl::float2(toDegrees(radians.x), toDegrees(radians.y));
		}
		inline ALL kl::float3 toDegrees(const kl::float3& radians) {
			return kl::float3(toDegrees(radians.x), toDegrees(radians.y), toDegrees(radians.z));
		}

		inline ALL float toFloCol(byte val) {
			return val / 255.0f;
		}

		inline ALL kl::color toColor(const kl::float4& colf) {
			return kl::color(byte(min(max(colf.x, 0.0f), 1.0f) * 255), byte(min(max(colf.y, 0.0f), 1.0f) * 255), byte(min(max(colf.z, 0.0f), 1.0f) * 255), byte(min(max(colf.w, 0.0f), 1.0f) * 255));
		}

		inline std::wstring toWString(const String& data) {
			std::wstring toReturn;
			toReturn.resize(data.size());
			mbstowcs(&toReturn[0], &data[0], data.size());
			return toReturn;
		}
	};
}
