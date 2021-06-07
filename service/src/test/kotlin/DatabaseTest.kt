package settlement.kotlin.service

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EntityScan(basePackages = ["settlement.kotlin.db"])
@EnableJpaRepositories(basePackages = ["settlement.kotlin.db"])
@ComponentScan(basePackages = ["settlement.kotlin"])
annotation class DatabaseTest
