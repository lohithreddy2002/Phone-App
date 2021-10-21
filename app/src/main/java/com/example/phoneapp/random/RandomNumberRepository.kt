package com.example.phoneapp.random

import com.example.phoneapp.BlockedContactData
import com.example.phoneapp.data.DataBaseDB
import javax.inject.Inject

class RandomNumberRepository @Inject constructor(private val database: DataBaseDB) {

    private val dao = database.contactsDao()
   suspend fun insert(contactData: BlockedContactData) = dao.insertContact(contactData)
}