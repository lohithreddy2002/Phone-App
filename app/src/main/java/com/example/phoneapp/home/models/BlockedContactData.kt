package com.example.phoneapp.home.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blocked_contacts")
data class BlockedContactData(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val name:String,
    val phone:String
)