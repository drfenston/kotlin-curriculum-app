package com.cyrilmaquaire.curriculum.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Autre (
    val id: Long,
    val libelle: String,
    val description: String,
    val created: String,

    @SerialName("CVId")
    val cvID: Long
)