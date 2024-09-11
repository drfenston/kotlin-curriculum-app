package com.cyrilmaquaire.curriculum.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Formation (
    val id: Long,
    var dateDebut: String = "",
    var dateFin: String = "",
    var etablissement: String = "",
    var description: String = "",
    val created: String,

    @SerialName("CVId")
    val cvID: Long
)