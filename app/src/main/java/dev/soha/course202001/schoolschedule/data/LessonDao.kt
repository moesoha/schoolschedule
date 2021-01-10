package dev.soha.course202001.schoolschedule.data

import androidx.room.*
import dev.soha.course202001.schoolschedule.model.Lesson

@Dao interface LessonDao {
	@Insert suspend fun insert(note: Lesson): Long
	@Update suspend fun update(note: Lesson)
	@Delete suspend fun delete(note: Lesson)
}
