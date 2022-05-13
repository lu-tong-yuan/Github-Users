package com.lu.githubusers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lu.githubusers.model.UsersList
import com.lu.githubusers.presenter.Presenter
import com.lu.githubusers.view.ListView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(),ListView,ListOfUsersAdapter.OnItemClickListener{
    private lateinit var rvUsers : RecyclerView
    private lateinit var userList: ArrayList<UsersList>
    private lateinit var presenter: Presenter
    private lateinit var usersApi: UsersApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvUsers = findViewById(R.id.rvUsers)

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .connectTimeout(30, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://static.mixerbox.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        usersApi = retrofit.create(UsersApi::class.java)

        rvUsers.layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.VERTICAL,false)

        rvUsers.setHasFixedSize(true)

        userList = arrayListOf<UsersList>()

        getList(0,20)





    }

    override fun getInto() {

    }

    override fun getList(since: Int?, per_page: Int?) {
        val call: Call<List<UsersList>> = usersApi.getUsers(since,20)
        call.enqueue(object : Callback<List<UsersList>?> {
            override fun onResponse(call: Call<List<UsersList>?>, response: Response<List<UsersList>?>) {
                if (!response.isSuccessful()) {
                    Log.d("MainFragment", "Code" + response.code())
                    return
                }
                userList = response.body() as ArrayList<UsersList>
                showUsers(userList)
            }

            override fun onFailure(call: Call<List<UsersList>?>, t: Throwable) {}
        })
    }

    override fun onItemClick(user: UsersList) {
        val intent = Intent()
        intent.putExtra("username",user.login)
        intent.setClass(
            this@MainActivity,
            UserDetailAcitvity::class.java
        )
        startActivity(intent)
    }

    private fun showUsers(userList : List<UsersList>?) {
        if (userList == null || userList.isEmpty()) {
            //Toast.makeText(activity,"NoNewsFound",Toast.LENGTH_SHORT).show();
        }
        val usersAdapter: ListOfUsersAdapter =
            rvUsers.getAdapter() as ListOfUsersAdapter
        if (usersAdapter == null) {
            rvUsers.setAdapter(ListOfUsersAdapter(this, userList))
        } else {
            usersAdapter.setUsers(userList!!)
            usersAdapter.notifyDataSetChanged()
        }
    }
}