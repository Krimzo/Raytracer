package editor

import logging.Logger
import raytracer.Raytracer
import render.RenderWindow
import scene.Scene
import utility.Timer
import java.awt.BorderLayout
import java.awt.Dimension
import java.util.*
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel

class EditorWindow(private val renderWindow: RenderWindow, private val raytracer: Raytracer) : JFrame() {
    private val newSceneButton = JButton("New Scene").let {
        it.addActionListener {
            raytracer.scene = Scene()
        }
        it.isVisible = true
        it
    }

    private val renderSceneButton = JButton("Render Scene").let { button ->
        button.addActionListener {
            Thread { renderScene() }.start()
        }
        button.isVisible = true
        button
    }

    private val saveSceneButton = JButton("Save Scene").let {
        it.addActionListener {

        }
        it.isVisible = true
        it
    }

    private val loadSceneButton = JButton("Load Scene").let {
        it.addActionListener {

        }
        it.isVisible = true
        it
    }

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        size = Dimension(500, 500)
        title = "Scene Editor"

        layout = BorderLayout()
        JPanel().let { panel0 ->
            panel0.layout = BorderLayout()
            panel0.add(newSceneButton, BorderLayout.WEST)
            panel0.add(renderSceneButton, BorderLayout.CENTER)

            JPanel().let { panel1 ->
                panel1.layout = BorderLayout()
                panel1.add(saveSceneButton, BorderLayout.WEST)
                panel1.add(loadSceneButton, BorderLayout.EAST)

                panel1.isVisible = true
                panel0.add(panel1, BorderLayout.EAST)
            }

            panel0.isVisible = true
            add(panel0, BorderLayout.NORTH)
        }

        isVisible = true
    }

    @Synchronized private fun renderScene() {
        newSceneButton.isEnabled = false
        renderSceneButton.isEnabled = false
        saveSceneButton.isEnabled = false
        loadSceneButton.isEnabled = false
        renderWindow.isResizable = false

        Logger.log("Render started")

        val timer = Timer()
        raytracer.render(renderWindow)
        val elapsedTime = timer.elapsed()

        Logger.log("Render finished in %.2f seconds".format(Locale.US, elapsedTime))

        renderWindow.isResizable = true
        loadSceneButton.isEnabled = true
        saveSceneButton.isEnabled = true
        renderSceneButton.isEnabled = true
        newSceneButton.isEnabled = true
    }
}
