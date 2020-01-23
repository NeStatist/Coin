package com.example.coin.net

import io.reactivex.Observable
import retrofit2.http.GET


interface CryptoNames {
    @GET("currencies")
    fun getNames (): Observable<NameCrypto.Rows>

    companion object {
        const val BASE_URL = "https://www.cryptonator.com/api/"
    }
}