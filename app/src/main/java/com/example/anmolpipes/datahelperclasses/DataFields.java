package com.example.anmolpipes.datahelperclasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataFields implements Serializable {
    private int id;
    private int productId;
    private double totalAmount;
    private String delivery;

    //All products
    private int customerId;
    private int productsId;
    private int singleProductId;
    private String productName;
    private String size;
    private double quantity;
    private double rate;
    private double totalAmountOfAllProduct;
    private boolean isExpanded;

    public DataFields() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public int getSingleProductId() {
        return singleProductId;
    }

    public void setSingleProductId(int singleProductId) {
        this.singleProductId = singleProductId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public DataFields(int customerId, int productId, String productName, String productSize, double quantity, double rate, double totalAmountOfAllProduct){
        this.customerId = customerId;
        this.productId = productId;
        this.productName = productName;
        this.size = productSize;
        this.quantity = quantity;
        this.rate = rate;
        this.totalAmountOfAllProduct = totalAmountOfAllProduct;
    }

    public DataFields(int customerId,int productId, int productsId, String productName, String productSize, double quantity, double rate, double totalAmountOfAllProduct, String delivery, double totalAmount) {
        this.productId = productId;
        this.customerId = customerId;
        this.productsId = productsId;
        this.productName = productName;
        this.size = productSize;
        this.quantity = quantity;
        this.rate = rate;
        this.totalAmountOfAllProduct = totalAmountOfAllProduct;
        this.delivery = delivery;
        this.totalAmount = totalAmount;
    }


    public DataFields(String productName, double quantity, double rate, double totalAmount) {
        this.productName = productName;
        this.quantity = quantity;
        this.rate = rate;
        this.totalAmount = totalAmount;
        this.isExpanded = false;
    }

    // constructor for update data
    public  DataFields(String productName, String size, double quantity, double rate, double totalAmountOfAllProduct) {
        this.productName = productName;
        this.size = size;
        this.quantity = quantity;
        this.rate = rate;
        this.totalAmountOfAllProduct = totalAmountOfAllProduct;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getProductsId() {
        return productsId;
    }

    public void setProductsId(int productsId) {
        this.productsId = productsId;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTotalAmountOfAllProduct() {
        return totalAmountOfAllProduct;
    }

    public void setTotalAmountOfAllProduct(double totalAmountOfAllProduct) {
        this.totalAmountOfAllProduct = totalAmountOfAllProduct;
    }

    public List<DataFields> getLists(int no){
        List<DataFields> data = new ArrayList<>();
        for (int i = 0; i<no; i++){
            data.add(new DataFields("pipe"+i, i+1, i+200, 200+(i*1)));
        }
        return data;
    }

}
