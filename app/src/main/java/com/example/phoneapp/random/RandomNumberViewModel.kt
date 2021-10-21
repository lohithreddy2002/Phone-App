package com.example.phoneapp.random

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phoneapp.BlockedContactData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomNumberViewModel @Inject constructor(private val repo: RandomNumberRepository):ViewModel() {
    fun insert(phoneNumber:String,name:String){
        viewModelScope.launch {
            repo.insert(BlockedContactData(name = name, phone = phoneNumber))
        }
    }
}