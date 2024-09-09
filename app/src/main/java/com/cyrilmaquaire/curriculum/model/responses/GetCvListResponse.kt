package com.cyrilmaquaire.curriculum.model.responses

import com.cyrilmaquaire.curriculum.model.CV
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class GetCvListResponse : Response() {
    @SerializedName("data")
    @Expose
    var data: List<CV> = listOf()
}