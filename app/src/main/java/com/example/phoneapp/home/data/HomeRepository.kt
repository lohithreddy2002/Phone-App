package com.example.phoneapp.home.data

import com.example.phoneapp.home.models.BlockedContactData
import com.example.phoneapp.data.DataBaseDB
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepository @Inject constructor(private val dataBaseDB: DataBaseDB) {
    private val dao = dataBaseDB.contactsDao()

    suspend fun deleteContact(data: BlockedContactData) = dao.deleteContact(data)

    fun getAllContacts(): Flow<List<BlockedContactData>> = dao.getContacts()

}