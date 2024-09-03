package com.cyrilmaquaire.curriculum.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Langue (
    val id: Long,
    val origine: String,
    val niveau: String,
    val percent: Long,
    val created: String,

    @SerialName("CVId")
    val cvID: Long
)