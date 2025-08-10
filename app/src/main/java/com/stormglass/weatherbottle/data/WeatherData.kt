package com.stormglass.weatherbottle.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("current")
    val current: CurrentWeather,
    @SerializedName("location")
    val location: Location
)

data class CurrentWeather(
    @SerializedName("temp_c")
    val temperatureCelsius: Double,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("wind_kph")
    val windSpeedKph: Double,
    @SerializedName("condition")
    val condition: WeatherCondition,
    @SerializedName("last_updated")
    val lastUpdated: String
)

data class WeatherCondition(
    @SerializedName("text")
    val text: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("code")
    val code: Int
)

data class Location(
    @SerializedName("name")
    val name: String,
    @SerializedName("region")
    val region: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("lon")
    val longitude: Double,
    @SerializedName("localtime")
    val localTime: String
)

enum class WeatherType(val description: String) {
    SUNNY("晴天"),
    CLOUDY("多云"),
    RAINY("雨天"),
    SNOWY("雪天"),
    UNKNOWN("未知")
}

fun WeatherCondition.toWeatherType(): WeatherType {
    return when {
        text.contains("晴") || text.contains("Sunny") -> WeatherType.SUNNY
        text.contains("云") || text.contains("Cloudy") -> WeatherType.CLOUDY
        text.contains("雨") || text.contains("Rain") -> WeatherType.RAINY
        text.contains("雪") || text.contains("Snow") -> WeatherType.SNOWY
        else -> WeatherType.UNKNOWN
    }
}
