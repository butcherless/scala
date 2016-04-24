import java.time.LocalDate

val MEGA = 1024
val today = LocalDate.now
val dayOfMonth = today.getDayOfMonth

val lastDayOfMonth = LocalDate.now.getMonth.maxLength
val limit = 100 * dayOfMonth.toDouble / lastDayOfMonth

val mBytesPerDay = 2.toDouble * MEGA / lastDayOfMonth

val message = "today: " + today + " - " +
               f"$limit%2.2f"+ "% - " + f"$mBytesPerDay%2.2f" + " MBytes/day"

println(message)
