// app/src/main/java/com/agustin/converter/ConverterApplication.kt
package com.agustin.converter

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp // remove this line if you’re not using Hilt
class ConverterApplication : Application()
