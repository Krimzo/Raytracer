#include "Raytracer.cuh"


int main() {
	Raytracer::SetupInput();
	Raytracer::win.start = Raytracer::Start;
	Raytracer::win.resize = Raytracer::Resize;
	Raytracer::win.update = Raytracer::Update;
	Raytracer::timer.interval();
	Raytracer::timer.reset();
	Raytracer::win.startNew(Raytracer::pixelBuffer.size, "Raytracer", true, true);
}
