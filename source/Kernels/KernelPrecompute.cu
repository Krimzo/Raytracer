#include "Kernels/Kernels.cuh"


RUN void Kernels::Precompute(uint64 triangleCount, Raytracer::Entity* entities, uint64 entityCount) {
	const uint64 i = kl::cuda::getX();
	if (i < triangleCount) {
		uint64 leftTrCount = 0;
		uint64 rightTrCount = 0;
		for (uint64 e = 0; e < entityCount; e++) {
			rightTrCount += entities[e].mesh->size;
			if (i >= leftTrCount && i < rightTrCount) {
				const uint64 t = i - leftTrCount;
				const kl::mat4 entMat = entities[e].matrix();
				entities[e].computed.buffer[t].a.world = (entMat * kl::float4(entities[e].mesh->buffer[t].a.world, 1.0f)).xyz;
				entities[e].computed.buffer[t].b.world = (entMat * kl::float4(entities[e].mesh->buffer[t].b.world, 1.0f)).xyz;
				entities[e].computed.buffer[t].c.world = (entMat * kl::float4(entities[e].mesh->buffer[t].c.world, 1.0f)).xyz;
				entities[e].computed.buffer[t].a.texture = entities[e].mesh->buffer[t].a.texture;
				entities[e].computed.buffer[t].b.texture = entities[e].mesh->buffer[t].b.texture;
				entities[e].computed.buffer[t].c.texture = entities[e].mesh->buffer[t].c.texture;
				entities[e].computed.buffer[t].a.normal = (entMat * kl::float4(entities[e].mesh->buffer[t].a.normal, 0.0f)).xyz.norm();
				entities[e].computed.buffer[t].b.normal = (entMat * kl::float4(entities[e].mesh->buffer[t].b.normal, 0.0f)).xyz.norm();
				entities[e].computed.buffer[t].c.normal = (entMat * kl::float4(entities[e].mesh->buffer[t].c.normal, 0.0f)).xyz.norm();
				return;
			}
			leftTrCount = rightTrCount;
		}
	}
}
