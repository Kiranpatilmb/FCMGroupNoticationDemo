package com.example.fcmdemo.APIManager

import retrofit2.Call
import retrofit2.http.*
import java.util.concurrent.atomic.AtomicBoolean

interface ServiceCall  {

    @POST("fcm/send")
    fun postTopic(@Body operationModel: OperationModel): Call<ResponseModel>?


    @GET("iid/info/{token}")
    fun getTopics( @Path("token") Token: String?,@Query("details") details: Boolean ): Call<TopicListModel>?

}
