package com.example.coin.net

import com.google.gson.annotations.SerializedName

class Crypto {
    @SerializedName("ticker")
    var ticker: Ticker? = null
    @SerializedName("timestamp")
    var timestamp: Int? = null
    @SerializedName("success")
    var success: Boolean? = null
    @SerializedName("error")
    var error: String? = null
    var market: Market?= null

     class Market {
        @SerializedName("market")
        var market: String? = null
        @SerializedName("price")
        var price: String? = null
        @SerializedName("volume")
        var volume: Float? = null
        var coinName: String? = null
    }

   class Ticker {
        @SerializedName("base")
        var base: String? = null
        @SerializedName("target")
        var target: String? = null
        @SerializedName("price")
        var price: String? = null
        @SerializedName("volume")
        var volume: String? = null
        @SerializedName("change")
        var change: String? = null
        @SerializedName("markets")
        var markets: List<Market>? = null
    }
}