package com.devlog.remember.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Word(
    val id: Int,
    val headword: String,
    val pos: String,
    val meaning: String,
    @SerialName("example (English)")
    val exampleEn: String,
    @SerialName("example (Korean)")
    val exampleKo: String,
    val level: Int
)
