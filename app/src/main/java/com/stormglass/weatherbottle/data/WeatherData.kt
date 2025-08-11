package com.stormglass.weatherbottle.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("now")
    val now: CurrentWeather,
    @SerializedName("location")
    val location: List<Location>
)

data class CurrentWeather(
    @SerializedName("obsTime")
    val obsTime: String,
    @SerializedName("temp")
    val temp: String,
    @SerializedName("feelsLike")
    val feelsLike: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("wind360")
    val wind360: String,
    @SerializedName("windDir")
    val windDir: String,
    @SerializedName("windScale")
    val windScale: String,
    @SerializedName("windSpeed")
    val windSpeed: String,
    @SerializedName("humidity")
    val humidity: String,
    @SerializedName("precip")
    val precip: String,
    @SerializedName("pressure")
    val pressure: String,
    @SerializedName("vis")
    val vis: String,
    @SerializedName("cloud")
    val cloud: String,
    @SerializedName("dew")
    val dew: String
)

data class Location(
    @SerializedName("name")
    val name: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("lat")
    val lat: String,
    @SerializedName("lon")
    val lon: String,
    @SerializedName("adm2")
    val adm2: String,
    @SerializedName("adm1")
    val adm1: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("tz")
    val tz: String,
    @SerializedName("utcOffset")
    val utcOffset: String,
    @SerializedName("isDst")
    val isDst: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("rank")
    val rank: String,
    @SerializedName("fxLink")
    val fxLink: String
)

enum class WeatherType(val description: String) {
    SUNNY("晴天"),
    CLOUDY("多云"),
    RAINY("雨天"),
    SNOWY("雪天"),
    UNKNOWN("未知")
}

fun CurrentWeather.toWeatherType(): WeatherType {
    return when {
        text.contains("晴") -> WeatherType.SUNNY
        text.contains("云") -> WeatherType.CLOUDY
        text.contains("雨") -> WeatherType.RAINY
        text.contains("雪") -> WeatherType.SNOWY
        else -> WeatherType.UNKNOWN
    }
}
