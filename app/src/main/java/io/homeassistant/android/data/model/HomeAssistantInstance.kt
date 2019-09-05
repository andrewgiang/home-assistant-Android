package io.homeassistant.android.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import okhttp3.HttpUrl

@Parcelize
@JsonClass(generateAdapter = true)
data class HomeAssistantInstance(
    @Json(name = "base_url")
    val baseUrl: String,
    val version: String,
    @Json(name = "location_name")
    val name: String
) : Parcelable {

    val baseHttpUrl: HttpUrl = HttpUrl.get(baseUrl)
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