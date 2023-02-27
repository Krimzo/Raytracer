package render

import raytracer.Square
import java.awt.Graphics
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import javax.swing.JPanel
import kotlin.math.min

class DrawTarget : JPanel(), ComponentListener {
    var buffer = FrameBuffer(1, 1)
    val squares = HashMap<Long, Square>()

    val aspect: Double
        get() = width.toDouble() / height

    init {
        addComponentListener(this)
        isVisible = true
    }

    private fun drawSquares(graphics: Graphics) {
        for (square in squares.values) {
            val width = min(square.size - 1, width - 1 - square.x)
            val height = min(square.size - 1, height - 1 - square.y)
            graphics.color = square.color
            graphics.drawRect(square.x, square.y, width, height)
        }
    }

    override fun paint(g: Graphics?) {
        g?.let {
            it.drawImage(buffer, 0, 0, null)
            drawSquares(it)
        }
    }

    override fun componentResized(e: ComponentEvent?) {
        buffer = FrameBuffer(width, height)
    }

    override fun componentMoved(e: ComponentEvent?) {}
    override fun componentShown(e: ComponentEvent?) {}
    override fun componentHidden(e: ComponentEvent?) {}
}
