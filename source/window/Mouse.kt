package window

import java.awt.Point

class Mouse : HashMap<Int, () -> Unit>() {
    internal val states = HashMap<Int, Boolean>()
    var position = Point()

    internal fun process(window: Window) {
        position = window.mousePosition ?: Point()
        for (entry in this) {
            if (states[entry.key] == true) {
                entry.value()
            }
        }
    }
}
