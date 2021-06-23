package dev.soha.course202002.schedule.android.model

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
) {
	companion object {
		/** 本地缓存的教务系统用户名 */
		const val OA_USERNAME = "oa_username"
		/** 本地缓存的教务系统密码 */
		const val OA_PASSWORD = "oa_password"

		/** 目前可用的 token */
		const val OA_TOKEN = "oa_token"
		/** 目前可用的 token 已经登入的用户名 */
		const val OA_TOKEN_ASSOCIATED_USERNAME = "oa_token_associated_username"
		/** 目前可用的 token 的有效期，时间戳 */
		const val OA_TOKEN_VALID_BEFORE = "oa_token_valid_before"

		/** 第一周的周日的时间戳，用来计算当前周 */
		const val DISPLAY_FIRST_WEEK_MONDAY = "display_first_monday"
	}
}
