package com.devlog.remember.utils

import android.speech.tts.TextToSpeech
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.util.*

private class AndroidTts(private val tts: TextToSpeech) : Tts {
    override fun speak(text: String) {
        if (text.isNotBlank()) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "word-$text")
        }
    }
}

@Composable
actual fun rememberPlatformTts(): Tts {
    val context = LocalContext.current
    var engine by remember { mutableStateOf<TextToSpeech?>(null) }
    var ttsEngine: TextToSpeech? = null
    DisposableEffect(context) {
        ttsEngine = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // ✅ 여기서 바로 engine 업데이트
                ttsEngine!!.language = Locale.US
                engine = ttsEngine
            }
        }

        onDispose {
            ttsEngine.stop()
            ttsEngine.shutdown()
        }
    }

    return engine?.let { AndroidTts(it) } ?: NoopTts
}
