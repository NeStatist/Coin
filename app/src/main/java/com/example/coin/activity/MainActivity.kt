package com.example.coin.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coin.R
import com.example.coin.adapter.RecyclerViewAdapter
import com.example.coin.net.CryptoNames
import com.example.coin.net.NameCrypto
import com.google.gson.GsonBuilder
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
        recyclerViewAdapter!!.setListener(object: RecyclerViewAdapter.MyItemClick{
            override fun onClick(code: NameCrypto) {
              val intent = Intent(this@MainActivity,SecondActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable("code",code)
                intent.putExtra("bundle",bundle)
                startActivity(intent)
            }
        })
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val gson = GsonBuilder()
            .setLenient()
            .create()
        retrofit = Retrofit.Builder()
            .baseUrl(CryptoNames.BASE_URL)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        callEndpoints()
    }

    @SuppressLint("CheckResult")
    private fun callEndpoints() {
        val namesApi = retrofit!!.create(CryptoNames::class.java)
        val names = namesApi.getNames()
            .map { result: NameCrypto.Rows ->
                result.rows
            }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { handleResults(it) }
    }

    private fun handleResults(names: List<NameCrypto>?) {
        if (names != null && names.isNotEmpty()) {
            recyclerViewAdapter!!.setData(names)
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