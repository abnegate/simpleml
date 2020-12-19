package com.jakebarnby.camdroid.helpers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

interface CoroutineBase : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Main

    val job: Job
}