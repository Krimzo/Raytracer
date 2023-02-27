package utility

class Timer {
    private val startTime = System.nanoTime()

    fun elapsed(): Double {
        return (System.nanoTime() - startTime) * 1e-9
    }
}
