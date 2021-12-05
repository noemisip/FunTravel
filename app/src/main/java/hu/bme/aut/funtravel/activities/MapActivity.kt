package hu.bme.aut.funtravel.activities

import android.content.Context
import android.content.Intent
import android.location.Address
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hu.bme.aut.funtravel.databinding.ActivityMapBinding
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener

import com.google.android.gms.maps.model.Marker
import java.io.IOException
import java.security.AccessController.getContext
import java.util.*
import android.widget.Toast
import com.google.android.gms.maps.model.MapStyleOptions
import hu.bme.aut.funtravel.R
import hu.bme.aut.funtravel.adapter.DestinationAdapter
import hu.bme.aut.funtravel.data.DestinationDatabase
import kotlin.concurrent.thread


private lateinit var binding: ActivityMapBinding
private lateinit var database: DestinationDatabase

class MapActivity : AppCompatActivity(), OnMapReadyCallback{

    private var mMap: GoogleMap? = null
    private var geo : Geocoder? = null
    private var location : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = DestinationDatabase.getDatabase(applicationContext)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rightbutton.setOnClickListener {
            val intent = Intent(this, PhotoActivity::class.java)
            startActivity(intent)
            overridePendingTransition(hu.bme.aut.funtravel.R.anim.slide_in_right, hu.bme.aut.funtravel.R.anim.slide_out_left);
        }
        binding.leftbutton.setOnClickListener {
            finish()
            overridePendingTransition(hu.bme.aut.funtravel.R.anim.slide_in_left, hu.bme.aut.funtravel.R.anim.slide_out_right);
        }

        thread {
            binding.textLocation.text = database.DestinationDao().getDestLocation(intent.getStringExtra("DESTINATION_NAME").toString())
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(hu.bme.aut.funtravel.R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

    }

    override fun onMapReady(googlemap: GoogleMap?) {
        mMap = googlemap


        val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle)
        mMap!!.setMapStyle(mapStyleOptions)

        if (mMap != null) {
            geo = Geocoder(this, Locale.getDefault());

            if(binding.textLocation.text != ""){
                var pin = getLocationFromAddress(baseContext,binding.textLocation.text.toString())
                mMap!!.addMarker(
                    MarkerOptions()
                        .position(pin!!)
                        .title(binding.textLocation.text.toString())
                )
            }


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
                        binding.textLocation.text = ("Country: " + address[0].countryName
                                + " Address: " + address[0].getAddressLine(0))
                        thread {
                            database.DestinationDao().updateDest(intent.getStringExtra("DESTINATION_NAME").toString(),location!!)
                        }

                    }
                } catch (ex: IOException) {
                    if (ex != null) Toast.makeText(
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

}

