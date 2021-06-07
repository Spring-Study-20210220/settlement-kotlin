package settlement.kotlin.service

import java.time.Instant

interface Event {
    val timestamp: Instant
}
