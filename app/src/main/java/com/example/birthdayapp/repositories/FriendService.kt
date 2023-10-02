package com.example.birthdayapp.repositories

import com.example.birthdayapp.models.Friend
import retrofit2.Call
import retrofit2.http.*


interface FriendService {

    @GET("persons")
    fun getAllFriends(): Call<List<Friend>>

    @GET("persons/{friendId}")
    fun getFriendById(@Path("friendId") friendId: Int): Call<Friend>

    @POST("persons")
    fun saveFriend(@Body friend: Friend): Call<Friend>

    @DELETE("persons/{id}")
    fun deleteFriend(@Path("id") id: Int): Call<Friend>

    @PUT("persons/{id}")
    fun updateFriend(@Path("id") id: Int, @Body friend: Friend): Call<Friend>
}