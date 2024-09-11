package com.cyrilmaquaire.curriculum.model.responses

import com.cyrilmaquaire.curriculum.model.Autre
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class AutreResponse: Response() {
    @SerializedName("data")
    @Expose
    var data: Autre? = null
}