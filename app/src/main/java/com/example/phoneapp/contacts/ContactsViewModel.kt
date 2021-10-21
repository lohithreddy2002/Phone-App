package com.example.phoneapp.contacts

import android.app.Application
import android.content.ContentResolver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phoneapp.BlockedContactData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(private val repo: ContactsRepository) : ViewModel() {

    val list = MutableLiveData<ArrayList<Contact>>()

    fun getcontacts(contentResolver: ContentResolver) {
        viewModelScope.launch {
            val contactsListAsync = async { repo.getPhoneContacts(contentResolver) }
            val contactNumbersAsync = async { repo.getContactNumbers(contentResolver) }
            val contacts = contactsListAsync.await()
            val contactNumbers = contactNumbersAsync.await()

            contacts.forEach {
                contactNumbers[it.id]?.let { numbers ->
                    it.numbers = numbers

                }
            }
            list.postValue(contacts)
            Timber.e("$contacts")
        }
    }

    fun insert(contactData: Contact) {
        var number = contactData.numbers[0].replace("(", "").replace(")", "").replace("-", "")
            .replace(" ", "")
        if (number.length > 10){
            number = number.subSequence(3,number.length).toString()
        }
        viewModelScope.launch {
            repo.insertContact(
                BlockedContactData(
                    contactData.id.toInt(),
                    contactData.name,
                    number
                )
            )
        }
    }


}