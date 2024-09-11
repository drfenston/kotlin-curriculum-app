package com.cyrilmaquaire.curriculum.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Experience (
    val id: Long,
    var dateDebut: String = "",
    var dateFin: String = "",
    var entreprise: String = "",
    var poste: String = "",
    val created: String,

    @SerialName("CVId")
    val cvID: Long,

    val projets: List<Projet>
)