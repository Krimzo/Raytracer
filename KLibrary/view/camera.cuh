#pragma once 

#include "math/mat4.cuh"

#include "cuda/kcuda.cuh"

#undef near
#undef far


namespace kl {
	class camera {
	public:
		kl::float3 forward = kl::float3(0.0f, 0.0f, 1.0f);
		kl::float3 position;
		float aspect = 1.7778f;
		float fov = 75.0f;
		float near = 0.01f;
		float far = 200.0f;
		float speed = 2.0f;
		float sens = 0.1f;

		// Camera direction getters
		ALL kl::float3 getForward() const {
			return forward.normalize();
		}
		ALL kl::float3 getRight() const {
			return kl::float3(0.0f, 1.0f, 0.0f).cross(getForward()).normalize();
		}
		ALL kl::float3 getUp() const {
			return getForward().cross(getRight()).normalize();
		}

		// Camera movement
		ALL void moveForward(float deltaTime) {
			position = position + getForward() * (speed * deltaTime);
		}
		ALL void moveBack(float deltaTime) {
			position = position - getForward() * (speed * deltaTime);
		}
		ALL void moveRight(float deltaTime) {
			position = position + getRight() * (speed * deltaTime);
		}
		ALL void moveLeft(float deltaTime) {
			position = position - getRight() * (speed * deltaTime);
		}
		ALL void moveUp(float deltaTime) {
			position = position + kl::float3(0.0f, 1.0f, 0.0f) * (speed * deltaTime);
		}
		ALL void moveDown(float deltaTime) {
			position = position - kl::float3(0.0f, 1.0f, 0.0f) * (speed * deltaTime);
		}

		// Camera rotation
		ALL void rotate(const kl::int2& mousePos, const kl::int2& frameCenter, float verticalAngleLimit = 85.0f) {
			// Calculating the mouse movement
			const int dx = mousePos.x - frameCenter.x;
			const int dy = mousePos.y - frameCenter.y;

			// Calculating the x and y rotation
			const float xRotation = dx * sens;
			const float yRotation = dy * sens;

			// Calculating the vertically rotated forward vector
			kl::float3 forwardVert = getForward().rotate(yRotation, getRight());

			// Checking if the vertical rotation is goin to be inside the bounds
			if (std::abs(forwardVert.angle(kl::float3(0.0f, 1.0f, 0.0f)) - 90.0f) <= verticalAngleLimit) {
				forward = forwardVert;
			}

			// Calculating the horizontally rotated forward vector
			forward = getForward().rotate(xRotation, kl::float3(0.0f, 1.0f, 0.0f));
		}

		// Computes and returns the camera matrix
		ALL kl::mat4 lookAtMat() const {
			return kl::mat4::lookAt(position, position + getForward(), kl::float3(0.0f, 1.0f, 0.0f));
		}
		ALL kl::mat4 projMat() const {
			return kl::mat4::persp(fov, aspect, near, far);
		}
		ALL kl::mat4 matrix() const {
			return projMat() * lookAtMat();
		}
	};
}
