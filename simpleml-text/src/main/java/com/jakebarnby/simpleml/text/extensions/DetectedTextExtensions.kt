package com.jakebarnby.simpleml.text.extensions

import com.google.mlkit.nl.languageid.IdentifiedLanguage
import com.google.mlkit.vision.text.Text
import com.jakebarnby.simpleml.models.text.DetectedText
import com.jakebarnby.simpleml.models.text.TextBox
import com.jakebarnby.simpleml.models.text.TextElement
import com.jakebarnby.simpleml.models.text.TextLine

object DetectedTextExtensions {

    fun Text.toDetectedText() =
        DetectedText().apply {
            text = this@toDetectedText.text
            textBoxes = this@toDetectedText.textBlocks.map { block ->
                TextBox().apply {
                    text = block.text
                    boundingBox = block.boundingBox
                    cornerPoints = block.cornerPoints?.toList()
                    detectedLanguages = listOf(block.recognizedLanguage)
                    lines = block.lines.map { line ->
                        TextLine().apply {
                            text = line.text
                            boundingBox = line.boundingBox
                            cornerPoints = line.cornerPoints?.toList()
                            detectedLanguages = listOf(block.recognizedLanguage)
                            elements = line.elements.map { element ->
                                TextElement().apply {
                                    text = element.text
                                    boundingBox = element.boundingBox
                                    cornerPoints = element.cornerPoints?.toList()
                                    detectedLanguages = listOf(block.recognizedLanguage)
                                }
                            }
                        }
                    }
                }
            }
        }

    fun List<IdentifiedLanguage>.toDetectedText(text: String) =
        DetectedText().apply {
            this.text = text
            this.detectedLanguages = map {
                it.languageTag
            }
        }
}