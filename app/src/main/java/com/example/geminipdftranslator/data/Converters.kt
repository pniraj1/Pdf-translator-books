package com.example.geminipdftranslator.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun toStatus(value: String): TranslationStatus = TranslationStatus.valueOf(value)

    @TypeConverter
    fun fromStatus(value: TranslationStatus): String = value.name
}
