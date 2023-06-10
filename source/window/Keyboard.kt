package window

class Keyboard : HashMap<Char, () -> Unit>() {
    internal val states = HashMap<Char, Boolean>()

    internal fun process() {
        for (entry in this) {
            if (states[entry.key] == true) {
                entry.value()
            }
        }
    }
}
