#include "Kernels/Kernels.cuh"


GPU kl::float2 GetNDC(size_t ind, const kl::int2& screenSize) {
	const kl::int2 screen(ind % screenSize.x, ind / screenSize.x);
	return { kl::float2(screen.x, screenSize.y - screen.y) / kl::float2(screenSize.x, screenSize.y) * 2.0f - 1.0f };
}

GPU bool RayCanHit(const kl::ray& ray, const Raytracer::Entity& entity) {
	return ray.intersect({ entity.position, entity.computed.far.x });
}

GPU bool TraceShadow(const kl::ray& ray, Raytracer::Entity* entities, size_t entityCount) {
	kl::float3 ignore;
	for (size_t e = 0; e < entityCount; e++) {
		if (RayCanHit(ray, entities[e])) {
			for (size_t t = 0; t < entities[e].computed.size; t++) {
				if (ray.intersect(entities[e].computed.buffer[t], ignore)) {
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

GPU kl::color TraceRay(const kl::ray& ray, Raytracer::Entity* entities, size_t entityCount, size_t depth, const kl::float3& sunDir) {
	float interDepth = INFINITY;
	Raytracer::Entity* entity = nullptr;
	kl::triangle* intersTri = nullptr;
	kl::float3 intersPoint = {};
	for (size_t e = 0; e < entityCount; e++) {
		if (RayCanHit(ray, entities[e])) {
			for (size_t t = 0; t < entities[e].computed.size; t++) {
				kl::float3 tempPoint;
				if (ray.intersect(entities[e].computed.buffer[t], tempPoint)) {
					const float tempDepth = (tempPoint - ray.origin).length();
					if (tempDepth < interDepth) {
						interDepth = tempDepth;
						entity = entities + e;
						intersTri = entity->computed.buffer + t;
						intersPoint = tempPoint;
					}
				}
			}
		}
	}

	/* constants */
	const kl::color skyTopColor = { 115, 190, 225 };
	const kl::color skyBottomColor = { 200, 200, 200 };
	const kl::float3 ambient = 0.075f;
	const kl::color sunDiffColor = { 255, 255, 255 };
	const kl::color sunSkyColor = { 250, 230, 195 };
	const kl::float2 sunRadiuses = { 0.75f, 1.55f };

	kl::color rayColor;
	if (entity) {
		const kl::vertex intersVert = intersTri->interpolate(intersPoint);
		const kl::float3 offsetWorld = intersVert.world + (intersVert.normal * 1e-5f);

		const kl::float3 diffuse = kl::float3(sunDiffColor) * max(sunDir.negate().dot(intersVert.normal), 0.0f);
		const bool inShadow = TraceShadow({ offsetWorld, sunDir.negate() }, entities, entityCount);

		const kl::float3 fullLight = ambient + (diffuse * !inShadow);
		rayColor = GetColor(entity->texture->buffer, entity->texture->size, intersVert.texture);
		rayColor.r = byte(min(rayColor.r * fullLight.r, 255.0f));
		rayColor.g = byte(min(rayColor.g * fullLight.g, 255.0f));
		rayColor.b = byte(min(rayColor.b * fullLight.b, 255.0f));

		if (depth > 1 && entity->roughness < 1.0f) {
			const kl::color reflectColor = TraceRay({ offsetWorld, ray.direction.reflect(intersVert.normal) }, entities, entityCount, depth - 1, sunDir);
			rayColor = reflectColor.mix(rayColor, entity->roughness);
		}
	}
	else {
		const float skyMixValue = (-ray.direction.dot({ 0.0f, 1.0f, 0.0f }) + 1.0f) * 0.5f;
		rayColor = skyTopColor.mix(skyBottomColor, skyMixValue);

		const float sunAngle = ray.direction.angle(sunDir.negate());
		const float sunMixValue = (sunAngle - sunRadiuses.x) / (sunRadiuses.y - sunRadiuses.x);
		rayColor = sunSkyColor.mix(rayColor, sunMixValue);
	}
	return rayColor;
}

EXEC void Kernels::Raytrace(size_t pixelCount, kl::color* pixelBuffer, kl::int2 screenSize, kl::float3 camPos, kl::mat4 invCam, Raytracer::Entity* entities, size_t entityCount, kl::float3 sunDir) {
	const size_t i = kl::cuda::GetX();
	if (i < pixelCount) {
		const kl::float2 ndc = GetNDC(i, screenSize);
		const kl::ray pixelRay = { camPos, invCam, ndc };
		pixelBuffer[i] = TraceRay(pixelRay, entities, entityCount, 3, sunDir);
	}
}
