package com.jakebarnby.camdroid.helpers

import android.os.Binder

data class BindWrapper<T>(val data: T) : Binder()