package com.example.phoneapp.home.data

import android.content.ContentResolver
import android.provider.CallLog
import androidx.core.net.toUri
import com.example.phoneapp.data.ContactsDao
import com.example.phoneapp.home.models.BlockedContactData
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class HomeRepository @Inject constructor(private val dao: ContactsDao) {

    suspend fun deleteContact(data: BlockedContactData) = dao.deleteContact(data)

    fun getAllContacts(): Flow<List<BlockedContactData>> = dao.getContacts()

    fun getCallLogs(contentResolver: ContentResolver):List<HashMap<String,String>> {
        val contactsNumberMap = mutableListOf<HashMap<String,String>>()
        val callLogCursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null)
        if(callLogCursor!=null && callLogCursor.count>0) {
            val nameIndex = callLogCursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
            val numberIndex = callLogCursor.getColumnIndex(CallLog.Calls.CACHED_FORMATTED_NUMBER)
            val contactUriIndex = callLogCursor.getColumnIndex(CallLog.Calls.CACHED_LOOKUP_URI)
            while (callLogCursor.moveToNext()){
                val name = callLogCursor.getString(nameIndex)
                val number = callLogCursor.getString(numberIndex)
                val contactUri = callLogCursor.getString(contactUriIndex)
                val x = hashMapOf("Name" to name,"Number" to number,"Uri" to contactUri)
                contactsNumberMap.add(x)
            }
            callLogCursor.close()
        }
        for(i in contactsNumberMap){
            Timber.e("${i["Name"]}")
            Timber.e("${i["Number"]}")
            Timber.e("${i["Uri"]}")
        }
        return contactsNumberMap
    }

}