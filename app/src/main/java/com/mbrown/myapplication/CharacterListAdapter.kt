package com.mbrown.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mbrown.myapplication.databinding.CharacterItemViewBinding

data class CharacterItem(val id: Long, val imageUrl: String, val name: String, val status: String, val species: String)

class CharacterListAdapter(private val clickListener: (CharacterItem) -> Unit) : RecyclerView.Adapter<CharacterListAdapter.CharacterHolder>() {

    var items = listOf<CharacterItem>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterHolder {
        val binding = CharacterItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val view = binding.root
        view.setOnClickListener { v ->
            val position = (parent as RecyclerView).getChildLayoutPosition(v)
            clickListener(items[position])
        }
        return CharacterHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.count()

    class CharacterHolder(private val binding: CharacterItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CharacterItem) {
            Glide.with(binding.root).load(item.imageUrl).into(binding.image)
            binding.name.text = item.name
            binding.info.text = "${item.status} - ${item.species}"
        }
    }
}