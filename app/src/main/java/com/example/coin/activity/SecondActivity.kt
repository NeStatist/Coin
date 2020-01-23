package com.example.coin.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coin.R
import com.example.coin.adapter.RecyclerViewAdapterSecond
import com.example.coin.net.Crypto
import com.example.coin.net.CurrencyCrypto
import com.example.coin.net.NameCrypto
import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class SecondActivity : AppCompatActivity() {

    var recyclerView: RecyclerView? = null
    var retrofit: Retrofit? = null
    var recyclerViewAdapter: RecyclerViewAdapterSecond? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        recyclerView = findViewById(R.id.recyclerViewSecondScreen)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerViewAdapter = RecyclerViewAdapterSecond()
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
        val crypto: NameCrypto? = intent.getBundleExtra("bundle")?.getParcelable("code")
        crypto?.let {
            callEndpoints(it)
        }
    }

    @SuppressLint("CheckResult")
    private fun callEndpoints(crypto:NameCrypto) {
        val cryptocurrency = retrofit!!.create(CurrencyCrypto::class.java)
        val btcObservable = cryptocurrency.getCoin(crypto.code)!!
            .map { result: Crypto ->
                result.coinName = crypto.name
                result
            }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{handleResults(it)}
    }

    private fun handleResults(marketList: Crypto) {
            recyclerViewAdapter!!.setData(marketList)
        }


    private fun handleError(t: Throwable) {
        Toast.makeText(this, "ERROR IN FETCHING API RESPONSE. Try again",
            Toast.LENGTH_LONG).show()
    }
}
