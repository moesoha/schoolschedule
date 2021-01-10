package dev.soha.course202001.schoolschedule.data

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.soha.course202001.schoolschedule.model.Lesson

@Dao interface LessonDao {
	@Query("SELECT * FROM lesson")
	fun getAllLessons(): LiveData<List<Lesson>>

	@Insert suspend fun insert(entity: Lesson): Long
	@Update suspend fun update(entity: Lesson)
	@Delete suspend fun delete(entity: Lesson)
}
