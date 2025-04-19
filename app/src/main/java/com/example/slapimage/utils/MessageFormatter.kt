package com.example.slapimage.utils

import android.text.Spanned
import android.text.SpannableStringBuilder
import androidx.core.text.HtmlCompat
import android.text.SpannableString

object MessageFormatter {
    fun formatMessage(text: String): Spanned {
        return try {
            // First apply code spans with proper validation
            val withCodeSpans = applyCodeSpans(text)
            // Then convert remaining markdown to HTML
            HtmlCompat.fromHtml(
                markdownToHtml(withCodeSpans.toString()),
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
        } catch (_: Exception) {
            // Fallback to plain text if formatting fails
            SpannableString(text)
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
        var html = text
            // Handle bold/italic
            .replace("\\*\\*([^*]+)\\*\\*".toRegex(), "<strong>$1</strong>")
            .replace("\\*([^*]+)\\*".toRegex(), "<em>$1</em>")
            // Handle lists
            .replace("- (.*)".toRegex(), "• $1<br>")
            // Handle links
            .replace("\\[([^]]+)]\\(([^)]+)\\)".toRegex(),
                "<a href=\"$2\">$1</a>")

        return html
    }
}