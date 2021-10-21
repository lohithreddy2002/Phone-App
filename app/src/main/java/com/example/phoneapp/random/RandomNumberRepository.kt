package com.example.phoneapp.random

import com.example.phoneapp.home.models.BlockedContactData
import com.example.phoneapp.data.DataBaseDB
import javax.inject.Inject

class RandomNumberRepository @Inject constructor(private val database: DataBaseDB) {

    private val dao = database.contactsDao()
   suspend fun insert(contactData: BlockedContactData) = dao.insertContact(contactData)
}