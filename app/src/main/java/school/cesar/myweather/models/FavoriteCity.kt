package school.cesar.myweather.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_cities")
data class FavoriteCity(@PrimaryKey var id: Long)
