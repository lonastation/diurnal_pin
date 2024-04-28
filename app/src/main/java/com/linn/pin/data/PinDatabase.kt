package com.linn.pin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Ding::class], version = 1, exportSchema = false)
abstract class PinDatabase : RoomDatabase() {
    abstract fun dingDao(): DingDao

    abstract fun girthDao(): GirthDao

    companion object {
        @Volatile
        private var Instance: PinDatabase? = null
        fun getDatabase(context: Context): PinDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, PinDatabase::class.java, "pin_database")
                    .build().also { Instance = it }
            }
        }
    }
}