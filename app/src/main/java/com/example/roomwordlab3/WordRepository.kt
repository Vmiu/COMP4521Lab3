package com.example.roomwordlab3

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class WordRepository(private val wordDao: WordDao) {
    val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()
    @WorkerThread
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }
    suspend fun deleteAll() {
        wordDao.deleteAll()
    }
    suspend fun deleteWord(word: String) {
        wordDao.deleteWord(word)
    }
}