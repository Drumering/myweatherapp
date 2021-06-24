package school.cesar.myweather.connector

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import school.cesar.myweather.dao.WeatherDao
import school.cesar.myweather.models.City
import school.cesar.myweather.models.FavoriteCity
import school.cesar.myweather.models.Main
import school.cesar.myweather.models.Icon

@Database(entities = [FavoriteCity::class], version = 1, exportSchema = false)
abstract class DatabaseConnector : RoomDatabase(){
    abstract val weatherDao: WeatherDao

    companion object {
        private var INSTANCE: DatabaseConnector? = null
        fun getInstance(context: Context) : DatabaseConnector {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        DatabaseConnector::class.java,
                        "weather_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}