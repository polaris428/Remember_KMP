package com.devlog.remember.model

import kotlinx.serialization.Serializable

@Serializable
data class Word(
    val term: String,
    val meaning: String,
    val example: String? = null
)