package com.jakebarnby.camdroid.text.extensions

import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.mlkit.nl.languageid.IdentifiedLanguage
import com.google.mlkit.vision.text.Text
import com.jakebarnby.camdroid.models.DetectedText
import com.jakebarnby.camdroid.models.TextBox
import com.jakebarnby.camdroid.models.TextElement
import com.jakebarnby.camdroid.models.TextLine

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

    fun FirebaseVisionText.toDetectedText() =
        DetectedText().apply {
            text = this@toDetectedText.text
            textBoxes = this@toDetectedText.textBlocks.map { block ->
                TextBox().apply {
                    text = block.text
                    confidence = block.confidence
                    boundingBox = block.boundingBox
                    cornerPoints = block.cornerPoints?.toList()
                    detectedLanguages = block.recognizedLanguages.map {
                        it.languageCode ?: ""
                    }
                    lines = block.lines.map { line ->
                        TextLine().apply {
                            text = line.text
                            confidence = line.confidence
                            boundingBox = line.boundingBox
                            cornerPoints = line.cornerPoints?.toList()
                            detectedLanguages = block.recognizedLanguages.map {
                                it.languageCode ?: ""
                            }
                            elements = line.elements.map { element ->
                                TextElement().apply {
                                    text = element.text
                                    confidence = element.confidence
                                    boundingBox = element.boundingBox
                                    cornerPoints = element.cornerPoints?.toList()
                                    detectedLanguages = block.recognizedLanguages.map {
                                        it.languageCode ?: ""
                                    }
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