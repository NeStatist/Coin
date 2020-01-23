package com.example.coin.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coin.net.Crypto
import com.example.coin.net.CurrencyCrypto
import com.example.coin.R
import com.example.coin.adapter.RecyclerViewAdapter
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var retrofit: Retrofit? = null
    var recyclerViewAdapter: RecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerViewAdapter = RecyclerViewAdapter()
        recyclerView!!.adapter = recyclerViewAdapter
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val gson = GsonBuilder()
            .setLenient()
            .create()
        retrofit = Retrofit.Builder()
            .baseUrl(CurrencyCrypto.BASE_URL)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        callEndpoints()
    }

    @SuppressLint("CheckResult")
    private fun callEndpoints() {
        val cryptocurrency = retrofit!!.create(CurrencyCrypto::class.java)
        val btcObservable = cryptocurrency.getCoin("btc")
            ?.map { result: Crypto -> Observable.fromIterable(result.ticker!!.markets) }
            ?.flatMap { x: Observable<Crypto.Market>? -> x }!!.filter { y: Crypto.Market ->
            y.coinName = "Bitcoin"
            true
        }.toList().toObservable()
        val ethObservable = cryptocurrency.getCoin("eth")!!
            .map { result: Crypto -> Observable.fromIterable(result.ticker!!.markets) }
            .flatMap { x: Observable<Crypto.Market>? -> x }.filter { y: Crypto.Market ->
                y.coinName = "Ethereum"
                true
            }.toList().toObservable()
        val ltcObservable = cryptocurrency.getCoin("ltc")!!
            .map{result: Crypto-> Observable.fromIterable(result.ticker!!.markets)}
            .flatMap { x: Observable<Crypto.Market>? ->x }!!.filter{y:Crypto.Market ->
                y.coinName = "Litecoin"
        true
        }.toList().toObservable()
        val xrpObservable = cryptocurrency.getCoin("xrp")!!
            .map{result: Crypto-> Observable.fromIterable(result.ticker!!.markets)}
            .flatMap { x: Observable<Crypto.Market>? ->x }!!.filter{y:Crypto.Market ->
            y.coinName = "Ripple"
            true
        }.toList().toObservable()
        val xmrObservable = cryptocurrency.getCoin("xmr")!!
            .map{result: Crypto-> Observable.fromIterable(result.ticker!!.markets)}
            .flatMap { x: Observable<Crypto.Market>? ->x }!!.filter{y:Crypto.Market ->
            y.coinName = "Monero"
            true
        }.toList().toObservable()
        Observable.merge(listOf(btcObservable, ethObservable, ltcObservable, xrpObservable, xmrObservable))
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ marketList: List<Crypto.Market>? -> handleResults(marketList) }) { t: Throwable -> handleError(t) }
    }

    private fun handleResults(marketList: List<Crypto.Market>?) {
        if (marketList != null && marketList.size != 0) {
            recyclerViewAdapter!!.setData(marketList)
        } else {
            Toast.makeText(this, "NO RESULTS FOUND",
                Toast.LENGTH_LONG).show()
        }
    }

    private fun handleError(t: Throwable) {
        Toast.makeText(this, "ERROR IN FETCHING API RESPONSE. Try again",
            Toast.LENGTH_LONG).show()
    }
}