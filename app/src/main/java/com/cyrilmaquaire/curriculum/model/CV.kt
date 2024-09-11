package com.cyrilmaquaire.curriculum.model

import kotlinx.serialization.Serializable

@Serializable
data class CV (
    val id: Long,
    var poste: String,
    var profileImage: String,
    var description: String,
    var nom: String,
    var prenom: String,
    var adresse1: String,
    var adresse2: String,
    var city: String,
    var zipCode: String,
    var telephone: String,
    var apropos: String,
    var mail: String,
    var website: String,
    var reseaux: String,
    var created: String,
    var formations: MutableList<Formation>,
    var langues: MutableList<Langue>,
    var autres: MutableList<Autre>,
    var competenceTechniques: MutableList<CompetenceTechnique>,
    var experiences: MutableList<Experience>
)