#include "Raytracer.cuh"


void MovementSetup() {
	Raytracer::win.keys.w.down = [&]() {
		Raytracer::camera.moveForward(Raytracer::deltaT);
	};
	Raytracer::win.keys.s.down = [&]() {
		Raytracer::camera.moveBack(Raytracer::deltaT);
	};
	Raytracer::win.keys.d.down = [&]() {
		Raytracer::camera.moveRight(Raytracer::deltaT);
	};
	Raytracer::win.keys.a.down = [&]() {
		Raytracer::camera.moveLeft(Raytracer::deltaT);
	};
	Raytracer::win.keys.e.down = [&]() {
		Raytracer::camera.moveUp(Raytracer::deltaT);
	};
	Raytracer::win.keys.q.down = [&]() {
		Raytracer::camera.moveDown(Raytracer::deltaT);
	};
	Raytracer::win.keys.shift.press = [&]() {
		Raytracer::camera.speed = 5.0f;
	};
	Raytracer::win.keys.shift.release = [&]() {
		Raytracer::camera.speed = 2.0f;
	};
}

static bool firstClick = true;
static bool camMoving = false;
void RotationSetup() {
	Raytracer::win.mouse.rmb.press = [&]() {
		Raytracer::win.mouse.hide();
		camMoving = true;
	};
	Raytracer::win.mouse.rmb.down = [&]() {
		if (camMoving) {
			// Window center
			const kl::int2 frameCenter = Raytracer::win.getCenter();

			// First click jump fix
			if (firstClick) {
				Raytracer::win.mouse.position = frameCenter;
				firstClick = false;
			}

			// Camera rotation
			Raytracer::camera.rotate(Raytracer::win.mouse.position, frameCenter);
			Raytracer::win.mouse.move(frameCenter);
		}
	};
	Raytracer::win.mouse.rmb.release = [&]() {
		Raytracer::win.mouse.show();
		firstClick = true;
		camMoving = false;
	};
}

void Raytracer::SetupInput() {
	MovementSetup();
	RotationSetup();

	/* DEBUG */
	static const float speed = 0.5f;
	Raytracer::win.keys.up.down = [&]() {
		Raytracer::entities[2].position.z += speed * Raytracer::deltaT;
	};
	Raytracer::win.keys.down.down = [&]() {
		Raytracer::entities[2].position.z -= speed * Raytracer::deltaT;
	};
	Raytracer::win.keys.right.down = [&]() {
		Raytracer::entities[2].position.x += speed * Raytracer::deltaT;
	};
	Raytracer::win.keys.left.down = [&]() {
		Raytracer::entities[2].position.x -= speed * Raytracer::deltaT;
	};
}
