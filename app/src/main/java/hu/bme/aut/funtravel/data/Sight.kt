package hu.bme.aut.funtravel.data

import android.media.Image
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Sight (

    @PrimaryKey (autoGenerate = true)
    var sightId: Long,
    var name: String,
    var location: String,
    var link: String,
    var price: Double,
    var sdname: String,
    var seen: Boolean

    )