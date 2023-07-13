package com.example.tryanimation.ui.kl_basic.chapter_12.data.test_data.room_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [TryFriendEntity::class],
    version = 2,
    exportSchema = false
)
abstract class TryMyDatabase : RoomDatabase() {

    abstract fun tryFriendDao(): TryFriendDao

    companion object {

        @Volatile
        private var INSTANCE: TryMyDatabase? = null

        @Synchronized
        fun getInstance(context: Context): TryMyDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            } else {
                val instance = Room.databaseBuilder(context.applicationContext,
                    TryMyDatabase::class.java,
                    "try_database")
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}