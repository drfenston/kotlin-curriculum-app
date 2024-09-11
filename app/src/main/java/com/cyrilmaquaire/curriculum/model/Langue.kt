package com.cyrilmaquaire.curriculum.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Langue (
    val id: Long,
    var origine: String = "",
    var niveau: String = "",
    var percent: Long = 0,
    val created: String,

    @SerialName("CVId")
    val cvID: Long
)