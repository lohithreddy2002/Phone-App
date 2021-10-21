package com.example.phoneapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.phoneapp.BlockedContactData

@Database(entities = [BlockedContactData::class], version = 1)
abstract class DataBaseDB : RoomDatabase() {
    abstract fun contactsDao(): ContactsDao
}