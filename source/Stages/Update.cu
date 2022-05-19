#include "Raytracer.cuh"
#include "Kernels/Kernels.cuh"


#define LOG_TIMES_
void TimeIt(const String& message, bool endOfFrame = false) {
#ifdef LOG_TIMES
	static float lastElapsed = 0.0f;
	static size_t frameCounter = 0;
	const float elapsed = Raytracer::timer.elapsed() - lastElapsed;
	std::cout << "Frame: " << frameCounter << ", " << message << " time: " << elapsed << std::endl;
	if (endOfFrame) {
		frameCounter++;
		std::cout << std::endl;
	}
	lastElapsed = Raytracer::timer.elapsed();
#endif
}

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
	ComputePhysics();
	TimeIt("Physics");

	// Precompute
	PrecomputeTransforms();
	TimeIt("Precompute");

	// Trace
	Raytrace();
	TimeIt("Trace");

	// Draw texture
	DrawFrame();
	TimeIt("Draw", true);

	// FPS
	win.setTitle(std::to_string(int(1.0f / deltaT)));
}
