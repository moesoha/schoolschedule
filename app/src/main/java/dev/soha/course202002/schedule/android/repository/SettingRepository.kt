package dev.soha.course202002.schedule.android.repository

import android.content.Context
import dev.soha.course202002.schedule.android.data.AppDatabase
import dev.soha.course202002.schedule.android.data.SettingDao
import dev.soha.course202002.schedule.android.helper.DateHelper
import dev.soha.course202002.schedule.android.model.Setting
import java.util.*

class SettingRepository(context: Context) {
	companion object {
		val TAG = SettingRepository::class.qualifiedName
	}
	val settingDao: SettingDao = AppDatabase.getDatabase(context).settingDao()

	suspend fun displayFirstWeekMonday(): Date =
		settingDao.get(Setting.DISPLAY_FIRST_WEEK_MONDAY).let {
			if (it == null) {
				val v = DateHelper.getMondayOfWeekFromDate(Date())
				displayFirstWeekMonday(v)
				return v
			}
			return DateHelper.stringToDate(it)
		}
	suspend fun displayFirstWeekMonday(value: Date) =
		settingDao.set(Setting.DISPLAY_FIRST_WEEK_MONDAY, DateHelper.dateToString(value))

	suspend fun getCurrentRelativeWeekNumber(): Int =
		DateHelper.diffWeekBetweenDates(displayFirstWeekMonday(), Date()) + 1
	suspend fun setCurrentRelativeWeekNumber(n: Int) {
		val currentMonday = DateHelper.getMondayOfWeekFromDate(Date())
		val firstMonday = DateHelper.addWeekFromDate(currentMonday, (-1 * n) + 1)
		displayFirstWeekMonday(firstMonday)
	}
}