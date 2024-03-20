package com.depravo.thesugarshop.network

import com.depravo.thesugarshop.data.CategoryModel
import com.depravo.thesugarshop.data.ConfectionModel
import com.depravo.thesugarshop.data.FullConfectionModel
import com.depravo.thesugarshop.data.LoginCredentials
import com.depravo.thesugarshop.data.LoginResponse
import com.depravo.thesugarshop.data.RegisterCredentials
import com.depravo.thesugarshop.data.UserModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface IConnectAPI {
    @POST("/api/v1/signIn")
    fun signIn(@Body credentials: LoginCredentials): Call<LoginResponse>

    @POST("/api/v1/register")
    fun register(@Body credentials: RegisterCredentials): Call<LoginResponse>

    @GET("/api/v1/catalog")
    fun getCatalog() : Call<List<ConfectionModel>>

    @GET("/api/v1/categories")
    fun getCategories(): Call<List<CategoryModel>>

    @GET("/api/v1/catalog/{category_name}/products")
    fun getCategoryProducts(@Path("category_name") categoryName: String) : Call<List<ConfectionModel>>

    @GET("/api/v1/product/{product_id}")
    fun getProductInfo(@Path("product_id") productId: Int): Call<FullConfectionModel>


    @GET("/api/v1/user/{user_id}")
    fun getUserInfo(@Path("user_id") userId: Int): Call<UserModel>
}