package hu.bme.aut.funtravel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.funtravel.data.DestinationwithSights
import hu.bme.aut.funtravel.data.Sight
import hu.bme.aut.funtravel.databinding.ItemDestinationBinding
import hu.bme.aut.funtravel.databinding.ItemSightBinding

class SightAdapter(private val listener: SightAdapter.SightClickListener) : RecyclerView.Adapter<SightAdapter.SightViewHolder>() {

    private val items = mutableListOf<Sight>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SightViewHolder(
        ItemSightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: SightViewHolder, position: Int) {
        val sight = items[position]
        holder.sight = sight
        holder.binding.textname.text = sight.name
        holder.binding.check.isChecked = sight.seen
        holder.binding.delete.setOnClickListener(){
            listener.onItemDeleted(sight)
        }
        holder.binding.check.setOnCheckedChangeListener { buttonView, isChecked ->
            sight.seen = isChecked
            listener.onItemChanged(sight)
        }
    }

    interface SightClickListener{
        fun onItemClick(item: Sight)
        fun onItemDeleted(item: Sight)
        fun onItemChanged(item: Sight)

    }
    override fun getItemCount(): Int = items.size

    inner class SightViewHolder(val binding: ItemSightBinding) : RecyclerView.ViewHolder(binding.root){
        var sight: Sight? = null

        init {

            itemView.setOnClickListener {
                sight?.let{ sight -> listener.onItemClick(sight)
                }
            }
        }

    }

    fun delete( item:Sight){
        items.remove(item)
        notifyDataSetChanged()
    }

    fun addItem(item: Sight) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(sights: List<Sight>) {
        items.clear()
        items.addAll(sights)
        notifyDataSetChanged()
    }

}