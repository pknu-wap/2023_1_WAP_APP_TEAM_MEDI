package com.android.mediproject.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.mediproject.core.database.searchhistory.SearchHistoryDao
import com.android.mediproject.core.database.searchhistory.SearchHistoryDto


@Database(entities = [SearchHistoryDto::class], version = 1, exportSchema = true)
@TypeConverters(RoomTypeConverter::class)
abstract class RoomDB : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
}