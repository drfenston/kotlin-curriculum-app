package com.cyrilmaquaire.curriculum.model.responses

import com.cyrilmaquaire.curriculum.model.Autre
import com.cyrilmaquaire.curriculum.model.CompetenceTechnique
import com.cyrilmaquaire.curriculum.model.Experience
import com.cyrilmaquaire.curriculum.model.Formation
import com.cyrilmaquaire.curriculum.model.Langue
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class GetCvResponse : Response() {

    @SerializedName("data")
    @Expose
    var data: Data? = null

    @Serializable
    data class Data (
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
}