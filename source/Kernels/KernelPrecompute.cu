#include "Kernels/Kernels.cuh"


EXEC void Kernels::Precompute(size_t entityCount, Raytracer::Entity* entities) {
	const size_t e = kl::cuda::Index();
	if (e < entityCount) {
		for (size_t t = 0; t < entities[e].mesh.size; t++) {
			const kl::mat4 entMat = entities[e].matrix();
			entities[e].mesh.computed.buffer[t].a.world = (entMat * kl::float4(entities[e].mesh.buffer[t].a.world, 1.0f)).xyz();
			entities[e].mesh.computed.buffer[t].b.world = (entMat * kl::float4(entities[e].mesh.buffer[t].b.world, 1.0f)).xyz();
			entities[e].mesh.computed.buffer[t].c.world = (entMat * kl::float4(entities[e].mesh.buffer[t].c.world, 1.0f)).xyz();
			entities[e].mesh.computed.buffer[t].a.texture = entities[e].mesh.buffer[t].a.texture;
			entities[e].mesh.computed.buffer[t].b.texture = entities[e].mesh.buffer[t].b.texture;
			entities[e].mesh.computed.buffer[t].c.texture = entities[e].mesh.buffer[t].c.texture;
			entities[e].mesh.computed.buffer[t].a.normal = (entMat * kl::float4(entities[e].mesh.buffer[t].a.normal, 0.0f)).xyz().normalize();
			entities[e].mesh.computed.buffer[t].b.normal = (entMat * kl::float4(entities[e].mesh.buffer[t].b.normal, 0.0f)).xyz().normalize();
			entities[e].mesh.computed.buffer[t].c.normal = (entMat * kl::float4(entities[e].mesh.buffer[t].c.normal, 0.0f)).xyz().normalize();
		}
		entities[e].mesh.computed.far = (entities[e].mesh.far * entities[e].scale).length();
	}
}
