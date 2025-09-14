// shared/src/commonMain/kotlin/com/devlog/remember/tts/Tts.kt
package com.devlog.remember.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

interface Tts {
    fun speak(text: String)
}

object NoopTts : Tts {
    override fun speak(text: String) { /* no-op */ }
}

val LocalTts = staticCompositionLocalOf<Tts> { NoopTts }

// 각 플랫폼에서 구현
@Composable
expect fun rememberPlatformTts(): Tts
