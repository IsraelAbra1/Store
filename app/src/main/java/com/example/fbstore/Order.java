package com.example.fbstore;

public class Order {
    public String orderId;
    public String productName;
    public int quantity;
    public double price;
    public boolean delivered;

    // Required empty constructor for Firebase
    public Order() {
    }

    public Order(String orderId, String productName, int quantity, double price, boolean delivered) {
        this.orderId = orderId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.delivered = delivered;

    }

    // Optional: Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public boolean isDelivered() {
        return delivered;
    }



    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }


}

