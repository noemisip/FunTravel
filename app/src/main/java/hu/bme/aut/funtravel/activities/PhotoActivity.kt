package hu.bme.aut.funtravel.activities

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import hu.bme.aut.funtravel.R
import hu.bme.aut.funtravel.databinding.ActivityMapBinding
import hu.bme.aut.funtravel.databinding.ActivityPhotoBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

private lateinit var binding: ActivityPhotoBinding
class PhotoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.leftbutton.setOnClickListener {
           finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        binding.imageButton.setOnClickListener {
            dispatchTakePictureIntent()

        }
    }


    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }
}