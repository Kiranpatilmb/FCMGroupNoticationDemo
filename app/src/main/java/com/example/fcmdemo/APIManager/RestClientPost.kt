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

class RestClientPost {
    private val serviceCall: ServiceCall
    var ispost=false;
    var isget=true;
    @Throws(IOException::class)
    fun postTopic(formFields: OperationModel): ResponseModel? {
        val opModel: Call<ResponseModel>? = serviceCall.postTopic(formFields)
        var opRes: Response<ResponseModel>? = null
        val thread = Thread(Runnable {
            opRes = opModel?.execute()
        })
        thread.start();
        return opRes?.body()
    }

    init {
        val logging = HttpLoggingInterceptor { message ->
            Log.d(
                "**** API ****",
                "[ $message ]...."
            )
        }
        //logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
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
            .baseUrl("https://fcm.googleapis.com/")
            .addConverterFactory(JacksonConverterFactory.create(mapper))
        val retrofit = retroBuild.build()
        serviceCall = retrofit.create(ServiceCall::class.java)
    }
}

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val original: Request = chain.request()
        val request: Request = original.newBuilder()
            .addHeader("Content-Type", "application/json") //<-- need to remove this one for only one request
            .addHeader("Authorization", "key=AAAAVsRwrYA:APA91bHsi8rTSFNT4DTccWsuYWaS4tOWTqfsN1UbsDB7y2-TV6CuzztF2bNUvRiU7kcSjI25h7fJjOp6ln-I2VMVm0_XcG-TtOEtOhOs41K5aFAK1X3WKy-hUubEvp2sOg0jp1t3aMDY")
            .removeHeader("Content-Length")
            .method(original.method(), original.body())
            .build()
        return chain.proceed(request);
    }}