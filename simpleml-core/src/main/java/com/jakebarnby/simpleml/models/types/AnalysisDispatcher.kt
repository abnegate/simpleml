package com.jakebarnby.simpleml.models.types

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

enum class AnalysisDispatcher(
    val dispatch: CoroutineDispatcher,
    val idx: Int,
) {
    IO(Dispatchers.IO, 0),
    Main(Dispatchers.Main, 1),
    Default(Dispatchers.Default, 2),
    Unconfined(Dispatchers.Unconfined, 3),
}