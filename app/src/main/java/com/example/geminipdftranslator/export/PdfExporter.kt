package com.example.geminipdftranslator.export

import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import java.io.File
import java.io.FileOutputStream

class PdfExporter {
    fun exportParagraphs(paragraphs: List<String>, outputFile: File) {
        val document = PdfDocument()
        val pageWidth = 595
        val pageHeight = 842
        val margin = 40
        val lineHeight = 20
        val paint = Paint().apply { textSize = 12f }

        var pageNumber = 1
        var page = document.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())
        var canvas = page.canvas
        var y = margin

        for (paragraph in paragraphs) {
            val words = paragraph.split(Regex("""\s+"""))
            var line = ""
            for (word in words) {
                val candidate = if (line.isEmpty()) word else "$line $word"
                if (paint.measureText(candidate) > pageWidth - margin * 2) {
                    canvas.drawText(line, margin.toFloat(), y.toFloat(), paint)
                    y += lineHeight
                    line = word
                } else {
                    line = candidate
                }

                if (y > pageHeight - margin) {
                    if (line.isNotEmpty()) {
                        canvas.drawText(line, margin.toFloat(), y.toFloat(), paint)
                        line = ""
                    }
                    document.finishPage(page)
                    pageNumber += 1
                    page = document.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())
                    canvas = page.canvas
                    y = margin
                }
            }
            if (line.isNotEmpty()) {
                canvas.drawText(line, margin.toFloat(), y.toFloat(), paint)
                y += lineHeight
            }
            y += lineHeight
        }

        document.finishPage(page)
        FileOutputStream(outputFile).use { document.writeTo(it) }
        document.close()
    }
}
