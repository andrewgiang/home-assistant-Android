package io.homeassistant.android.api

import io.homeassistant.android.data.model.HomeAssistantInstance
import retrofit2.http.GET

interface Api {
    @GET("api/discovery_info")
    suspend fun getDiscoveryInfo(): HomeAssistantInstance
}