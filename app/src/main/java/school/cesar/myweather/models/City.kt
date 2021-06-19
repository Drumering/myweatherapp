package school.cesar.myweather.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class City(@PrimaryKey var id: Long, var name: String, @Embedded var main: Main)
