package school.cesar.myweather.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import school.cesar.myweather.models.City

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(city: City)

    @Query("SELECT * FROM cities")
    fun getAll(): List<City>
}