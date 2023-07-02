package mb.safeEat.extensions

import android.util.Log
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

// default x years ago
// > year = x months ago
// > month = x days ago
// > day = x hours ago
// > hour = x minutes ago
// > minute = just now
data class TimeAgo(val moment: String, val value: Long) {
    override fun toString(): String {
        if (moment == "minute" && value < 1) {
            return "Just now"
        }
        return "$value $moment ago"
    }

    companion object {
        fun parse(date: LocalDateTime): TimeAgo {
            val today = LocalDateTime.now()

            val day = ChronoUnit.DAYS.between(date, today)
            val month = ChronoUnit.MONTHS.between(date, today)
            val year = ChronoUnit.YEARS.between(date, today)
            val minute = ChronoUnit.MINUTES.between(date, today)
            val hour = ChronoUnit.HOURS.between(date, today)

            if (year > 1) return TimeAgo("years", year)
            if (year > 0) return TimeAgo("year", year)

            if (month > 1) return TimeAgo("months", month)
            if (month > 0) return TimeAgo("month", month)

            if (day > 1) return TimeAgo("days", day)
            if (day > 0) return TimeAgo("day", day)

            if (hour > 1) return TimeAgo("hours", hour)
            if (hour > 0) return TimeAgo("hour", hour)

            if (minute > 1) return TimeAgo("minutes", minute)

            return TimeAgo("minute", minute)
        }

        fun parse(date: String): TimeAgo {
            return parse(LocalDateTime.parse(date))
        }
    }
}


private fun test(date: LocalDateTime) {
    val now = LocalDateTime.now()
    val result = TimeAgo.parse(date.toString()).toString()
    Log.d("Test", "date = $date, now = $now, result = $result")
}

@Suppress("unused")
private fun testTimeAgo() {
    val now = LocalDateTime.now()
    test(now)
    test(now.minusMinutes(1))
    test(now.minusHours(1))
    test(now.minusHours(2))
    test(now.minusDays(1))
    test(now.minusDays(2))
    test(now.minusMonths(1))
    test(now.minusMonths(2))
    test(now.minusYears(1))
    test(now.minusYears(2))
}
