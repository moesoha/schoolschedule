package dev.soha.course202001.schoolschedule.data

import androidx.room.*
import dev.soha.course202001.schoolschedule.model.Setting

@Dao interface SettingDao {
	@Insert suspend fun insert(note: Setting): Long
	@Update suspend fun update(note: Setting)
	@Delete suspend fun delete(note: Setting)
}
