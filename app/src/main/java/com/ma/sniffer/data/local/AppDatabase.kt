package com.ma.sniffer

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.ma.sniffer.data.local.NetworkUsageDao
import com.ma.sniffer.data.local.NetworkUsage

@Database(
    entities = [NetworkUsage::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun networkUsageDao(): NetworkUsageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sniffer_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}