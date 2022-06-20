#include "Kernels/Kernels.h"


RUN void Kernels::Physics(uint64 entityCount, Raytracer::Entity* entities, float deltaT) {
	const uint64 i = kl::cuda::getX();
	if (i < entityCount) {
		entities[i].updatePhys(deltaT);
	}
}
