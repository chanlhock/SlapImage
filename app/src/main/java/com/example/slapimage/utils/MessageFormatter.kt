package com.example.slapimage.utils

import android.text.Spanned
import android.text.SpannableStringBuilder
import androidx.core.text.HtmlCompat
import android.text.SpannableString
import java.util.Locale

object MessageFormatter {
    fun formatMessage(text: String): Spanned {
        return try {
            // First apply code spans with proper validation
            val withCodeSpans = applyCodeSpans(text)
            // Then convert remaining markdown to HTML
            //HtmlCompat.fromHtml(
            //    markdownToHtml(withCodeSpans.toString()),
            //    HtmlCompat.FROM_HTML_MODE_COMPACT
           // )
            HtmlCompat.fromHtml(
                markdownToHtml(formatParagraphs(withCodeSpans.toString())),
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
        } catch (_: Exception) {
            // Fallback to plain text if formatting fails
            SpannableString(text)
        }
    }

    private fun formatParagraphs(text: String): String {
        // Split by paragraphs while preserving code blocks
        val paragraphs = mutableListOf<String>()
        val codeBlockRegex = """```.*?```""".toRegex()
        val codeBlocks = codeBlockRegex.findAll(text).toList()

        if (codeBlocks.isEmpty()) {
            // No code blocks - simple split
            return text.split("\n\n").joinToString("\n\n") {
                it.trim().capitalizeFirstLetter()
            }
        }

        // Process text with code blocks
        var lastEnd = 0
        codeBlocks.forEach { match ->
            // Add text before code block
            if (match.range.first > lastEnd) {
                val textBefore = text.substring(lastEnd, match.range.first)
                paragraphs.addAll(textBefore.split("\n\n").map { it.trim().capitalizeFirstLetter() })
            }
            // Add code block unchanged
            paragraphs.add(match.value)
            lastEnd = match.range.last + 1
        }
        // Add remaining text after last code block
        if (lastEnd < text.length) {
            val textAfter = text.substring(lastEnd)
            paragraphs.addAll(textAfter.split("\n\n").map { it.trim().capitalizeFirstLetter() })
        }

        return paragraphs.joinToString("\n\n")
    }

    private fun String.capitalizeFirstLetter(): String {
        return this.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault())
            else it.toString()
        }
    }

    private fun applyCodeSpans(text: String): SpannableStringBuilder {
        val builder = SpannableStringBuilder(text)
        val regex = "`([^`]+)`".toRegex()

        // Find all matches in reverse order to avoid offset issues
        regex.findAll(text).toList().reversed().forEach { matchResult ->
            val start = matchResult.range.first
            val end = matchResult.range.last + 1

            // Only apply span if the range is valid and not empty
            if (end > start) {
                builder.setSpan(
                    CodeSpan(),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                // Remove the backticks
                builder.delete(end - 1, end)
                builder.delete(start, start + 1)
            }
        }

        return builder
    }

    private fun markdownToHtml(text: String): String {
        // First preserve code blocks
        val codeBlockPlaceholder = "~~~CODE_BLOCK~~~"
        val codeBlocks = mutableListOf<String>()
        var processedText = text

        // Store and replace code blocks
        val codeBlockRegex = """```.*?```""".toRegex()
        codeBlockRegex.findAll(text).forEach { match ->
            codeBlocks.add(match.value)
            processedText = processedText.replace(match.value, codeBlockPlaceholder)
        }

        // Process markdown
        var html = processedText
            .replace("\\*\\*([^*]+)\\*\\*".toRegex(), "<strong>$1</strong>")
            .replace("\\*([^*]+)\\*".toRegex(), "<em>$1</em>")
            .replace("- (.*)".toRegex(), "• $1<br>")
            .replace("\\[([^]]+)]\\(([^)]+)\\)".toRegex(), "<a href=\"$2\">$1</a>")
            .replace("\n(?!\n)", " ")
            .replace(" +", " ")
            // extra
            .replace("\n\n", "<br><br>")
           // .replace(Regex("(?m)^-\\s+(.*)"), "• $1<br>") // Better multiline list support
            // In formatParagraphs()
            //.replace(Regex("(?<=\\w)([.!?])(?=\\s)"), "$1\n") // Add newline after sentences
            // Handle bullet points (both - and *)
            .replace(Regex("""(?m)^[*\-]\s+(.*)"""), "• $1<br>")  // Single level
            // Handle nested bullets (2+ spaces)
            .replace(Regex("""(?m)^\s{2,}[*\-]\s+(.*)"""), "&nbsp;&nbsp;• $1<br>")
            // Handle numbered lists
            .replace(Regex("""(?m)^\d+\.\s+(.*)"""), "$1<br>")

        // Restore code blocks
        codeBlocks.forEach { codeBlock ->
            html = html.replaceFirst(codeBlockPlaceholder, codeBlock)
        }

        return html
    }
}