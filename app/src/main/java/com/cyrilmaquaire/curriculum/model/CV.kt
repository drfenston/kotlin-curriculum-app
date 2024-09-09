package com.cyrilmaquaire.curriculum.model

import kotlinx.serialization.Serializable

@Serializable
data class CV (
    val id: Long,
    val poste: String,
    val profileImage: String,
    val description: String,
    val nom: String,
    val prenom: String,
    val adresse1: String,
    val adresse2: String,
    val city: String,
    val zipCode: String,
    val telephone: String,
    val apropos: String,
    val mail: String,
    val website: String,
    val reseaux: String,
    val created: String,
    val formations: List<Formation>,
    val langues: List<Langue>,
    val autres: List<Autre>,
    val competenceTechniques: List<CompetenceTechnique>,
    val experiences: List<Experience>
)