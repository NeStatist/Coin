package com.example.coin.net

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyCrypto {
    @GET("{coin}-usd")
    fun getCoin(@Path("coin") coin: String?): Observable<Crypto?>?

    companion object {
        const val BASE_URL = "https://api.cryptonator.com/api/full/btc-usd/"
    }
}