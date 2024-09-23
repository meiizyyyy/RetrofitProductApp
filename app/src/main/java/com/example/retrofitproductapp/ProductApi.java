package com.example.retrofitproductapp;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
public interface ProductApi {
    @GET("products")
    Call<List<Product>> getProducts();
    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") int id);
    @POST("products")
    Call<Void> addProduct(@Body Product product);

    @PUT("products/{id}")
    Call<Void> updateProduct(@Path("id") int id, @Body Product product);
    @DELETE("products/{id}")
    Call<Void> deleteProduct(@Path("id") int id);
}