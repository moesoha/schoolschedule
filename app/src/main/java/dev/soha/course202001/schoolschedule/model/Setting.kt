package dev.soha.course202001.schoolschedule.model

import androidx.room.*

/** 应用设置保存 */
@Entity(tableName = "setting")
data class Setting(
	/** 设置项 键 */
	@PrimaryKey
	@ColumnInfo(name="key")
	val key: String,

	/** 设置项 值 */
	@ColumnInfo(name="value")
	var value: String
)
