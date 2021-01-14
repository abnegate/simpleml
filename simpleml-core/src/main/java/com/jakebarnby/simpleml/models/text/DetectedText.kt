package com.jakebarnby.simpleml.models.text

import com.jakebarnby.simpleml.models.ResultBase

class DetectedText(
    var text: String? = null,
    var detectedLanguages: List<String>? = null,
    var textBoxes: List<TextBox>? = null
) : ResultBase()

