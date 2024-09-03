package com.cyrilmaquaire.curriculum.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Experience (
    val id: Long,
    val dateDebut: String,
    val dateFin: String,
    val entreprise: String,
    val poste: String,
    val created: String,

    @SerialName("CVId")
    val cvID: Long,

    val projets: List<Projet>
)