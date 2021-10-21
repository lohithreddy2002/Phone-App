package com.example.phoneapp.contacts

data class Contact(val id: String, val name:String) {
    var numbers = ArrayList<String>()
}