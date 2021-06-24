package school.cesar.myweather.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import school.cesar.myweather.models.City
import school.cesar.myweather.models.FavoriteCity

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteCity: FavoriteCity)

    @Query("SELECT * FROM favorite_cities")
    suspend fun getAll(): List<FavoriteCity>
}