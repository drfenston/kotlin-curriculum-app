package com.cyrilmaquaire.curriculum.model.responses

import com.cyrilmaquaire.curriculum.model.CompetenceTechnique
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class CompTechResponse: Response() {
    @SerializedName("data")
    @Expose
    var data: CompetenceTechnique? = null
}