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

#define PRECOMPUTE_CPU_
void PrecomputeVertices() {
#ifdef PRECOMPUTE_CPU
	for (size_t e = 0; e < Raytracer::entities.size(); e++) {
		for (size_t t = 0; t < Raytracer::entities[e].mesh.size; t++) {
			const kl::mat4 entMat = Raytracer::entities[e].matrix();
			Raytracer::entities[e].mesh.computed.buffer[t].a.world = (entMat * kl::float4(Raytracer::entities[e].mesh.buffer[t].a.world, 1.0f)).xyz();
			Raytracer::entities[e].mesh.computed.buffer[t].b.world = (entMat * kl::float4(Raytracer::entities[e].mesh.buffer[t].b.world, 1.0f)).xyz();
			Raytracer::entities[e].mesh.computed.buffer[t].c.world = (entMat * kl::float4(Raytracer::entities[e].mesh.buffer[t].c.world, 1.0f)).xyz();
			Raytracer::entities[e].mesh.computed.buffer[t].a.texture = Raytracer::entities[e].mesh.buffer[t].a.texture;
			Raytracer::entities[e].mesh.computed.buffer[t].b.texture = Raytracer::entities[e].mesh.buffer[t].b.texture;
			Raytracer::entities[e].mesh.computed.buffer[t].c.texture = Raytracer::entities[e].mesh.buffer[t].c.texture;
			Raytracer::entities[e].mesh.computed.buffer[t].a.normal = (entMat * kl::float4(Raytracer::entities[e].mesh.buffer[t].a.normal, 0.0f)).xyz().normalize();
			Raytracer::entities[e].mesh.computed.buffer[t].b.normal = (entMat * kl::float4(Raytracer::entities[e].mesh.buffer[t].b.normal, 0.0f)).xyz().normalize();
			Raytracer::entities[e].mesh.computed.buffer[t].c.normal = (entMat * kl::float4(Raytracer::entities[e].mesh.buffer[t].c.normal, 0.0f)).xyz().normalize();
		}
		Raytracer::entities[e].mesh.computed.far = (Raytracer::entities[e].mesh.far * Raytracer::entities[e].scale).length();
	}
#else
	kl::cuda::Exec(Kernels::Precompute, Raytracer::entities.size(), Raytracer::entities.pointer());
#endif
}

void Raytracer::Update() {
	// Time
	deltaT = timer.interval();
	elapsedT = timer.elapsed();

	// Phys
	for (size_t i = 0; i < Raytracer::entities.size(); i++) {
		Raytracer::entities[i].updatePhys(Raytracer::deltaT);
	}
	TimeIt("Physics");

	// Precompute vertices
	PrecomputeVertices();
	TimeIt("Precompute");

	// Trace
	kl::cuda::Exec(Kernels::Raytrace, pixelBuffer.len, pixelBuffer.buffer, pixelBuffer.size,
		Raytracer::camera.position, Raytracer::camera.matrix().inverse(),
		Raytracer::entities.pointer(), Raytracer::entities.size());
	TimeIt("Trace");

	// Draw texture
	static kl::image tempPixelBuffer(pixelBuffer.size);
	tempPixelBuffer.resize(pixelBuffer.size);
	cudaMemcpy(tempPixelBuffer.pointer(), pixelBuffer.buffer, pixelBuffer.len * sizeof(kl::color), cudaMemcpyDeviceToHost);
	win.drawImage(tempPixelBuffer);
	TimeIt("Draw", true);

	// FPS
	win.setTitle(std::to_string(int(1.0f / deltaT)));
}
