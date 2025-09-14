// shared/src/iosMain/kotlin/com/devlog/remember/tts/Tts.ios.kt
package com.devlog.remember.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.AVFAudio.AVSpeechSynthesisVoice
import platform.AVFAudio.AVSpeechSynthesizer
import platform.AVFAudio.AVSpeechUtterance

private class IosTts : Tts {
    private val synth = AVSpeechSynthesizer()
    override fun speak(text: String) {
        if (text.isNotBlank()) {
            val utterance = AVSpeechUtterance.speechUtteranceWithString(text)
            utterance.voice = AVSpeechSynthesisVoice.voiceWithLanguage("en-US")
            synth.speakUtterance(utterance)
        }
    }
}

@Composable
actual fun rememberPlatformTts(): Tts = remember { IosTts() }
