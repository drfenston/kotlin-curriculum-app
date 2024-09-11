package com.cyrilmaquaire.curriculum.model.responses

import com.cyrilmaquaire.curriculum.model.Experience
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class ExperienceResponse: Response() {
    @SerializedName("data")
    @Expose
    var data: Experience? = null
}