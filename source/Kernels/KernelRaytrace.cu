#include "Kernels/Kernels.cuh"


GPU kl::float2 GetNDC(size_t ind, const kl::int2& screenSize) {
	const kl::int2 screen(ind % screenSize.x, ind / screenSize.x);
	return { kl::float2(screen.x, screenSize.y - screen.y) / kl::float2(screenSize.x, screenSize.y) * 2.0f - 1.0f };
}

GPU bool RayCanHit(const kl::ray& ray, const Raytracer::Entity& entity) {
	return ray.intersect({ entity.position, entity.mesh.computed.far });
}

GPU bool TraceShadow(const kl::ray& ray, Raytracer::Entity* entities, size_t entityCount) {
	for (size_t e = 0; e < entityCount; e++) {
		if (RayCanHit(ray, entities[e])) {
			for (size_t t = 0; t < entities[e].mesh.computed.size; t++) {
				if (ray.intersect(entities[e].mesh.computed.buffer[t], nullptr)) {
					return true;
				}
			}
		}
	}
	return false;
}

GPU kl::color GetColor(kl::color* textureBuffer, const kl::int2& textureSize, const kl::float2& uv) {
	const kl::int2 texPos = {
		min(max(int((textureSize.x - 1) * uv.x), 0), textureSize.x - 1),
		min(max(int((textureSize.y - 1) * uv.y), 0), textureSize.y - 1)
	};
	return textureBuffer[texPos.y * textureSize.x + texPos.x];
}

GPU kl::color TraceRay(const kl::ray& ray, Raytracer::Entity* entities, size_t entityCount) {
	kl::color rayColor = { 50, 50, 50 };

	float interDepth = INFINITY;
	kl::vertex interVert;
	kl::color* textureBuffer = nullptr;
	kl::int2 textureSize;

	for (size_t e = 0; e < entityCount; e++) {
		if (RayCanHit(ray, entities[e])) {
			for (size_t t = 0; t < entities[e].mesh.computed.size; t++) {
				kl::vertex tempVert;
				if (ray.intersect(entities[e].mesh.computed.buffer[t], &tempVert)) {
					const float tempDepth = (tempVert.world - ray.origin).length();
					if (tempDepth < interDepth) {
						interDepth = tempDepth;
						interVert = tempVert;
						textureBuffer = entities[e].texture.buffer;
						textureSize = entities[e].texture.size;
					}
				}
			}
		}
	}

	if (interDepth < INFINITY) {
		const kl::float3 sunDir = kl::float3(1.0f, -0.25f, 0.0f).normalize();

		rayColor = GetColor(textureBuffer, textureSize, interVert.texture);

		const kl::float3 ambient = 0.075f;
		const kl::float3 diffuse = max(sunDir.negate().dot(interVert.normal), 0.0f);
		const bool inShadow = TraceShadow({ interVert.world + (interVert.normal * 1e-5f), sunDir.negate() }, entities, entityCount);

		const kl::float3 fullLight = ambient + (diffuse * !inShadow);

		rayColor.r = byte(min(rayColor.r * fullLight.r, 255.0f));
		rayColor.g = byte(min(rayColor.g * fullLight.g, 255.0f));
		rayColor.b = byte(min(rayColor.b * fullLight.b, 255.0f));
	}

	return rayColor;
}

EXEC void Kernels::Raytrace(size_t pixelCount, kl::color* pixelBuffer, kl::int2 screenSize, kl::camera camera, Raytracer::Entity* entities, size_t entityCount) {
	const size_t p = kl::cuda::Index();
	if (p < pixelCount) {
		const kl::float2 ndc = GetNDC(p, screenSize);
		const kl::ray pixelRay = { camera, ndc };
		pixelBuffer[p] = TraceRay(pixelRay, entities, entityCount);
	}
}
