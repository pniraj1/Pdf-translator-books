package com.example.geminipdftranslator.chunking

import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import java.io.File

class ChapterChunker {
    private val chapterRegex = Regex("""(?im)^\s*chapter\s+\d+.*$""")

    data class ChapterBlock(val title: String, val text: String)

    fun extractChapters(pdfFile: File): List<ChapterBlock> {
        val text = PDDocument.load(pdfFile).use { document ->
            PDFTextStripper().getText(document)
        }
        val matches = chapterRegex.findAll(text).toList()
        if (matches.isEmpty()) {
            return listOf(ChapterBlock("Chapter 1", text))
        }

        val chapters = mutableListOf<ChapterBlock>()
        for ((index, match) in matches.withIndex()) {
            val start = match.range.first
            val end = matches.getOrNull(index + 1)?.range?.first ?: text.length
            val content = text.substring(start, end).trim()
            chapters += ChapterBlock(match.value.trim(), content)
        }
        return chapters
    }

    fun splitIntoSubChunks(chapterText: String, maxWordsPerChunk: Int = 1500): List<String> {
        val paragraphs = chapterText.split(Regex("""\n\s*\n""")).map { it.trim() }.filter { it.isNotEmpty() }
        val chunks = mutableListOf<String>()
        val builder = StringBuilder()
        var runningWords = 0

        for (paragraph in paragraphs) {
            val paragraphWords = paragraph.split(Regex("""\s+""")).size
            if (runningWords > 0 && runningWords + paragraphWords > maxWordsPerChunk) {
                chunks += builder.toString().trim()
                builder.clear()
                runningWords = 0
            }
            if (builder.isNotEmpty()) builder.append("\n\n")
            builder.append(paragraph)
            runningWords += paragraphWords
        }

        if (builder.isNotEmpty()) chunks += builder.toString().trim()
        return chunks
    }
}
