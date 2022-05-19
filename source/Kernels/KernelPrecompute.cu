#include "Kernels/Kernels.cuh"


EXEC void Kernels::Precompute(size_t entityCount, Raytracer::Entity* entities) {
	const size_t e = kl::cuda::Index();
	if (e < entityCount) {

	}
}
