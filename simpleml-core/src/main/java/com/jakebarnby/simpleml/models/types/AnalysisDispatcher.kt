package com.jakebarnby.simpleml.models.types

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

enum class AnalysisDispatcher(val coroutineDispatcher: CoroutineDispatcher) {
    IO(Dispatchers.IO),
    Main(Dispatchers.Main),
    Default(Dispatchers.Default),
    Unconfined(Dispatchers.Unconfined),
}