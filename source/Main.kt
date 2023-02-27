import entity.Entity
import entity.material.Material
import entity.material.Texture
import entity.mesh.StorageMesh
import math.vector.Vector3
import raytracer.Raytracer
import window.Timer
import window.Window
import java.awt.Color
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

fun main() {
    Main()
}

class Main : KeyListener {
    private val window = Window(1600, 900, "Raytracer")
    private val tracer = Raytracer()

    init {
        setupTestScene()
        window.addKeyListener(this)
    }

    private fun setupTestScene() {
        // Meshes
        val cubeMesh = StorageMesh("resource/meshes/cube.obj")
        val monkeMesh = StorageMesh("resource/meshes/monke.obj")
        val sphereMesh = StorageMesh("resource/meshes/sphere.obj")

        // Textures
        val checkersTexture = Texture("resource/textures/checkers.png")
        val dogoTexture = Texture("resource/textures/dogo.png")

        // Cube
        val cube = Entity()
        cube.position = Vector3(-3.0, 0.0, 3.5)
        cube.rotation.y = 45.0
        cube.storageMesh = cubeMesh
        cube.material = Material()
        cube.material?.color = Vector3(1.0)
        cube.material?.colorMap = checkersTexture
        tracer.scene["Cube"] = cube

        // Monke
        val monke = Entity()
        monke.position = Vector3(0.0, 0.1, 2.75)
        monke.rotation = Vector3(-10.0, -32.0, 0.0)
        monke.storageMesh = monkeMesh
        monke.material = Material()
        monke.material?.color = Vector3(Color(220, 150, 105))
        monke.material?.colorMap = checkersTexture
        tracer.scene["Monke"] = monke

        // Sphere
        val sphere = Entity()
        sphere.position = Vector3(3.0, 0.0, 3.0)
        sphere.storageMesh = sphereMesh
        sphere.material = Material()
        sphere.material?.color = Vector3(Color(80, 200, 130))
        sphere.material?.colorMap = checkersTexture
        sphere.material?.roughness = 0.1
        tracer.scene["Sphere"] = sphere

        // Camera setup
        val camera = tracer.scene.camera
        camera.position = Vector3(-1.0, 2.0, -2.0)
        camera.direction = monke.position - camera.position

        // Light
        tracer.scene.directionalLight.direction = Vector3(-1.0, -1.0, 1.0)
    }

    @Synchronized private fun renderScene() {
        val timer = Timer()
        window.isResizable = false
        tracer.render(window)
        window.isResizable = true
        window.title = "Raytracer [${timer.elapsed()} s]"
    }

    @Synchronized private fun saveScene() {
        window.target.buffer.saveToFile("render.png")
    }

    override fun keyTyped(e: KeyEvent?) {}

    override fun keyPressed(e: KeyEvent?) {}

    override fun keyReleased(e: KeyEvent?) {
        when (e?.keyChar) {
            'r' -> { Thread { renderScene() }.start() }
            's' -> { Thread { saveScene() }.start() }
        }
    }
}
