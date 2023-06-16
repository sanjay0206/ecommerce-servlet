package com.ecommerce.entities;

import com.ecommerce.enums.OrderStatus;

public class Order {
    private int id;
    private int customer_id;
    private int product_id;
    private int quantity;
    private OrderStatus orderStatus;

    public Order() {
    }

    public Order(int quantity, OrderStatus orderStatus) {
        this.quantity = quantity;
        this.orderStatus = orderStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customer_id=" + customer_id +
                ", product_id=" + product_id +
                ", quantity=" + quantity +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
