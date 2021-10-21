package com.example.phoneapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.phoneapp.contacts.Contact
import com.example.phoneapp.databinding.ContactsLayoutBinding
import timber.log.Timber

class ContactsAdapter(val add: (Contact) -> Unit) :
    ListAdapter<Contact, ContactsAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val itemBinding: ContactsLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Contact) {
            itemBinding.name.text = item.name
            itemBinding.number.text = item.numbers[0]
            itemBinding.root.setOnClickListener {
                add(item)
            }
        }

    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Contact>() {
            override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ContactsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(getItem(position).numbers.size>0)
        holder.bind(getItem(position))
    }
}
