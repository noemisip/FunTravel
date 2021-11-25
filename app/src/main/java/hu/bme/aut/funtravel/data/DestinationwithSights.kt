package hu.bme.aut.funtravel.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class DestinationwithSights (
    @Embedded val destination: Destination,
    @Relation(
        parentColumn = "dname",
        entityColumn = "sdname"
    )
    val sights: List<Sight>
    )