package io.homeassistant.android.onboarding

import io.homeassistant.android.api.Api
import io.homeassistant.android.data.model.HomeAssistantInstance
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class AuthRepository(baseUrl: String) {

    private val api = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(baseUrl)
        .build()
        .create(Api::class.java)

    suspend fun testConnection(): HomeAssistantInstance {
        return api.getDiscoveryInfo()
    }
}

