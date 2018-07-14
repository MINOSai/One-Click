package com.minosai.oneclick.network

import com.minosai.oneclick.model.Payload
import kotlinx.coroutines.experimental.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface WebService {

    @POST("authlogin")
    fun loginUser(@Body payload: Payload): Deferred<Response<Any>>

    @GET("authlogout?")
    fun logoutUser(): Deferred<Response<Any>>
}