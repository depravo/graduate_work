package com.depravo.thesugarshop.network

import android.util.Log
import com.depravo.thesugarshop.data.CategoryModel
import com.depravo.thesugarshop.data.ConfectionModel
import com.depravo.thesugarshop.data.FullConfectionModel
import com.depravo.thesugarshop.data.LoginCredentials
import com.depravo.thesugarshop.data.LoginResponse
import com.depravo.thesugarshop.data.RegisterCredentials
import com.depravo.thesugarshop.data.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiManager {

    fun signIn(login_cred: LoginCredentials, onResult: (LoginResponse?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(IConnectAPI::class.java)
        retrofit.signIn(login_cred).enqueue(
            object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    var result = response.body()
                    onResult(result)
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

    fun register(register_cred: RegisterCredentials, onResult: (LoginResponse?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(IConnectAPI::class.java)
        retrofit.register(register_cred).enqueue(
            object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    var result = response.body()
                    onResult(result)
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

    fun getCatalog(onResult: (List<ConfectionModel>?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(IConnectAPI::class.java)
        retrofit.getCatalog().enqueue(
            object : Callback<List<ConfectionModel>> {
                override fun onResponse(
                    call: Call<List<ConfectionModel>>,
                    response: Response<List<ConfectionModel>>
                ) {
                    var result = response.body()
                    onResult(result)
                }

                override fun onFailure(call: Call<List<ConfectionModel>>, t: Throwable) {
                    onResult(null)
                    Log.d("MYTAG", t.message.toString())
                }
            }
        )
    }

    fun getCategories(onResult: (List<CategoryModel>?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(IConnectAPI::class.java)
        retrofit.getCategories().enqueue(
            object : Callback<List<CategoryModel>> {
                override fun onResponse(
                    call: Call<List<CategoryModel>>,
                    response: Response<List<CategoryModel>>
                ) {
                    var result = response.body()
                    onResult(result)
                }

                override fun onFailure(call: Call<List<CategoryModel>>, t: Throwable) {
                    onResult(null)
                    Log.d("MYTAG", t.message.toString())
                }
            }
        )
    }

    fun getCategoryProducts(categoryName: String, onResult: (List<ConfectionModel>?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(IConnectAPI::class.java)
        retrofit.getCategoryProducts(categoryName).enqueue(
            object : Callback<List<ConfectionModel>> {
                override fun onResponse(
                    call: Call<List<ConfectionModel>>,
                    response: Response<List<ConfectionModel>>
                ) {
                    var result = response.body()
                    onResult(result)
                }

                override fun onFailure(call: Call<List<ConfectionModel>>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

    fun getProductInfo(productId: Int, onResult: (FullConfectionModel?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(IConnectAPI::class.java)
        retrofit.getProductInfo(productId).enqueue(
            object : Callback<FullConfectionModel> {
                override fun onResponse(
                    call: Call<FullConfectionModel>,
                    response: Response<FullConfectionModel>
                ) {
                    var result = response.body()
                    onResult(result)
                }

                override fun onFailure(call: Call<FullConfectionModel>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }


    fun getUserInfo(userId: Int, onResult: (UserModel?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(IConnectAPI::class.java)
        retrofit.getUserInfo(userId).enqueue(
            object : Callback<UserModel> {
                override fun onResponse(
                    call: Call<UserModel>,
                    response: Response<UserModel>
                ) {
                    var result = response.body()
                    onResult(result)
                }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }
}