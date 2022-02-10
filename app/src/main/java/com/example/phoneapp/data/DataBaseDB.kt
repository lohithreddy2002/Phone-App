package com.example.phoneapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.phoneapp.contacts.Contact
import com.example.phoneapp.home.models.BlockedContactData

@Database(entities = [BlockedContactData::class,Contact::class], version = 1)
@TypeConverters(ContactTypeConvertor::class)
abstract class DataBaseDB : RoomDatabase() {
    abstract fun contactsDao(): ContactsDao
}