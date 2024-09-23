package com.example.retrofitproductapp;

public class Product {
    private int id;
    private String name;
    private double price;

    // Constructors
    public Product() {}

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    // Getters và Setters
    public int getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }
}