package dev.soha.course202002.schedule.android.data

import android.content.Context
import androidx.room.*
import dev.soha.course202002.schedule.android.model.*
import dev.soha.course202002.schedule.android.data.TypeConverters as LocalTypeConverters

@Database(entities=[Setting::class, Lesson::class], version=2, exportSchema=false)
@TypeConverters(LocalTypeConverters::class)
abstract class AppDatabase: RoomDatabase() {
	companion object {
		private var INSTANCE: AppDatabase? = null
		private const val FILENAME = "schedule"

		fun getDatabase(context: Context): AppDatabase {
			if (INSTANCE == null) {
				synchronized(AppDatabase::class.java) {
					if (INSTANCE == null) {
						INSTANCE = Room
							.databaseBuilder(context.applicationContext, AppDatabase::class.java, FILENAME)
							.build()
					}
				}
			}
			return INSTANCE!!
		}
	}

	abstract fun settingDao(): SettingDao
	abstract fun lessonDao(): LessonDao
}