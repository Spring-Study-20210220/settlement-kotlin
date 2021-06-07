package settlement.kotlin.service.event

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import settlement.kotlin.service.user.res.RegisterUserResponse

@Aspect
@Component
class EventPublisher(
    val eventPublisher: ApplicationEventPublisher
) {

    @Pointcut("within(settlement.kotlin.service..*)")
    fun calledInService() {}

    @Around("calledInService()")
    fun doPublishEvent(jp: ProceedingJoinPoint): Any? {
        val result = jp.proceed()

        if(result is RegisterUserResponse) {
            println("Event Published!")
            println(result)
            eventPublisher.publishEvent(result)
        }

        return result
    }
}