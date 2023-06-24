package com.example.tryanimation.ui.kl_basic.chapter_12.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [FriendEntity::class],
    version = 1
)
abstract class MyDatabase : RoomDatabase() {

    abstract fun friendDao(): FriendDao

    companion object {

        @Volatile
        private var INSTANCE: MyDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): MyDatabase {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }

            val instance = Room.databaseBuilder(context.applicationContext,
                MyDatabase::class.java,
                "my_database")
                .fallbackToDestructiveMigration()
                .build()

            INSTANCE = instance
            return instance
        }
    }
}