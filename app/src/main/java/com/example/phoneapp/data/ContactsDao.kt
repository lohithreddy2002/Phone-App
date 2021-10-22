package com.example.phoneapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.phoneapp.home.models.BlockedContactData
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(data: BlockedContactData)

    @Query("SELECT * FROM CONTACTS")
    fun getContacts(): Flow<List<BlockedContactData>>


    @Delete
    suspend fun deleteContact(data: BlockedContactData)


    @Query("Select * from contacts where phone =:number")
    suspend fun findContact(number: String): BlockedContactData?

}