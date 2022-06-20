#include "Raytracer.h"


void Raytracer::Resize(const kl::int2& newSize) {
	pixelBuffer = Raytracer::Texture(newSize);
	camera.aspect = float(newSize.x) / newSize.y;
}
