package com.example.geminipdftranslator.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "chunks",
    foreignKeys = [
        ForeignKey(
            entity = ChapterEntity::class,
            parentColumns = ["id"],
            childColumns = ["chapterId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["chapterId", "chunkIndex"], unique = true)]
)
data class ChunkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val chapterId: Long,
    val chunkIndex: Int,
    val sourceText: String,
    val translatedText: String? = null,
    val summaryContext: String? = null,
    val status: TranslationStatus = TranslationStatus.PENDING,
    val retryCount: Int = 0
)
