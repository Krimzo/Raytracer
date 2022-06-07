#include "Kernels/Kernels.cuh"


EXEC void Kernels::Precompute(size_t triangleCount, Raytracer::Entity* entities, size_t entityCount) {
	const size_t i = kl::cuda::GetX();
	if (i < triangleCount) {
		size_t leftTrCount = 0;
		size_t rightTrCount = 0;
		for (size_t e = 0; e < entityCount; e++) {
			rightTrCount += entities[e].mesh->size;
			if (i >= leftTrCount && i < rightTrCount) {
				const size_t t = i - leftTrCount;
				const kl::mat4 entMat = entities[e].matrix();
				entities[e].computed.buffer[t].a.world = (entMat * kl::float4(entities[e].mesh->buffer[t].a.world, 1.0f)).xyz();
				entities[e].computed.buffer[t].b.world = (entMat * kl::float4(entities[e].mesh->buffer[t].b.world, 1.0f)).xyz();
				entities[e].computed.buffer[t].c.world = (entMat * kl::float4(entities[e].mesh->buffer[t].c.world, 1.0f)).xyz();
				entities[e].computed.buffer[t].a.texture = entities[e].mesh->buffer[t].a.texture;
				entities[e].computed.buffer[t].b.texture = entities[e].mesh->buffer[t].b.texture;
				entities[e].computed.buffer[t].c.texture = entities[e].mesh->buffer[t].c.texture;
				entities[e].computed.buffer[t].a.normal = (entMat * kl::float4(entities[e].mesh->buffer[t].a.normal, 0.0f)).xyz().normalize();
				entities[e].computed.buffer[t].b.normal = (entMat * kl::float4(entities[e].mesh->buffer[t].b.normal, 0.0f)).xyz().normalize();
				entities[e].computed.buffer[t].c.normal = (entMat * kl::float4(entities[e].mesh->buffer[t].c.normal, 0.0f)).xyz().normalize();
				return;
			}
			leftTrCount = rightTrCount;
		}
	}
}
