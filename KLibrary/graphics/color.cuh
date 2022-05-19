#pragma once 

#include <iostream>
#include <windows.h>

#include "cuda/kcuda.cuh"


namespace kl {
	class color {
	public:
		byte b, g, r, a;

		// Constructors
		ALL color() : b(0), g(0), r(0), a(0) {}
		ALL color(byte r, byte g, byte b, byte a = 255) : b(b), g(g), r(r), a(a) {}

		// Operator overloading
		ALL bool equals(const kl::color& obj) const {
			return r == obj.r && g == obj.g && b == obj.b && a == obj.a;
		}
		ALL bool operator==(const kl::color& obj) const {
			return this->equals(obj);
		}
		ALL bool operator!=(const kl::color& obj) const {
			return !this->equals(obj);
		}

		ALL int getInt() const {
			return *(int*)this;
		}
		ALL kl::color grayscale() const {
			const byte grayValue = byte(r * 0.299f + g * 0.587f + b * 0.114f);
			return kl::color(grayValue, grayValue, grayValue, a);
		}
		ALL kl::color invert() const {
			return kl::color(255 - r, 255 - g, 255 - b, a);
		}
		ALL char toASCII() const {
			const char asciiTable[10] = { '@', '%', '#', 'x', '+', '=', ':', '-', '.', ' ' };
			return asciiTable[min(int(grayscale().r * 0.035294117f), 255)];
		}

		ALL kl::color mix(const kl::color& col, float ratio) const {
			ratio = min(max(ratio, 0.0f), 1.0f);
			const float iratio = 1.0f - ratio;
			return kl::color(
				byte(r * iratio) + byte(col.r * ratio),
				byte(g * iratio) + byte(col.g * ratio),
				byte(b * iratio) + byte(col.b * ratio)
			);
		}
		ALL kl::color mix(const kl::color& col) const {
			return mix(col, col.a * 0.00392156f);
		}
	};

	// Predefined colors
	namespace colors {
		inline const kl::color defaul = kl::color(204, 204, 204);
		inline const kl::color black = kl::color(0, 0, 0);
		inline const kl::color white = kl::color(255, 255, 255);
		inline const kl::color gray = kl::color(50, 50, 50);
		inline const kl::color lgray = kl::color(100, 100, 100);
		inline const kl::color red = kl::color(255, 0, 0);
		inline const kl::color green = kl::color(0, 255, 0);
		inline const kl::color blue = kl::color(0, 0, 255);
		inline const kl::color cyan = kl::color(30, 180, 170);
		inline const kl::color purple = kl::color(220, 0, 220);
		inline const kl::color yellow = kl::color(220, 220, 0);
		inline const kl::color orange = kl::color(255, 140, 0);
		inline const kl::color magenta = kl::color(155, 0, 155);
		inline const kl::color crimson = kl::color(100, 0, 0);
		inline const kl::color wheat = kl::color(245, 220, 180);
		inline const kl::color sky = kl::color(190, 245, 255);
	};
}
