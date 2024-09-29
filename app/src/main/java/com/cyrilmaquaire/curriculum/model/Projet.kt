package com.cyrilmaquaire.curriculum.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Projet (
    val id: Long,
    var nom: String?,
    var description: String?,
    val created: String,

    @SerialName("experienceId")
    val experienceID: Long
)