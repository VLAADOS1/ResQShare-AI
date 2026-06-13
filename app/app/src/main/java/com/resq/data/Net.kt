package com.resq.data

import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object Net {

    private const val URL = "https://mounted-tones-strip-convert.trycloudflare.com/api/"

    fun img(id: Long): String = URL + "dona/" + id + "/img"

    fun pic(iid: Long): String = URL + "img/" + iid

    private val cli = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(180, TimeUnit.SECONDS)
        .callTimeout(190, TimeUnit.SECONDS)
        .build()

    val api: Api by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .client(cli)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }
}
