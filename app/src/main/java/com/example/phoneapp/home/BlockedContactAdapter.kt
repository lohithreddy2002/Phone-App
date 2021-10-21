package com.example.phoneapp.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.phoneapp.BlockedContactData
import com.example.phoneapp.databinding.BlockedNumbersLayoutBinding

class BlockedContactAdapter(val delete: (BlockedContactData) -> Unit) :
    ListAdapter<BlockedContactData, BlockedContactAdapter.ViewHolder>(diffutil) {

    inner class ViewHolder(private val itemBinding: BlockedNumbersLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: BlockedContactData) {
            itemBinding.name.text = item.name
            itemBinding.number.text = item.phone
            itemBinding.delete.setOnClickListener {
                delete(item)
            }
        }
    }

    companion object {
        val diffutil = object : DiffUtil.ItemCallback<BlockedContactData>() {
            override fun areItemsTheSame(
                oldItem: BlockedContactData,
                newItem: BlockedContactData
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: BlockedContactData,
                newItem: BlockedContactData
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            BlockedNumbersLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
