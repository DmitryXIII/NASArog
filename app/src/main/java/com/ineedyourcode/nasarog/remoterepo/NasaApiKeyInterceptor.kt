package com.ineedyourcode.nasarog.remoterepo

import com.ineedyourcode.nasarog.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class NasaApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val updatedUrl = chain.request().url.newBuilder()
            .addQueryParameter("api_key", BuildConfig.NASA_API_KEY)
            .build()

        return chain.proceed(
            chain.request().newBuilder()
                .url(updatedUrl)
                .build()
        )
    }
}