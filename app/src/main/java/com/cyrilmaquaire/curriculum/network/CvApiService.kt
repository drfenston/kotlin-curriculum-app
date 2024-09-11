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

import com.cyrilmaquaire.curriculum.model.CV
import com.cyrilmaquaire.curriculum.model.requests.LoginRequest
import com.cyrilmaquaire.curriculum.model.responses.AutreResponse
import com.cyrilmaquaire.curriculum.model.responses.CompTechResponse
import com.cyrilmaquaire.curriculum.model.responses.ExperienceResponse
import com.cyrilmaquaire.curriculum.model.responses.FormationResponse
import com.cyrilmaquaire.curriculum.model.responses.LangueResponse
import com.cyrilmaquaire.curriculum.model.responses.ProjetResponse
import com.cyrilmaquaire.curriculum.model.responses.GetCvListResponse
import com.cyrilmaquaire.curriculum.model.responses.GetCvResponse
import com.cyrilmaquaire.curriculum.model.responses.LoginResponse
import com.cyrilmaquaire.curriculum.token
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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
        .client(okHttpClient().build())
        .build()
}

private fun okHttpClient() = OkHttpClient().newBuilder()
    .addInterceptor(
        Interceptor { chain ->
            val request: Request = chain.request()
                .newBuilder()
                .header("accept", "application/json")
                .header("Authorization", "Bearer $token")
                .build()
            chain.proceed(request)
        }
    )

/**
 * Retrofit service object for creating api calls
 */
interface CvApiService {
    @GET("CV")
    suspend fun getAllCV(): GetCvListResponse

    @GET("CV/{id}")
    suspend fun getCV(@Path("id") id: Long?): GetCvResponse

    @PUT("CV/{id}")
    suspend fun updateCV(@Path("id") id: Long?, @Body cv: CV): GetCvResponse

    @POST("Login")
    suspend fun login(@Body loginResquest: LoginRequest): LoginResponse

    @POST("langue/{cvId}")
    suspend fun createLangue(@Path("cvId") cvId: Long?): LangueResponse

    @POST("competenceTechnique/{cvId}")
    suspend fun createCompetenceTechnique(@Path("cvId") cvId: Long?): CompTechResponse

    @POST("autre/{cvId}")
    suspend fun createAutre(@Path("cvId") cvId: Long?): AutreResponse

    @POST("experience/{cvId}")
    suspend fun createExperience(@Path("cvId") cvId: Long?): ExperienceResponse

    @POST("formation/{cvId}")
    suspend fun createFormation(@Path("cvId") cvId: Long?): FormationResponse

    @POST("projet/{projetId}")
    suspend fun createProjet(@Path("projetId") cvId: Long?): ProjetResponse

    @DELETE("langue/{cvId}")
    suspend fun deleteLangue(@Path("cvId") cvId: Long?): LangueResponse

    @DELETE("competenceTechnique/{cvId}")
    suspend fun deleteCompetenceTechnique(@Path("cvId") cvId: Long?): CompTechResponse

    @DELETE("autre/{cvId}")
    suspend fun deleteAutre(@Path("cvId") cvId: Long?): AutreResponse

    @DELETE("experience/{cvId}")
    suspend fun deleteExperience(@Path("cvId") cvId: Long?): ExperienceResponse

    @DELETE("formation/{cvId}")
    suspend fun deleteFormation(@Path("cvId") cvId: Long?): FormationResponse

    @DELETE("projet/{projetId}")
    suspend fun deleteProjet(@Path("projetId") cvId: Long?): ProjetResponse

}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object CvApi {
    val retrofitService: CvApiService by lazy {
        getClient().create(CvApiService::class.java)
    }
}
