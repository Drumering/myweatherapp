package school.cesar.myweather.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class City(@PrimaryKey val id: Long, val name: String, val main: Main)
