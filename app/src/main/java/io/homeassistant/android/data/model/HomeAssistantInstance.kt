package io.homeassistant.android.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomeAssistantInstance(
    val baseUrl: String,
    val version: String,
    val name: String
) : Parcelable {
    companion object {
        fun fromMap(attrMap: Map<String, String>): HomeAssistantInstance {
            return HomeAssistantInstance(
                baseUrl = attrMap["base_url"]
                    ?: throw IllegalStateException("baseUrl should not be null"),
                version = attrMap["version"].orEmpty(),
                name = attrMap["name"].orEmpty()
            )
        }
    }
}