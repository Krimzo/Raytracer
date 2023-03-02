package render

import editor.EditorWindow
import raytracer.Square
import java.awt.Color
import java.awt.Graphics
import javax.swing.JPanel
import kotlin.math.min

class RenderPanel(private val editor: EditorWindow) : JPanel() {
    var buffer = FrameBuffer(1, 1)
    val squares = HashMap<Long, Square>()

    val aspect: Double
        get() = (width.toDouble() / height)

    init {
        isVisible = true
    }

    fun reallocateBuffer() {
        if (width != buffer.width || height != buffer.height) {
            buffer = FrameBuffer(width, height)
        }
    }

    fun clear(color: Color) {
        buffer.graphics.let {
            it.color = color
            it.fillRect(0, 0, width, height)
            it.dispose()
        }
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
}
