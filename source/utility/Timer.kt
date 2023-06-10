package utility

class Timer {
    private val elapsedStart = System.nanoTime()
    private var deltaStart = System.nanoTime()
    private var deltaEnd = System.nanoTime()

    fun elapsed(): Double {
        return calcuate(elapsedStart, System.nanoTime())
    }

    fun delta(): Double {
        return calcuate(deltaStart, deltaEnd)
    }

    fun updateDelta() {
        deltaStart = deltaEnd
        deltaEnd = System.nanoTime()
    }

    private fun calcuate(start: Long, end: Long): Double {
        return (end - start) * 1e-9
    }
}
