package hu.bme.aut.funtravel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.funtravel.data.Destination
import hu.bme.aut.funtravel.databinding.ItemDestinationBinding

class DestinationAdapter(private val listener: DestinationClickListener) :
    RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder>() {


    private val items = mutableListOf<Destination>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DestinationViewHolder(
        ItemDestinationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        val destination = items[position]
        holder.destination = destination
        // var dest: Destination =  Destination("Barcelona","","",null, 1,null)
        holder.binding.textname.text = destination.dname
    }

    interface DestinationClickListener{
        fun onItemClick(item: Destination)
        fun onItemChanged(item: Destination)

    }
    override fun getItemCount(): Int = items.size

    inner class DestinationViewHolder(val binding: ItemDestinationBinding) : RecyclerView.ViewHolder(binding.root){
        var destination: Destination? = null

        init {

            itemView.setOnClickListener {
                destination?.let{ destination -> listener.onItemClick(destination)
                }
            }
        }

    }

    fun addItem(item: Destination) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(destinations: List<Destination>) {
        items.clear()
        items.addAll(destinations)
        notifyDataSetChanged()
    }

}
