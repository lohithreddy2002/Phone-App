package com.example.phoneapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.phoneapp.contacts.Contact
import com.example.phoneapp.home.models.BlockedContactData
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(data: BlockedContactData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfContacts(data:List<Contact>)

    @Query("SELECT * FROM contacts")
    fun getAllContacts():Flow<List<Contact>>

    @Query("SELECT * FROM blocked_contacts")
    fun getContacts(): Flow<List<BlockedContactData>>


    @Delete
    suspend fun deleteContact(data: BlockedContactData)


    @Query("Select * from blocked_contacts where phone =:number")
    suspend fun findContact(number: String): BlockedContactData?

}