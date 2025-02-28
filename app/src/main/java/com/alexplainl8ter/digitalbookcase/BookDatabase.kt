package com.alexplainl8ter.digitalbookcase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Book::class], version = 1, exportSchema = false) // Define entities, version, and schema export
abstract class BookDatabase : RoomDatabase() { // Abstract class extending RoomDatabase
    abstract fun bookDao(): BookDao // Abstract function to get BookDao instance

    companion object { // Singleton pattern to ensure only one instance of the database
        @Volatile
        private var Instance: BookDatabase? = null

        fun getDatabase(context: Context): BookDatabase {
            // if Instance is not null, then return it,
            // if it is null, then create the database
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    BookDatabase::class.java,
                    "book_database"
                ).fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}