package dev.soha.course202002.schedule.android.data

import androidx.room.*

@Dao interface SettingDao {
	@Query("INSERT OR IGNORE INTO setting (`key`, value) VALUES (:key, :value)")
	suspend fun insertOrIgnore(key: String, value: String)

	@Query("UPDATE setting SET value = :value WHERE `key` = :key")
	suspend fun update(key: String, value: String)

	@Query("DELETE FROM setting WHERE `key` = :key")
	suspend fun delete(key: String)

	@Query("SELECT value FROM setting WHERE `key` = :key")
	suspend fun get(key: String): String?

	@Transaction
	suspend fun set(key: String, value: String?) {
		if (value == null) {
			delete(key)
		} else {
			insertOrIgnore(key, value)
			update(key, value)
		}
	}
}
