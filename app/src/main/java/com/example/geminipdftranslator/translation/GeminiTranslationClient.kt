package com.example.geminipdftranslator.translation

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.delay
import kotlin.math.min

class GeminiTranslationClient(
    private val apiKey: String,
    private val modelName: String = "gemini-1.5-flash"
) {
    suspend fun translateChunk(sourceText: String, targetLanguage: String, rollingSummary: String?): String {
        val model = GenerativeModel(modelName = modelName, apiKey = apiKey)
        val prompt = buildString {
            append("Translate the text into ")
            append(targetLanguage)
            append(". Keep meaning and style. Use natural paragraphs.\n")
            if (!rollingSummary.isNullOrBlank()) {
                append("Context summary from previous chunks:\n")
                append(rollingSummary)
                append("\n\n")
            }
            append("Text:\n")
            append(sourceText)
        }

        var attempts = 0
        var delayMs = 1000L
        while (true) {
            try {
                val response = model.generateContent(prompt)
                return response.text?.trim().orEmpty()
            } catch (t: Throwable) {
                attempts += 1
                if (attempts >= 5) throw t
                delay(delayMs)
                delayMs = min(delayMs * 2, 16_000L)
            }
        }
    }
}
