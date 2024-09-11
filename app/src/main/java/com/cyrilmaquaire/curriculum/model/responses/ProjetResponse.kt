package com.cyrilmaquaire.curriculum.model.responses

import com.cyrilmaquaire.curriculum.model.Projet
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class ProjetResponse: Response() {
    @SerializedName("data")
    @Expose
    var data: Projet? = null
}