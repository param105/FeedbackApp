package com.param.feedbackapp.utils

import android.graphics.Path
import java.io.Serializable
import java.io.Writer

interface Action : Serializable {
    fun perform(path: Path)
    fun perform(writer: Writer)
}
