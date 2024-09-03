package com.cyrilmaquaire.curriculum.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Formation (
    val id: Long,
    val dateDebut: String,
    val dateFin: String,
    val etablissement: String,
    val description: String,
    val created: String,

    @SerialName("CVId")
    val cvID: Long
)