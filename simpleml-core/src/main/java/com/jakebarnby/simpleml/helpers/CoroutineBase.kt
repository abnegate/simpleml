package com.jakebarnby.simpleml.helpers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

interface CoroutineBase : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Main + job

    val job: Job
}