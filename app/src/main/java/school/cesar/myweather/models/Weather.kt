package school.cesar.myweather.models

data class Weather(val message: String, val list: List<City>)

data class City(val id: Long, val name: String, val main: Main)

data class Main(val temp: Float)
