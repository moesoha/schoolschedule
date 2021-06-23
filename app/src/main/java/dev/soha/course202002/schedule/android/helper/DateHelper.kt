package dev.soha.course202002.schedule.android.helper

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.*
import kotlin.math.roundToInt

class DateHelper {
	companion object {
		/**
		 * 把 Calendar 中的 DAY_OF_WEEK 值转换成 DayOfWeek
		 * @param Int dow
		 * @return DayOfWeek
		 */
		fun convertCalendarDowToDayOfWeek(i: Int): DayOfWeek = DayOfWeek.of(if (i > 1) (i - 1) else 7)

		/**
		 * 获取某个日期那一周的周一
		 * *以周一为一周起始计算*
		 */
		fun getMondayOfWeekFromDate(d: Date): Date = Calendar.getInstance().apply {
				time = d
				set(Calendar.HOUR_OF_DAY, 0)
				set(Calendar.MINUTE, 0)
				set(Calendar.SECOND, 0)
				set(Calendar.MILLISECOND, 0)
				val dow = get(Calendar.DAY_OF_WEEK)
				add(Calendar.DATE, -1 * (convertCalendarDowToDayOfWeek(dow).value - DayOfWeek.MONDAY.value))
			}.time

		/**
		 * 对 Date 加上（或减去）周数
		 */
		fun addWeekFromDate(d: Date, diff: Int): Date = Calendar.getInstance().apply {
			time = d
			add(Calendar.DATE, diff * 7)
		}.time

		/**
		 * 对 Date 加上（或减去）天数
		 */
		fun addDayFromDate(d: Date, diff: Int): Date = Calendar.getInstance().apply {
			time = d
			add(Calendar.DATE, diff)
		}.time

		/**
		 * 两个 Date 之间的周差
		 */
		fun diffWeekBetweenDates(d1: Date, d2: Date): Int {
			val a = getMondayOfWeekFromDate(d1).time
			val b = getMondayOfWeekFromDate(d2).time
			return ((b - a) / (7 * 24 * 60 * 60 * 1000).toDouble()).roundToInt()
		}

		/**
		 * Date 转换为 String
		 * yyyy-MM-dd
		 */
		@SuppressLint("SimpleDateFormat")
		fun dateToString(d: Date): String = SimpleDateFormat("yyyy-MM-dd").format(d)

		/**
		 * String 转换为 Date
		 * yyyy-MM-dd
		 */
		@SuppressLint("SimpleDateFormat")
		fun stringToDate(s: String): Date = SimpleDateFormat("yyyy-MM-dd").parse(s)!!
	}
}