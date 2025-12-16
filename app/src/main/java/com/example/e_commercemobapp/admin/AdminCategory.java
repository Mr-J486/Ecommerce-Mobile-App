package com.example.e_commercemobapp.admin;

public class AdminCategory {
    private String id;
    private String name;
    private String description;

    public AdminCategory() {}

    public AdminCategory(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
}
