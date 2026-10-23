package com.example.geminipdftranslator.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TranslationDao {
    @Insert
    suspend fun insertBook(book: BookEntity): Long

    @Insert
    suspend fun insertChapters(chapters: List<ChapterEntity>): List<Long>

    @Insert
    suspend fun insertChunks(chunks: List<ChunkEntity>)

    @Update
    suspend fun updateChunk(chunk: ChunkEntity)

    @Query("SELECT * FROM chunks WHERE status != 'COMPLETED' ORDER BY id LIMIT 1")
    suspend fun getNextIncompleteChunk(): ChunkEntity?
}
