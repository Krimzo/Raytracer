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

    window.mouse[3] = {
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
    val orangeMat = Material()
    orangeMat.albedo = Vector3(Color(220, 150, 105))
    orangeMat.roughness = 0.1
    orangeMat.emissionColor = orangeMat.albedo
    orangeMat.emissionPower = 2.5

    val greenMat = Material()
    greenMat.albedo = Vector3(Color(80, 200, 130))
    greenMat.roughness = 0.2

    val whiteMat = Material()
    whiteMat.albedo = Vector3(Color.WHITE)
    greenMat.roughness = 0.5

    // Spheres
    val sphere1 = Sphere()
    sphere1.origin = Vector3(30.0, 10.0, 0.0)
    sphere1.radius = 15.0
    sphere1.material = orangeMat
    scene.add(sphere1)

    val sphere2 = Sphere()
    sphere2.origin = Vector3(0.0, 1.0, 0.0)
    sphere2.radius = 1.0
    sphere2.material = greenMat
    scene.add(sphere2)

    val sphere3 = Sphere()
    sphere3.origin = Vector3(0.0, -1000.0, 0.0)
    sphere3.radius = 1000.0
    sphere3.material = whiteMat
    scene.add(sphere3)

    // Camera setup
    val camera = scene.camera
    camera.position = Vector3(0.0, 2.0, 5.0)
    camera.forward = -camera.position

    return scene
}
