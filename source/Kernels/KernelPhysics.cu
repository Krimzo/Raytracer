#include "Kernels/Kernels.cuh"


EXEC void Kernels::Physics(size_t entityCount, Raytracer::Entity* entities, float deltaT) {
	size_t i = kl::cuda::GetX();
	if (i < entityCount) {
		entities[i].updatePhys(deltaT);
	}
}
