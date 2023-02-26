package window

import raytracer.Square
import java.awt.Graphics
import javax.swing.JPanel

class DrawTarget : JPanel() {
    var buffer = FrameBuffer(1, 1)
    val squares = HashMap<Long, Square>()

    init {
        isVisible = true
    }

    private fun drawSquares(graphics: Graphics) {
        for (square in squares.values) {
            graphics.color = square.color
            graphics.drawRect(square.x, square.y, square.size, square.size)
        }
    }

    override fun paint(g: Graphics?) {
        g?.let {
            it.drawImage(buffer, 0, 0, null)
            drawSquares(it)
        }
    }
}
