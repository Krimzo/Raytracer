package utility

object Memory {
    fun getTotalMemory(): Long {
        return Runtime.getRuntime().totalMemory()
    }

    fun getFreeMemory(): Long {
        return Runtime.getRuntime().freeMemory()
    }

    fun getAllocatedMemory(): Long {
        return (getTotalMemory() - getFreeMemory())
    }
}
