package dev.soha.course202002.schedule.web.data

import org.apache.ibatis.annotations.*
import org.apache.ibatis.type.JdbcType

interface LessonMapper {
//	@Results(Result(property = "session", column = "session", typeHandler = ListIntHandler::class, jdbcType = JdbcType.VARCHAR))
	@Select("SELECT * FROM `lesson` WHERE `id`=#{id};")
	fun findOne(@Param("id") id: Int): Lesson?

	@Select("SELECT * FROM `lesson` WHERE `username`=#{username};")
	fun findByUsername(@Param("username") username: String): List<Lesson>

	@Insert("INSERT INTO `lesson` (`username`, `name`, `teacher`, `place`, `day`, `session`, `week_start`, `week_end`, `week_type`) VALUES (#{o.username}, #{o.name}, #{o.teacher}, #{o.place}, #{o.day}, #{o.session}, #{o.weekStart}, #{o.weekEnd}, #{o.weekType});")
	fun insert(@Param("o") lesson: Lesson)

	@Update("UPDATE `lesson` SET `username`=#{o.username}, `name`=#{o.name}, `teacher`=#{o.teacher}, `place`=#{o.place}, `day`=#{o.day}, `session`=#{o.session}, `week_start`=#{o.weekStart}, `week_end`=#{o.weekEnd}, `week_type`=#{o.weekType} WHERE `id`=#{o.id};")
	fun update(@Param("o") lesson: Lesson)

	@Delete("DELETE FROM `lesson` WHERE `id`=#{o.id};")
	fun delete(@Param("o") lesson: Lesson)
}
