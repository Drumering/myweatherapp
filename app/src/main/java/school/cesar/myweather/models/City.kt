package school.cesar.myweather.models

import com.google.gson.annotations.SerializedName

data class City(val id: Long, val name: String, val main: Main, @SerializedName("weather") val icons: MutableList<Icon>)
