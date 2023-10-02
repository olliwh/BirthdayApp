package com.example.birthdayapp.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.birthdayapp.repositories.FriendsRepository

class FriendsViewModel : ViewModel() {
    private val repository = FriendsRepository()
    val friendsLiveData: LiveData<List<Friend>> = repository.friendsLiveData
    val errorMessageLiveData: LiveData<String> = repository.errorMessageLiveData
    val updateMessageLiveData: LiveData<String> = repository.updateMessageLiveData

    init {
        reload()

    }

    fun reload() {
        repository.getFriends()
    }

    operator fun get(index: Int): Friend? {
        return friendsLiveData.value?.get(index)
    }

    fun add(friend: Friend) {
        repository.add(friend)
    }

    fun delete(id: Int) {
        repository.delete(id)
    }

    fun update(friend: Friend) {
        repository.update(friend)
    }

    fun sortByName() {
        repository.sortByName()
    }

    fun sortByNameDescending() {
        repository.sortByNameDescending()
    }

    fun sortByAge() {
        repository.sortByAge()
    }

    fun sortByAgeDescending() {
        repository.sortByAgeDescending()
    }/*
    fun sortByBirth() {
        repository.sortByBirth()
    }
    fun sortByBirthDescending() {
        repository.sortByBirthDescending()
    }*/

    fun filter(condition: String) {
        repository.filter(condition)
    }
}