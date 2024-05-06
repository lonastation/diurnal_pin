package com.linn.pin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Work::class, Girth::class], version = 1, exportSchema = false)
@TypeConverters(DateTimeConverter::class)
abstract class PinDatabase : RoomDatabase() {
    abstract fun workDao(): WorkDao

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