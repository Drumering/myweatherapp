package school.cesar.myweather.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "main")
data class Main(@PrimaryKey var temp: Float)
