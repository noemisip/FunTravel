package hu.bme.aut.funtravel.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hu.bme.aut.funtravel.R
import hu.bme.aut.funtravel.data.Destination
import hu.bme.aut.funtravel.data.Sight
import hu.bme.aut.funtravel.databinding.ActivityDetailsBinding


private lateinit var binding: ActivityDetailsBinding


class DetailsActivity : AppCompatActivity() {

    var currentSight : Sight = Sight(0,"","","", 0.0,"",false);


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


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

    }
}