package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.FamilyCharacter
import com.example.data.model.ScaleRecord

@Database(
    entities = [FamilyCharacter::class, ScaleRecord::class],
    version = 1,
    exportSchema = false
)
abstract class ScaleDatabase : RoomDatabase() {
    abstract fun scaleDao(): ScaleDao

    companion object {
        @Volatile
        private var INSTANCE: ScaleDatabase? = null

        fun getDatabase(context: Context): ScaleDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScaleDatabase::class.java,
                    "digital_scale_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
