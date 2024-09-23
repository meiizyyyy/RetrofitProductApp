package com.example.retrofitproductapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, this);
        recyclerView.setAdapter(productAdapter);

        // Thiết lập button tạo sản phẩm
        Button buttonCreate = findViewById(R.id.buttonCreate);
        buttonCreate.setOnClickListener(v -> showCreateProductDialog());

        // Thiết lập button fetch sản phẩm
        Button buttonFetch = findViewById(R.id.buttonFetch);
        buttonFetch.setOnClickListener(v -> fetchProducts());

        fetchProducts();
    }

    private void fetchProducts() {
        ProductApi productApi = RetrofitClient.getClient().create(ProductApi.class);
        Call<List<Product>> call = productApi.getProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body());
                    productAdapter.notifyDataSetChanged();
                } else {
                    Log.e("MainActivity", "Response not successful");
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("MainActivity", "API call failed: " + t.getMessage());
            }
        });
    }

    private void showCreateProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create New Product");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_product, null);
        builder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextPrice = dialogView.findViewById(R.id.editTextPrice);

        builder.setPositiveButton("Create", (dialog, which) -> {
            String name = editTextName.getText().toString();
            String priceStr = editTextPrice.getText().toString();
            double price = Double.parseDouble(priceStr);

            Product newProduct = new Product(name, price);
            createProduct(newProduct);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void createProduct(Product product) {
        ProductApi productApi = RetrofitClient.getClient().create(ProductApi.class);
        Call<Product> call = productApi.createProduct(product);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(MainActivity.this, "Product created successfully", Toast.LENGTH_SHORT).show();
                    fetchProducts(); // Tải lại danh sách sản phẩm
                } else {
                    Toast.makeText(MainActivity.this, "Failed to create product", Toast.LENGTH_SHORT).show();
                    fetchProducts();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(MainActivity.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                fetchProducts();
            }
        });
    }

    @Override
    public void onEditClick(Product product) {
        showEditProductDialog(product);
    }

    private void showEditProductDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_product, null);
        builder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextPrice = dialogView.findViewById(R.id.editTextPrice);

        editTextName.setText(product.getName());
        editTextPrice.setText(String.valueOf(product.getPrice()));

        builder.setTitle("Edit Product");
        builder.setPositiveButton("Update", (dialog, which) -> {
            String updatedName = editTextName.getText().toString();
            double updatedPrice = Double.parseDouble(editTextPrice.getText().toString());

            product.setName(updatedName);
            product.setPrice(updatedPrice);

            ProductApi productApi = RetrofitClient.getClient().create(ProductApi.class);
            Call<Product> call = productApi.updateProduct(product.getId(), product);

            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                        fetchProducts();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to update product", Toast.LENGTH_SHORT).show();
                        fetchProducts();
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    fetchProducts();
                }
            });
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onDeleteClick(int productId) {
        ProductApi productApi = RetrofitClient.getClient().create(ProductApi.class);
        Call<Void> call = productApi.deleteProduct(productId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Product deleted", Toast.LENGTH_SHORT).show();
                    fetchProducts();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to delete product", Toast.LENGTH_SHORT).show();
                    fetchProducts();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("MainActivity", "API call failed: " + t.getMessage());
                fetchProducts();
            }
        });
    }
}
