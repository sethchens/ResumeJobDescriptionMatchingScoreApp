package com.example.todolistapp.data

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

// Data classes to represent request and response
data class SimilarityRequest(val resume: String, val job_description: String)
data class SimilarityResponse(val similarity_score: Float)

// Define the API service interface
interface ApiService {
    @POST("/similarity_score")
    suspend fun getSimilarityScore(@Body data: SimilarityRequest): Response<SimilarityResponse>
}

class RepositoryFlaskAPI {

    // Initialize Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5000")  // The address provided by the emulator, which sends
        // sends the request to local loopback address (127.0.0.1)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    // Function to request similarity score from Flask API
    suspend fun getSimilarityScore(resumeText: String, jobDescriptionText: String): Float {
        return try {
            val request = SimilarityRequest(resumeText, jobDescriptionText)
            val response = apiService.getSimilarityScore(request)

            if (response.isSuccessful) {
                response.body()?.similarity_score ?: 0f
            } else {
                Log.e("FlaskAPI", "Error: ${response.errorBody()?.string()}")
                0f
            }
        } catch (e: Exception) {
            Log.e("FlaskAPI", "Network request failed", e)
            0f  // Return a default value
        }
    }
}