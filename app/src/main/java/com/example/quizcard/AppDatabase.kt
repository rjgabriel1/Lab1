package com.example.quizcard

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yourpackage.packagenamehere.Flashcard
import com.yourpackage.packagenamehere.FlashcardDao

@Database(entities = [Flashcard::class], version = 1)

abstract class AppDatabase : RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao
}