#include "Raytracer.cuh"
#include "Kernels/Kernels.cuh"


void ComputePhysics() {
	kl::cuda::Exec(Kernels::Physics,
		Raytracer::entities.size(), Raytracer::entities.pointer(), Raytracer::deltaT);
}

void PrecomputeTransforms() {
	size_t triangleCount = 0;
	for (size_t e = 0; e < Raytracer::entities.size(); e++) {
		triangleCount += Raytracer::entities[e].mesh->size;
		Raytracer::entities[e].computed.far = (Raytracer::entities[e].mesh->far * Raytracer::entities[e].scale).length();
	}
	kl::cuda::Exec(Kernels::Precompute,
		triangleCount, Raytracer::entities.pointer(), Raytracer::entities.size());
}

void Raytrace() {
	kl::cuda::Exec(Kernels::Raytrace,
		Raytracer::pixelBuffer.len, Raytracer::pixelBuffer.buffer, Raytracer::pixelBuffer.size,
		Raytracer::camera.position, Raytracer::camera.matrix().inverse(),
		Raytracer::entities.pointer(), Raytracer::entities.size());
}

void DrawFrame() {
	static kl::image tempPixelBuffer(Raytracer::pixelBuffer.size);
	tempPixelBuffer.resize(Raytracer::pixelBuffer.size);
	kl::cuda::copy(tempPixelBuffer.pointer(), Raytracer::pixelBuffer.buffer,
		Raytracer::pixelBuffer.len, kl::cuda::transfer::DH);
	Raytracer::win.drawImage(tempPixelBuffer);
}

void Raytracer::Update() {
	// Time
	deltaT = timer.interval();
	elapsedT = timer.elapsed();

	// Physics
	Raytracer::Debug::TimeItStart();
	ComputePhysics();
	Raytracer::Debug::TimeItEnd("Physics");

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
