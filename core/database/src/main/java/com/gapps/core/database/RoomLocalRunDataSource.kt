package com.gapps.core.database

import android.database.sqlite.SQLiteFullException
import com.gapps.core.database.dao.RunDao
import com.gapps.core.database.mappers.toRun
import com.gapps.core.database.mappers.toRunEntity
import com.gapps.core.domain.run.LocalRunDataSource
import com.gapps.core.domain.run.Run
import com.gapps.core.domain.run.RunId
import com.gapps.core.domain.util.DataError
import com.gapps.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLocalRunDataSource(
    private val runDao: RunDao
): LocalRunDataSource {
    override fun getRuns(): Flow<List<Run>> {
        return runDao.getRuns()
            .map { runentities ->
                runentities.map { it.toRun() }
            }
    }

    override suspend fun upsertRun(run: Run): Result<RunId, DataError.Local> {
        return try {
            val entity = run.toRunEntity()
            runDao.updateRun(entity)
            Result.Success(entity.id)
        }catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun upsertRuns(runs: List<Run>): Result<List<RunId>, DataError.Local> {
        return try {
            val entities = runs.map { it.toRunEntity() }
            runDao.updateRuns(entities)
            Result.Success(entities.map { it.id })
        }catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteRun(id: String) {
        runDao.deleteRun(id)
    }

    override suspend fun deleteAllRuns() {
        runDao.deleteAllRuns()
    }
}