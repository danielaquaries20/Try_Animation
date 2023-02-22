package com.example.tryanimation.try_architecture_code.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tryanimation.try_architecture_code.database.user.UserDao
import com.example.tryanimation.try_architecture_code.database.user.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false)
abstract class MyDatabaseTry : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        @Volatile
        private var INSTANCE: MyDatabaseTry? = null

        fun getMyDatabaseTry(context: Context): MyDatabaseTry {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabaseTry::class.java,
                    "my_database_try"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}