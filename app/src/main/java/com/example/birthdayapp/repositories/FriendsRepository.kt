package com.example.birthdayapp.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.birthdayapp.models.Friend
import com.example.birthdayapp.repositories.FriendService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Date
import kotlin.reflect.typeOf

class FriendsRepository {

    private val baseUrl = "https://birthdaysrest.azurewebsites.net/api/"
    // the specific (collection) part of the URL is on the individual methods in the interface FrindstoreService

    //"http://anbo-restserviceproviderfriends.azurewebsites.net/Service1.svc/"
    private val friendService: FriendService
    val friendsLiveData: MutableLiveData<List<Friend>> = MutableLiveData<List<Friend>>()
    val errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val updateMessageLiveData: MutableLiveData<String> = MutableLiveData()




    init {
        //val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val build: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create()) // GSON
            //.addConverterFactory(KotlinJsonAdapterFactory)
            //.addConverterFactory(MoshiConverterFactory.create(moshi)) // Moshi, added to Gradle dependencies
            .build()
        friendService = build.create(FriendService::class.java)
        getFriends()
    }

    fun onFailureMessage(t: Throwable){
        errorMessageLiveData.postValue(t.message)
        Log.d("APPLE","my method. " + t.message!!)
    }

    fun responceFail(response: Response<Friend>){
        val message = response.code().toString() + " " + response.message()
        errorMessageLiveData.postValue(message)
        Log.d("APPLE", message)
    }
    fun listResponceFail(response: Response<List<Friend>>){
        val message = response.code().toString() + " " + response.message()
        errorMessageLiveData.postValue(message)
        Log.d("APPLE", message)
    }

    fun checkResponse(response: Response<Friend>, operation: String){
        if (response.isSuccessful) {
            Log.d("APPLE", "$operation: " + response.body())
            updateMessageLiveData.postValue("$operation: " + response.body())
        } else {
            val message = response.code().toString() + " " + response.message()
            errorMessageLiveData.postValue(message)
            Log.d("APPLE", message)
        }
    }

    fun getFriends() {
        friendService.getAllFriends().enqueue(object : Callback<List<Friend>> {
            override fun onResponse(call: Call<List<Friend>>, response: Response<List<Friend>>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", response.body().toString() + "repo")
                    val b: List<Friend>? = response.body()
                    friendsLiveData.postValue(b!!)
                    errorMessageLiveData.postValue("")
                } else {
                    listResponceFail(response)
                }
            }

            override fun onFailure(call: Call<List<Friend>>, t: Throwable) {
                onFailureMessage(t)
            }
        })
    }

    fun add(friend: Friend) {
        friendService.saveFriend(friend).enqueue(object : Callback<Friend> {
            override fun onResponse(call: Call<Friend>, response: Response<Friend>) {
                checkResponse(response, "Added")
            }

            override fun onFailure(call: Call<Friend>, t: Throwable) {
                onFailureMessage(t)
            }
        })
    }
    fun delete(id: Int) {
        friendService.deleteFriend(id).enqueue(object : Callback<Friend> {
            override fun onResponse(call: Call<Friend>, response: Response<Friend>) {
                checkResponse(response, "Deleted")
            }

            override fun onFailure(call: Call<Friend>, t: Throwable) {
                onFailureMessage(t)
            }
        })
    }


    fun update(friend: Friend) {
        friendService.updateFriend(friend.id, friend).enqueue(object : Callback<Friend> {
            override fun onResponse(call: Call<Friend>, response: Response<Friend>) {
                checkResponse(response, "Updated")
            }

            override fun onFailure(call: Call<Friend>, t: Throwable) {
                onFailureMessage(t)
            }
        })
    }

    fun sortByName() {
        friendsLiveData.value = friendsLiveData.value?.sortedBy { it.name.uppercase() }
    }

    fun sortByNameDescending() {
        friendsLiveData.value = friendsLiveData.value?.sortedByDescending { it.name.uppercase() }
    }

    fun sortByAge() {
        friendsLiveData.value = friendsLiveData.value?.sortedBy { it.age }
    }

    fun sortByAgeDescending() {
        friendsLiveData.value = friendsLiveData.value?.sortedByDescending { it.age }
    }
    /*
    fun sortByBirth() {
        val year = friendsLiveData.value?.sortedBy { it.birthDay }
    }
    fun sortByBirthDescending() {
        val year = friendsLiveData.value?.sortedByDescending { it.birthDay }
        Log.d("APPLE", "BDay")

    }*/

    fun filter(condition: String){
        friendService.getAllFriends().enqueue(object : Callback<List<Friend>> {
            override fun onResponse(call: Call<List<Friend>>, response: Response<List<Friend>>) {
                if (response.isSuccessful) {
                    val b: List<Friend>? = response.body()
                    val tryNum: String = condition

                    if(tryNum.toIntOrNull() != null) {
                        friendsLiveData.value = b?.filter { friend -> friend.age == tryNum.toInt()}
                    }
                    else {
                        friendsLiveData.value = b?.filter { friend -> friend.name.uppercase().contains(condition.uppercase()) }
                    }
                } else {
                    listResponceFail(response)
                }
            }

            override fun onFailure(call: Call<List<Friend>>, t: Throwable) {
                onFailureMessage(t)
            }
        })
    }

}