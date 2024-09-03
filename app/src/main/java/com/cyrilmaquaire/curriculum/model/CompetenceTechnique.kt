package com.cyrilmaquaire.curriculum.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompetenceTechnique (
    val id: Long,
    val icon: String,
    val libelle: String,
    val competence: String,
    val percent: Long,
    val created: String,

    @SerialName("CVId")
    val cvID: Long
)