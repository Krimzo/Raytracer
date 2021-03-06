#pragma once

#include "math/math.h"
#include "render/plane.h"
#include "render/sphere.h"
#include "render/triangle.h"
#include "render/camera.h"


namespace kl {
	struct ray {
		kl::float3 origin;
		kl::float3 direction;

		ALL ray() {}
		ALL ray(const kl::float3& origin, const kl::float3& direction) : origin(origin), direction(direction) {}
		ALL ray(const kl::float3& origin, const kl::mat4& invCamMat, const kl::float2& ndc) : origin(origin) {
			const kl::float4 pixelDir = invCamMat * kl::float4(ndc, 1.0f, 1.0f);
			direction = (pixelDir / pixelDir.w).xyz.normalize();
		}
		ALL ray(const kl::camera& cam, const kl::float2& ndc) : ray(cam.position, cam.matrix().inverse(), ndc) {}

		ALL bool intersect(const kl::plane& plane, kl::float3* outInter = nullptr) const {
			const float dnDot = direction.dot(plane.normal);
			if (dnDot != 0.0f) {
				if (outInter) {
					*outInter = origin - direction * ((origin - plane.point).dot(plane.normal) / dnDot);
				}
				return true;
			}
			return false;
		}

		ALL bool intersect(const kl::triangle& triangle, kl::float3* outInter = nullptr) const {
			const kl::float3 edge1 = triangle.b.world - triangle.a.world;
			const kl::float3 edge2 = triangle.c.world - triangle.a.world;
			const kl::float3 h = direction.cross(edge2);
			const kl::float3 s = origin - triangle.a.world;
			const float f = 1.0f / edge1.dot(h);
			const float u = s.dot(h) * f;
			if (u < 0.0f || u > 1.0f) {
				return false;
			}

			const kl::float3 q = s.cross(edge1);
			const float v = direction.dot(q) * f;
			if (v < 0.0f || (u + v) > 1.0f) {
				return false;
			}

			const float t = edge2.dot(q) * f;
			if (t > 0.0f) {
				if (outInter) {
					*outInter = origin + direction * t;
				}
				return true;
			}
			return false;
		}

		ALL bool intersect(const kl::sphere& sphere, kl::float3& outInter, float& outDis) const {
			const kl::float3 centerRay = sphere.center - origin;
			const float cdDot = centerRay.dot(direction);
			if (cdDot < 0.0f) {
				return false;
			}

			const float ccDot = centerRay.dot(centerRay) - cdDot * cdDot;
			const float rr = sphere.radius * sphere.radius;
			if (ccDot > rr) {
				return false;
			}

			const float thc = sqrt(rr - ccDot);
			const float dis0 = cdDot - thc;
			const float dis1 = cdDot + thc;
			outInter = origin + direction * ((dis0 < 0.0f) ? dis1 : dis0);
			outDis = (dis0 < 0.0f) ? dis1 : dis0;
			return true;
		}
		ALL bool intersect(const kl::sphere& sphere) const {
			const float rayDis = (sphere.center - origin).dot(direction);
			const kl::float3 rayPoint = origin + direction * rayDis;
			const float sphRayDis = (sphere.center - rayPoint).length();
			if (sphRayDis > sphere.radius) {
				return false;
			}
			return true;
		}
	};

	// std::cout
	inline std::ostream& operator<<(std::ostream& os, const kl::ray& obj) {
		os << "{" << obj.origin << ", " << obj.direction << "}";
		return os;
	}
}
