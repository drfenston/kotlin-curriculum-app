package com.cyrilmaquaire.curriculum.model.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class UploadPhotoResponse : Response() {
    @SerializedName("data")
    @Expose
    var data: Data? = null

    @Serializable
    data class Data(
        @SerializedName("fieldname") var fieldname: String? = null,
        @SerializedName("originalname") var originalname: String? = null,
        @SerializedName("encoding") var encoding: String? = null,
        @SerializedName("mimetype") var mimetype: String? = null,
        @SerializedName("destination") var destination: String? = null,
        @SerializedName("filename") var filename: String? = null,
        @SerializedName("path") var path: String? = null,
        @SerializedName("size") var size: Int? = null
    )
}

