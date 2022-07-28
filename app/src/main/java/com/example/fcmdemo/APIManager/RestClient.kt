package com.example.fcmdemo.APIManager

import android.util.Log
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class RestClient {
    private val serviceCall: ServiceCall
    @Throws(IOException::class)
    fun getTopics(token:String): TopicListModel? {
        val opModel: Call<TopicListModel>? = serviceCall.getTopics(token,true)
        var opRes: Response<TopicListModel>? = null
            opRes = opModel?.execute()
        Log.d("gettopics body", opRes?.body().toString());
        return opRes?.body()
    }

    init {
        val logging = HttpLoggingInterceptor { message ->
            Log.d(
                "**** API ****",
                "[ $message ]...."
            )
        }
        logging.level = HttpLoggingInterceptor.Level.BODY


        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .addInterceptor(logging)
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(15, TimeUnit.MINUTES)
            .readTimeout(15, TimeUnit.MINUTES).build()

        val mapper: ObjectMapper =
            ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val retroBuild = Retrofit.Builder()
            .client(client)
            .baseUrl("https://iid.googleapis.com/")
            .addConverterFactory(JacksonConverterFactory.create(mapper))
        val retrofit = retroBuild.build()
        serviceCall = retrofit.create(ServiceCall::class.java)
    }
}

