package raytracer

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class JobQueue {
    private val executor = Executors.newFixedThreadPool(cpuThreadCount)

    fun addJob(job: (Long) -> Unit) {
        executor.execute { job(Thread.currentThread().id) }
    }

    fun finalize() {
        executor.shutdown()
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)
    }

    companion object {
        val cpuThreadCount = Runtime.getRuntime().availableProcessors()
    }
}
