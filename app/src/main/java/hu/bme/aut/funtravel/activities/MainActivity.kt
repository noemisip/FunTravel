package hu.bme.aut.funtravel.activities

import android.R.attr
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.funtravel.fragments.NewDestinationDialogFragment
import hu.bme.aut.funtravel.adapter.DestinationAdapter
import hu.bme.aut.funtravel.data.Destination
import hu.bme.aut.funtravel.data.DestinationDatabase

import hu.bme.aut.funtravel.databinding.ActivityMainBinding
import kotlin.concurrent.thread
import android.widget.Toast

import android.R.attr.data




private lateinit var binding: ActivityMainBinding
private lateinit var adapter: DestinationAdapter
private lateinit var database: DestinationDatabase


class MainActivity : AppCompatActivity(), DestinationAdapter.DestinationClickListener, NewDestinationDialogFragment.NewDestinationDialogListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

       database = DestinationDatabase.getDatabase(applicationContext)


        binding.btnplus.setOnClickListener(){

            NewDestinationDialogFragment().show(
                supportFragmentManager,
                NewDestinationDialogFragment.TAG
            )
        }
        initRecyclerView()

    }

    private fun initRecyclerView() {
        adapter = DestinationAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter
        loadItemsInBackground()
    }

    override fun onItemClick(item: Destination) {
        val intent = Intent(this, SightsActivity::class.java)
        intent.putExtra("DESTINATION_NAME", item.dname)
        startActivity(intent)
    }

    override fun onItemChanged(item: Destination) {
        //database.DestinationDao().update(item)
    }


    override fun onDestinationCreated(newItem: Destination) {
        thread {
            val insertId = database.DestinationDao().insert(newItem)
            newItem.destId = insertId
            runOnUiThread {
                adapter.addItem(newItem)
            }
        }
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.DestinationDao().getAll()
            runOnUiThread {
                adapter.update(items)
            }
        }
    }
}