package com.example.retrofitproductapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.List;

public interface ProductApi {
    @GET("products")
    Call<List<Product>> getProducts(); // Lấy danh sách sản phẩm

    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") int id); // Lấy sản phẩm theo ID

    @POST("products")
    Call<Product> createProduct(@Body Product product); // Tạo mới sản phẩm

    @PUT("products/{id}")
    Call<Product> updateProduct(@Path("id") int id, @Body Product product); // Cập nhật sản phẩm

    @DELETE("products/{id}")
    Call<Void> deleteProduct(@Path("id") int id); // Xóa sản phẩm
}