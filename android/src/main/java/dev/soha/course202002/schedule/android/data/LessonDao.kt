package dev.soha.course202002.schedule.android.data

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.soha.course202002.schedule.android.model.Lesson
import java.time.DayOfWeek

@Dao interface LessonDao {
	@Query("SELECT * FROM lesson")
	fun getAllLessons(): LiveData<List<Lesson>>

	@Query("SELECT * FROM lesson WHERE day = :dayOfWeek")
	fun getLessonsByDay(dayOfWeek: DayOfWeek): LiveData<List<Lesson>>

	@Query("SELECT * FROM lesson WHERE _id = :id")
	fun find(id: Long): LiveData<Lesson>

	@Query("DELETE FROM lesson WHERE 1=1")
	suspend fun removeAllImportedLessons()

	@Insert suspend fun insert(entity: Lesson): Long
	@Update suspend fun update(entity: Lesson)
	@Delete suspend fun delete(entity: Lesson)
}
