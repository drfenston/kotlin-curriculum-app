package com.cyrilmaquaire.curriculum.model.responses

import com.cyrilmaquaire.curriculum.model.Formation
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class FormationResponse: Response() {
    @SerializedName("data")
    @Expose
    var data: Formation? = null
}