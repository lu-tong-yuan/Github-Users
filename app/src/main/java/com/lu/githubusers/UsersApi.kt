package com.lu.githubusers

import com.lu.githubusers.model.UsersList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UsersApi {
    @GET("users")
    fun getUsers(@Query("since") since : Int?, @Query("per_page") per_page:Int?): Call<List<UsersList>>

    @GET("users/{username}")
    fun getUser(@Path("username") username:String?): Call<UsersList>
}