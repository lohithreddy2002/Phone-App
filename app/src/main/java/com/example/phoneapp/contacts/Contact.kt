package com.example.phoneapp.contacts

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey
    val id: String,
        val name: String
) {
    var numbers = ArrayList<String>()
}