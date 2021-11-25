package hu.bme.aut.funtravel.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [Destination::class, Sight::class], version = 1, exportSchema = false)
abstract class DestinationDatabase : RoomDatabase(){

    abstract fun DestinationDao(): DestinationDao
    companion object {

        fun getDatabase(applicationContext: Context): DestinationDatabase {
            return Room.databaseBuilder(
                applicationContext,
                DestinationDatabase::class.java,
                "funtravel"
            ).build();
        }

    }

}