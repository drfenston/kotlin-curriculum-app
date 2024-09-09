/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyrilmaquaire.curriculum.network

import com.cyrilmaquaire.curriculum.model.requests.LoginRequest
import com.cyrilmaquaire.curriculum.model.responses.GetCvListResponse
import com.cyrilmaquaire.curriculum.model.responses.GetCvResponse
import com.cyrilmaquaire.curriculum.model.responses.LoginResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


private const val BASE_URL =
    "https://cyrilmaquaire.com/curriculum/api/"


fun getClient(): Retrofit {

    val logging = HttpLoggingInterceptor()

    // set your desired log level
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
    val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()

    // add your other interceptors …
    // add logging as last interceptor
    httpClient.addInterceptor(logging)

    return Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        //.client(httpClient.build())
        .build()
}

/**
 * Retrofit service object for creating api calls
 */
interface CvApiService {
    @GET("CV")
    suspend fun getAllCV(): GetCvListResponse

    @GET("CV/{id}")
    suspend fun getCV(@Path("id") id: Long?): GetCvResponse

    @POST("Login")
    suspend fun login(@Body loginResquest: LoginRequest): LoginResponse
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object CvApi {
    val retrofitService: CvApiService by lazy {
        getClient().create(CvApiService::class.java)
    }
}
