package hu.bme.aut.funtravel.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import hu.bme.aut.funtravel.adapter.SightAdapter
import hu.bme.aut.funtravel.data.Sight
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.funtravel.R
import hu.bme.aut.funtravel.data.Destination
import hu.bme.aut.funtravel.data.DestinationDatabase

import hu.bme.aut.funtravel.fragments.NewSightDialogFragment

import hu.bme.aut.funtravel.databinding.ActivitySightsBinding
import kotlin.concurrent.thread


private lateinit var binding : ActivitySightsBinding
private lateinit var adapter: SightAdapter
private lateinit var database: DestinationDatabase



class SightsActivity : AppCompatActivity(), NewSightDialogFragment.NewSightDialogListener,SightAdapter.SightClickListener{

    var currDestination : Destination? = Destination(0,"","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySightsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.btnplus.setOnClickListener {
            NewSightDialogFragment().show(
                supportFragmentManager,
                NewSightDialogFragment.TAG
            )
        }

        binding.rightbutton.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("DESTINATION_NAME",currDestination!!.dname)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        binding.leftbutton.setOnClickListener {
            finish()
            overridePendingTransition(hu.bme.aut.funtravel.R.anim.slide_in_left, hu.bme.aut.funtravel.R.anim.slide_out_right);
        }

        database = DestinationDatabase.getDatabase(applicationContext)

        currDestination = Destination(0,intent.getStringExtra("DESTINATION_NAME").toString(),"")

        binding.textDestination.text = currDestination!!.dname
        initRecyclerView()

    }

    private fun initRecyclerView() {
        adapter = SightAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter
        loadItemsInBackground()

    }


    override fun onSightCreated(newItem: Sight) {
        newItem.sdname = currDestination!!.dname
        thread {
            val insertId = database.DestinationDao().insertSight(newItem)
            newItem.sightId = insertId
            runOnUiThread {
                adapter.addItem(newItem)
            }
        }
    }

    override fun onItemClick(item: Sight) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("SIGHT_NAME", item.name)
        intent.putExtra("SIGHT_LOCATION", item.location)
        intent.putExtra("SIGHT_LINK", item.link)
        intent.putExtra("SIGHT_PRICE", item.price.toString())
        startActivity(intent)

    }

    override fun onItemDeleted(item: Sight) {
        thread {
            database.DestinationDao().deleteSight(item)
            runOnUiThread(){
                adapter.delete(item)
            }
        }
    }


    private fun loadItemsInBackground() {
       thread {
            val items = database.DestinationDao().getDestWithSights(currDestination!!.dname)
            runOnUiThread {
              adapter.update(items[items.size-1].sights)
            }
        }

    }

    override fun onItemChanged(item: Sight) {
        thread {
            database.DestinationDao().updateSight(item)
        }
    }


}