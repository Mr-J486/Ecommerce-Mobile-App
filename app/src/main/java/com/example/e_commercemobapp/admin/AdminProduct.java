package com.example.e_commercemobapp.admin;

public class AdminProduct {

    private String id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String categoryName;
    private String imageUrl;

    public AdminProduct() {}

    public AdminProduct(String id, String name, String description, double price, int quantity, String categoryName, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.categoryName = categoryName;
        this.imageUrl = imageUrl;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getCategoryName() { return categoryName; }
    public String getImageUrl() { return imageUrl; }
}
