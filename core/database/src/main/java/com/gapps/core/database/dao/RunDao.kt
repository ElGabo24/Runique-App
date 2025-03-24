package com.gapps.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.gapps.core.database.entity.RunEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {

    @Upsert
    suspend fun updateRun(run: RunEntity)

    @Upsert
    suspend fun updateRuns(runs: List<RunEntity>)

    @Query("SELECT * FROM RunEntity ORDER BY dateTimeUtc Desc")
    fun getRuns(): Flow<List<RunEntity>>

    @Query("DELETE FROM RunEntity WHERE id = :id")
    suspend fun deleteRun(id: String)

    @Query("DELETE FROM RunEntity")
    suspend fun deleteAllRuns()
}