package com.example.anmolpipes.datahelperclasses;

public class Customer {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String storeName;
    private String date;
    private double totalAmount;
    private boolean expandable;

    public Customer() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Customer(int id, String name, String email, String phone, String storeName, String date, double totalAmount) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.storeName = storeName;
        this.expandable = false;
        this.date = date;
        this.totalAmount = totalAmount;
    }

    public Customer(String name, String email, String phone, String storeName) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.storeName = storeName;
        this.expandable = expandable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public Customer(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
