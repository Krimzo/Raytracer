import math.ray.Sphere
import math.vector.Vector2
import math.vector.Vector3
import raytracer.Raytracer
import window.Window
import scene.Material
import scene.Scene
import utility.Timer
import java.awt.Color

fun main() {
    val window = Window(800, 600)
    val raytracer = Raytracer()
    val scene = setupTestScene()
    val timer = Timer()

    window.keyboard['r'] = {
        raytracer.reset()
    }
    window.keyboard['p'] = {
        raytracer.save("render.png")
    }

    window.keyboard['w'] = {
        scene.camera.moveForward(timer.delta())
        raytracer.reset()
    }
    window.keyboard['s'] = {
        scene.camera.moveBack(timer.delta())
        raytracer.reset()
    }
    window.keyboard['d'] = {
        scene.camera.moveRight(timer.delta())
        raytracer.reset()
    }
    window.keyboard['a'] = {
        scene.camera.moveLeft(timer.delta())
        raytracer.reset()
    }
    window.keyboard['e'] = {
        scene.camera.moveUp(timer.delta())
        raytracer.reset()
    }
    window.keyboard['q'] = {
        scene.camera.moveDown(timer.delta())
        raytracer.reset()
    }

    window.mouse[1] = {
        val mousePosition = Vector2(window.mouse.position.x.toDouble(), window.mouse.position.y.toDouble())
        scene.camera.rotate(mousePosition, window.center)
        window.centerMouse()
        raytracer.reset()
    }

    while (window.process()) {
        timer.updateDelta()
        raytracer.resize(window.renderDimension)
        val buffer = raytracer.render(scene)
        window.display(buffer)
    }
}

fun setupTestScene(): Scene {
    // Scene
    val scene = Scene()

    // Materials
    val material0 = Material()
    material0.albedo = Vector3(Color(225, 155, 15))

    val material1 = Material()
    material1.albedo = Vector3(1.0)
    material1.emissionColor = material1.albedo
    material1.emissionPower = 10.0

    val material2 = Material()
    material2.albedo = Vector3(Color(235, 15, 115))

    // Spheres
    val sphere0 = Sphere()
    sphere0.origin = Vector3(0.0, -100.0, 0.0)
    sphere0.radius = 100.0
    sphere0.material = material0
    scene.add(sphere0)

    val sphere1 = Sphere()
    sphere1.origin = Vector3(25.0, 10.0, 0.0)
    sphere1.radius = 10.0
    sphere1.material = material1
    scene.add(sphere1)

    val sphere2 = Sphere()
    sphere2.origin = Vector3(0.0, 1.0, 0.0)
    sphere2.radius = 1.0
    sphere2.material = material2
    scene.add(sphere2)

    // Camera setup
    val camera = scene.camera
    camera.position = Vector3(-2.68, 2.45, -4.07)
    camera.forward = Vector3(0.78, -0.22, 0.59)

    return scene
}
