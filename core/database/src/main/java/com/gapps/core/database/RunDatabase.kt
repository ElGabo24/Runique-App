package com.gapps.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gapps.core.database.dao.AnalyticsDao
import com.gapps.core.database.dao.RunDao
import com.gapps.core.database.dao.RunPendingSyncDao
import com.gapps.core.database.entity.DeletedRunSyncEntity
import com.gapps.core.database.entity.RunEntity
import com.gapps.core.database.entity.RunPendingSyncEntity

@Database(
    entities = [
        RunEntity::class,
        RunPendingSyncEntity::class,
        DeletedRunSyncEntity::class
    ], version = 1
)
abstract class RunDatabase : RoomDatabase() {

    abstract val runDao: RunDao
    abstract val runPendingSyncDao: RunPendingSyncDao
    abstract val analyticsDao: AnalyticsDao 

}