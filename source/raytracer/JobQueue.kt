package raytracer

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class JobQueue {
    private val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    fun addJob(job: Runnable) {
        executor.execute(job)
    }

    fun finalize() {
        executor.shutdown()
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)
    }
}
