package com.ecommerce.entities;

public class Customer {
    private int id;
    private String name;
    private String email;
    private String shippingAddress;

    public Customer() {
    }

    public Customer(String name, String email, String shippingAddress) {
        this.name = name;
        this.email = email;
        this.shippingAddress = shippingAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", shippingAddress='" + shippingAddress + '\'' +
                '}';
    }
}
