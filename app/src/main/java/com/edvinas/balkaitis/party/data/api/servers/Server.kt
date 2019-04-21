package com.edvinas.balkaitis.party.data.api.servers

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Server(
    @SerializedName("name") val country: String,
    val distance: String
) : Parcelable
