package com.youtubekids.app.data.local.dao

import androidx.room.*
import com.youtubekids.app.data.local.entities.SettingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {

    @Query("SELECT * FROM settings WHERE key = :key")
    suspend fun getSetting(key: String): SettingEntity?

    @Query("SELECT * FROM settings WHERE key = :key")
    fun getSettingFlow(key: String): Flow<SettingEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetting(setting: SettingEntity)

    @Query("DELETE FROM settings WHERE key = :key")
    suspend fun deleteSetting(key: String)

    @Query("DELETE FROM settings")
    suspend fun deleteAllSettings()
}
