package com.example.coin.net

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class NameCrypto (

    @SerializedName("code") val code : String,
    @SerializedName("name") val name : String,
    @SerializedName("statuses") val statuses : List<String>
) : Parcelable {
    class Rows (val rows: List<NameCrypto>)
}