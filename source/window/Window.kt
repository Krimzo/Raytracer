package window

import math.vector.Int2
import java.awt.Dimension
import javax.swing.JFrame

class Window(size: Int2, title: String) : JFrame() {
    init {
        defaultCloseOperation = DISPOSE_ON_CLOSE
        setSize(Dimension(size.x, size.y))
        setTitle(title)
        isVisible = true
    }

    val frameSize: Int2
        get() = Int2(width, height)

    fun process(): Boolean {
        return isDisplayable
    }

    fun display(frame: FrameBuffer) {
        graphics.drawImage(frame, 0, 0, null)
    }
}
