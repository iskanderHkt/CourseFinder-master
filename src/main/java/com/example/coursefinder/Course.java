package com.example.coursefinder;

public class Course {
    private int id;
    private String title;
    private String address;
    private int price;
    private String email;
    private String description;

    public Course() {
    }

    public Course(int id, String title, String address, int price, String email, String description) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.price = price;
        this.email = email;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
