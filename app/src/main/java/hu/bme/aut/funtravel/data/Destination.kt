package hu.bme.aut.funtravel.data

import android.media.Image
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity ( tableName = "Destination")
data class Destination(
  @PrimaryKey (autoGenerate = true)
   var destId: Long,
   var dname: String,
   var location: String

)
