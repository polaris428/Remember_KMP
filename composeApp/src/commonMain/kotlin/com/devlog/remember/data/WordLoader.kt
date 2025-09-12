package com.devlog.remember.data


import com.devlog.remember.model.Word
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import remember.composeapp.generated.resources.Res

@OptIn(ExperimentalResourceApi::class)
suspend fun loadWords(): List<Word> {
    val json = Res.readBytes("files/words.json").decodeToString()

    return Json { ignoreUnknownKeys = true }
        .decodeFromString<List<Word>>(json)
}