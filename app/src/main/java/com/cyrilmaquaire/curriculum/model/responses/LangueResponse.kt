package com.cyrilmaquaire.curriculum.model.responses

import com.cyrilmaquaire.curriculum.model.Langue
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class LangueResponse: Response() {
    @SerializedName("data")
    @Expose
    var data: Langue? = null
}