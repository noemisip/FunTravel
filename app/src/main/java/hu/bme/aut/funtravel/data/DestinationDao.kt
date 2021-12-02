package hu.bme.aut.funtravel.data

import androidx.room.*

@Dao
interface DestinationDao {
    @Query("SELECT * FROM destination")
    fun getAll(): List<Destination>


    @Query("SELECT dname FROM destination WHERE dname = :name")
    fun getDestName(name: String): String

    @Query("SELECT location FROM destination WHERE dname = :name")
    fun getDestLocation(name: String): String

    @Query("SELECT location FROM sight WHERE name = :name")
    fun getSightLocation(name: String): String

    @Insert
    fun insert(destinations: Destination): Long

    @Insert
    fun insertSight(sights: Sight): Long

    @Transaction
    @Query("SELECT * FROM destination WHERE dname = :name")
    fun getDestWithSights(name: String): List<DestinationwithSights>

    @Query("UPDATE Destination SET location = :location WHERE dname = :name")
    fun updateDest(name: String, location: String)

    @Query("UPDATE Sight SET location = :location WHERE name = :name")
    fun updateSight(name: String, location: String)

    @Update
    fun updateSight(sight: Sight)

    @Delete
    fun deleteSight(sight: Sight)
}