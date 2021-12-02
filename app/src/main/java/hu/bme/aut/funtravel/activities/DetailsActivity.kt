package hu.bme.aut.funtravel.activities

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import hu.bme.aut.funtravel.R
import hu.bme.aut.funtravel.data.Destination
import hu.bme.aut.funtravel.data.DestinationDatabase
import hu.bme.aut.funtravel.data.Sight
import hu.bme.aut.funtravel.databinding.ActivityDetailsBinding
import java.io.IOException
import java.util.*
import kotlin.concurrent.thread


private lateinit var binding: ActivityDetailsBinding
private lateinit var database: DestinationDatabase



class DetailsActivity : AppCompatActivity() , OnMapReadyCallback,  GoogleMap.OnMarkerClickListener{

    var currentSight : Sight = Sight(0,"","","", 0.0,"",false);

    private var mMap: GoogleMap? = null
    private var geo : Geocoder? = null
    private var location : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = DestinationDatabase.getDatabase(applicationContext)

        currentSight = Sight(0,intent.getStringExtra("SIGHT_NAME").toString(),
            intent.getStringExtra("SIGHT_LOCATION").toString(),
            intent.getStringExtra("SIGHT_LINK").toString(),
            intent.getStringExtra("SIGHT_PRICE").toString().toDouble(),"", false)

        binding.leftbutton.setOnClickListener {
            finish()
        }

        binding.textname.text = currentSight.name
        binding.textlocation.text = currentSight.location
        binding.textlink.text = currentSight.link
        binding.textprice.text = currentSight.price.toString()


        thread {
            binding.textlocation.text = database.DestinationDao().getSightLocation(currentSight.name)
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.sightmap) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

    }

    override fun onMapReady(googlemap: GoogleMap?) {
        mMap = googlemap
        if (mMap != null) {
            geo = Geocoder(this, Locale.getDefault());

            if(binding.textlocation.text != ""){
                var pin = getLocationFromAddress(baseContext,binding.textlocation.text.toString())
                mMap!!.addMarker(
                    MarkerOptions()
                        .position(pin!!)
                        .title(binding.textlocation.text.toString())
                )
            }

            val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle)
            mMap!!.setMapStyle(mapStyleOptions)

            mMap!!.setOnMapClickListener { latLng ->
                try {
                    if (geo == null) geo = Geocoder(this, Locale.getDefault())
                    val address = geo!!.getFromLocation(latLng.latitude, latLng.longitude, 1)
                    location = address[0].getAddressLine(0)
                    if (address.size > 0) {
                        mMap!!.addMarker(
                            MarkerOptions().position(latLng).title(
                                "Country: " + address[0].countryName
                                        + " Address: " + address[0].getAddressLine(0)
                            )
                        )
                        binding.textlocation.setText(
                            "Country: " + address[0].countryName
                                    + " Address: " + address[0].getAddressLine(0)
                        )
                        thread {
                            database.DestinationDao().updateSight(currentSight.name,location!!)
                        }

                    }
                } catch (ex: IOException) {
                  Toast.makeText(
                        this,
                        "Error:" + ex.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }


    }

    fun getLocationFromAddress(context: Context?, strAddress: String?): LatLng? {
        val coder = Geocoder(context)
        val address: List<Address>?
        var p1: LatLng? = null
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            val location = address[0]
            p1 = LatLng(location.latitude, location.longitude)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return p1
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("Not yet implemented")
    }


}