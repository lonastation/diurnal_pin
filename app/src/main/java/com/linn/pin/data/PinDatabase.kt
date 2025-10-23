package com.linn.pin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.linn.pin.data.length.Length
import com.linn.pin.data.length.LengthDao
import com.linn.pin.data.work.Work
import com.linn.pin.data.work.WorkDao

@Database(entities = [Work::class, Length::class], version = 2, exportSchema = false)
@TypeConverters(DateTimeConverter::class)
abstract class PinDatabase : RoomDatabase() {
    abstract fun workDao(): WorkDao

    abstract fun lengthDao(): LengthDao

    companion object {
        @Volatile
        private var Instance: PinDatabase? = null
        fun getDatabase(context: Context): PinDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, PinDatabase::class.java, "pin_database")
                    .addMigrations(object : Migration(1, 2) {
                        override fun migrate(db: SupportSQLiteDatabase) {
                            db.execSQL("alter table girth rename to length;")
                            db.execSQL("alter table length rename column number1 to number;")
                            db.execSQL("alter table length drop column number2;")
                        }
                    })
                    .build().also { Instance = it }
            }
        }
    }
}