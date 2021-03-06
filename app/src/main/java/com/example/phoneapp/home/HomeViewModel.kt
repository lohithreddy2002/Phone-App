package com.example.phoneapp.home

import android.content.ContentResolver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phoneapp.home.data.HomeRepository
import com.example.phoneapp.home.models.BlockedContactData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: HomeRepository) : ViewModel() {


    private val _blockedNumber = MutableLiveData<List<BlockedContactData>>(listOf())
    val blockedNumbers = _blockedNumber


    fun deleteNumber(contactData: BlockedContactData) {
        viewModelScope.launch {
            repo.deleteContact(contactData)

        }
    }


    fun getAllBlockedContacts(): Flow<List<BlockedContactData>> {
        return repo.getAllContacts()
    }

    fun getCallLogs(contentResolver: ContentResolver) {
        viewModelScope.launch {
            val listAsync = async { repo.getCallLogs(contentResolver) }
            val list = listAsync.await()
        }
    }


}