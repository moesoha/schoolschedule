package dev.soha.course202002.schedule.web.data

import org.apache.ibatis.type.JdbcType
import org.apache.ibatis.type.MappedJdbcTypes
import org.apache.ibatis.type.MappedTypes
import org.apache.ibatis.type.TypeHandler
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

//@MappedJdbcTypes(JdbcType.VARCHAR)
//@MappedTypes(List<Int>::class)
class ListIntHandler: TypeHandler<List<Int>> {
	private fun langToDatabase(value: List<Int>): String = value.joinToString(",")
	private fun databaseToLang(value: String): List<Int> = value.split(',').map { it.toInt() }

	override fun setParameter(ps: PreparedStatement?, i: Int, parameter: List<Int>?, jdbcType: JdbcType?) {
		ps?.setString(i, parameter?.let { langToDatabase(it) } ?: "")
	}

	override fun getResult(rs: ResultSet?, columnName: String?): List<Int>
		= columnName?.let { rs?.let { rs -> databaseToLang(rs.getString(it)) } } ?: listOf()

	override fun getResult(rs: ResultSet?, columnIndex: Int): List<Int>
		= rs?.let { rs -> databaseToLang(rs.getString(columnIndex)) } ?: listOf()

	override fun getResult(cs: CallableStatement?, columnIndex: Int): List<Int>
		= cs?.let { cs -> databaseToLang(cs.getString(columnIndex)) } ?: listOf()

}