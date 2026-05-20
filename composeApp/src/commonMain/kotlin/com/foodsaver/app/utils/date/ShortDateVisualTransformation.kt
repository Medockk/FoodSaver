package com.foodsaver.app.utils.date

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

internal class ShortDateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // максимум 6 символов - мм/уууу
        val trimmed = if (text.text.length >= 6) text.text.substring(0..5) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 1) out += "/" // слеш после второго символа
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 6) return offset + 1
                return 7
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 7) return offset - 1
                return 6
            }
        }

        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}