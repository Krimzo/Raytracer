#include "Raytracer.cuh"


void Raytracer::User() {

	const float interval = 24.0f;
	const float val = elapsedT * (3.14159265359f / interval);
	sunDir.x = std::sin(val);
	sunDir.y = std::cos(val);

}
