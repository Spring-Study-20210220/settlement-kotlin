package settlement.kotlin.batch

import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class Scheduler(
    val jobLauncher: JobLauncher,
    val batchConfig: BatchConfig
) {

//    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
//    fun runJob() {
//        jobLauncher.run(
//            batchConfig.simpleJob(),
//            JobParametersBuilder()
//                .addString("date", LocalDateTime.now().toString())
//                .toJobParameters()
//        )
//    }
}