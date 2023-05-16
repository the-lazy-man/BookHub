package com.example.mybooks.database

import androidx.room.Database
import androidx.room.RoomDatabase

class BookDatabase {
    @Database(entities = [BookEntity::class], version = 1)
    abstract  class BookDatabase: RoomDatabase(){
        abstract  fun bookDao() : BookDao
    }
}