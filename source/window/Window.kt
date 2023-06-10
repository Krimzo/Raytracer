package window

import math.vector.Vector2
import raytracer.FrameBuffer
import java.awt.Dimension
import java.awt.Point
import java.awt.Robot
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities

class Window(width: Int, height: Int) : JFrame(), KeyListener, MouseListener {
    private val renderPanel = JPanel()

    val keyboard = Keyboard()
    val mouse = Mouse()

    val renderDimension: Dimension
        get() = renderPanel.size
    val center: Vector2
        get() = Vector2(width * 0.5, height * 0.5)

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        this.size = Dimension(width, height)
        this.title = "Raytracer"
        this.addKeyListener(this)
        this.addMouseListener(this)
        add(renderPanel)
        isVisible = true
    }

    fun process(): Boolean {
        keyboard.process()
        mouse.process(this)
        return isDisplayable
    }

    fun display(frameBuffer: FrameBuffer) {
        renderPanel.graphics.let {
            it.drawImage(frameBuffer, 0, 0, null)
            it.dispose()
        }
    }

    fun centerMouse() {
        val point = Point(width / 2, height / 2)
        SwingUtilities.convertPointToScreen(point, this)
        Robot().mouseMove(point.x, point.y)
    }

    fun maximize() {
        extendedState = (extendedState or MAXIMIZED_BOTH)
    }

    override fun keyTyped(e: KeyEvent?) {
    }

    override fun keyPressed(e: KeyEvent?) {
        if (e == null) return
        keyboard.states[e.keyChar] = true
    }

    override fun keyReleased(e: KeyEvent?) {
        if (e == null) return
        keyboard.states[e.keyChar] = false
    }

    override fun mouseClicked(e: MouseEvent?) {
    }

    override fun mousePressed(e: MouseEvent?) {
        if (e == null) return
        mouse.states[e.button] = true
    }

    override fun mouseReleased(e: MouseEvent?) {
        if (e == null) return
        mouse.states[e.button] = false
    }

    override fun mouseEntered(e: MouseEvent?) {
    }

    override fun mouseExited(e: MouseEvent?) {
    }
}
