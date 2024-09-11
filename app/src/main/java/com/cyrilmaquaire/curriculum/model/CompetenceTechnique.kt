package com.cyrilmaquaire.curriculum.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompetenceTechnique (
    val id: Long,
    var icon: String = "",
    var libelle: String = "",
    var competence: String = "",
    var percent: Long = 0,
    val created: String,

    @SerialName("CVId")
    val cvID: Long
)