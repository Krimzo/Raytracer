package editor

import logging.LogPanel
import raytracer.Raytracer
import render.RenderPanel
import scene.ScenePanel
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JFrame

class EditorWindow : JFrame() {
    val scenePanel = ScenePanel(this)
    val renderPanel = RenderPanel(this)
    val logPanel = LogPanel(this)

    val raytracer = Raytracer(this)

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        this.size = Dimension(1600, 900)
        this.title = "Krimz Raytracer"

        layout = BorderLayout()
        add(scenePanel, BorderLayout.WEST)
        add(renderPanel, BorderLayout.CENTER)
        add(logPanel, BorderLayout.EAST)

        isVisible = true
        extendedState = (extendedState or MAXIMIZED_BOTH)
    }
}
