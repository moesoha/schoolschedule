package dev.soha.course202002.schedule.android.data

import androidx.room.TypeConverter
import java.time.DayOfWeek
import java.util.*

class TypeConverters {
	@TypeConverter fun fromDateToLong(value: Date?): Long? = value?.time
	@TypeConverter fun fromLongToDate(value: Long?): Date? = value?.let { Date(it) }

	@TypeConverter fun fromDayOfWeekToInt(value: DayOfWeek?): Int? = value?.value
	@TypeConverter fun fromIntToDayOfWeek(value: Int?): DayOfWeek? = value?.let { DayOfWeek.of(it) }
}
