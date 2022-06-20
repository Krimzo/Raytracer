#include "Raytracer.h"
#include "Kernels/Kernels.h"


void ComputePhysics() {
	Kernels::physics.runs = uint(Raytracer::entities.size());
	Kernels::physics.run(Raytracer::entities.size(), Raytracer::entities.pointer(), Raytracer::deltaT);
}

void PrecomputeTransforms() {
	uint triangleCount = 0;
	for (uint e = 0; e < Raytracer::entities.size(); e++) {
		triangleCount += uint(Raytracer::entities[e].mesh->size);
		Raytracer::entities[e].computed.far = (Raytracer::entities[e].mesh->far * Raytracer::entities[e].scale).length();
	}
	Kernels::precompute.runs = triangleCount;
	Kernels::precompute.run(triangleCount, Raytracer::entities.pointer(), Raytracer::entities.size());
}

void Raytrace() {
	Kernels::raytrace.runs = uint(Raytracer::pixelBuffer.len);
	Kernels::raytrace.run(Raytracer::pixelBuffer.len, Raytracer::pixelBuffer.buffer, Raytracer::pixelBuffer.size,
		Raytracer::camera.position, Raytracer::camera.matrix().inverse(),
		Raytracer::entities.pointer(), Raytracer::entities.size(), Raytracer::sunDir.normalize());
}

void DrawFrame() {
	static kl::image tempPixelBuffer(Raytracer::pixelBuffer.size);
	tempPixelBuffer.resize(Raytracer::pixelBuffer.size);
	kl::cuda::copy(tempPixelBuffer.data(), Raytracer::pixelBuffer.buffer,
		Raytracer::pixelBuffer.len, kl::cuda::transfer::DH);
	Raytracer::win.draw(tempPixelBuffer);
}

void Raytracer::Update() {
	// Time
	deltaT = float(timer.interval());
	elapsedT = float(timer.elapsed());

	// Physics
	Raytracer::Debug::TimeItStart();
	ComputePhysics();
	Raytracer::Debug::TimeItEnd("Physics");

	// User
	Raytracer::User();

	// Precompute
	Raytracer::Debug::TimeItStart();
	PrecomputeTransforms();
	Raytracer::Debug::TimeItEnd("Precompute");

	// Trace
	Raytracer::Debug::TimeItStart();
	Raytrace();
	Raytracer::Debug::TimeItEnd("Trace");

	// Draw texture
	Raytracer::Debug::TimeItStart();
	DrawFrame();
	Raytracer::Debug::TimeItEnd("Draw");

	// FPS
	Raytracer::Debug::DisplayTimes();
}
