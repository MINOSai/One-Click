package com.minosai.oneclick.ui.model

import com.google.gson.annotations.SerializedName

data class Payload (
        @SerializedName("userId") val userId: String,
        @SerializedName("password") val password: String
) {
    @SerializedName("serviceName") val serviceName = ""
    @SerializedName("Submit22") val submit22 = ""
}