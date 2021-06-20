package settlement.kotlin.batch

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object DateTimeUtil {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun parseDate(date: String): LocalDate =
        LocalDate.parse(date, dateTimeFormatter)

    fun getStartDateTime(date: String): LocalDateTime =
        parseDate(date).let {
            LocalDateTime.of(it, LocalTime.MIN)
        }

    fun getEndDateTime(date: String): LocalDateTime =
        parseDate(date).let {
            LocalDateTime.of(it, LocalTime.MAX)
        }
}