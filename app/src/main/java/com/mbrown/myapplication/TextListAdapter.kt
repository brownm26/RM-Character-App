package com.mbrown.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mbrown.myapplication.databinding.TextItemViewBinding

data class TextIdItem(val id: Long, override val text: String) : TextItem(text)
open class TextItem(open val text: String)

class TextListAdapter(private val clickListener: (TextItem) -> Unit) : RecyclerView.Adapter<TextListAdapter.TextHolder>() {

    var items = listOf<TextItem>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextHolder {
        val binding = TextItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val view = binding.root
        view.setOnClickListener { v ->
            val position = (parent as RecyclerView).getChildLayoutPosition(v)
            clickListener(items[position])
        }
        return TextHolder(binding)
    }

    override fun onBindViewHolder(holder: TextHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.count()

    class TextHolder(binding: TextItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

        private val textView = binding.textView

        fun bind(item: TextItem) {
            textView.text = item.text
        }
    }
}