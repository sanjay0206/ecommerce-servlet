package com.ecommerce.services;

import com.ecommerce.entities.Order;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.enums.HttpResponseType;
import com.ecommerce.utilities.DBUtils;
import com.ecommerce.utilities.ResponseDto;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    public static String SQL = "";
    private static final Connection DB_CONN = DBUtils.getConnection();
    public List<Order> getAllOrders() {
        List<Order> ordersList = new ArrayList<>();
        try {
            // Get the list of products from the database
            Statement stmt = DB_CONN.createStatement();
            SQL = "SELECT * FROM orders" ;
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                int quantity = rs.getInt("quantity");
                OrderStatus status = OrderStatus.valueOf(rs.getString("status"));
                Order order = new Order(quantity, status);
                ordersList.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ordersList;
    }

    public ResponseDto createOrder(String payload) {
        ResponseDto responseDto = new ResponseDto();
        try {
            JSONObject request = new JSONObject(payload);
            int customerId = request.getInt("customer_id");
            int productId = request.getInt("product_id");
            int quantity = request.getInt("quantity");
            String status = request.getString("status");
            if (status == null || status.trim().length() == 0) {
                responseDto.setMessage("Order status is required");
                responseDto.setCode(HttpResponseType.BAD_REQUEST.getStatusCode());
            } else if (quantity <= 0) {
                responseDto.setMessage("Order quantity must be a positive number");
                responseDto.setCode(HttpResponseType.BAD_REQUEST.getStatusCode());
            } else {
                SQL = "INSERT INTO orders (customer_id, product_id, quantity, status) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = DB_CONN.prepareStatement(SQL);
                stmt.setInt(1, customerId);
                stmt.setInt(2, productId);
                stmt.setInt(3, quantity);
                stmt.setString(4, status);
                stmt.executeUpdate();
                responseDto.setMessage("Order successfully added");
                responseDto.setCode(HttpResponseType.CREATED.getStatusCode());
            }
        } catch (Exception e) {
            responseDto.setMessage(e.getMessage());
            responseDto.setCode(HttpResponseType.INTERNAL_SERVER_ERROR.getStatusCode());
        }
        return responseDto;
    }

    public ResponseDto updateOrder (String payload, int id) {
        ResponseDto responseDto = new ResponseDto();
        try {
            JSONObject request = new JSONObject(payload);
            int quantity = request.getInt("quantity");
            double price = request.getDouble("price");
            String status = request.getString("status");

            PreparedStatement pstmt = DB_CONN.prepareStatement("SELECT COUNT(*) FROM orders WHERE id = ?");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            // if record is not there it can't be updated
            if (count == 0) {
                responseDto.setMessage("Order with id " + id + " does not exist");
                responseDto.setCode(HttpResponseType.BAD_REQUEST.getStatusCode());
            } else {
                // If the product name does not exist, update the product in the table
                pstmt = DB_CONN.prepareStatement("UPDATE orders SET quantity = ?, status = ? WHERE id = ?");
                pstmt.setInt(1, quantity);
                pstmt.setString(2, status);
                pstmt.setInt(3, id);
                pstmt.executeUpdate();

                // Set the response status to success
                responseDto.setMessage("Order with id " + id + " is updated");
                responseDto.setCode(HttpResponseType.OK.getStatusCode());
            }
        } catch (Exception e) {
            responseDto.setMessage(e.getMessage());
            responseDto.setCode(HttpResponseType.INTERNAL_SERVER_ERROR.getStatusCode());
        }
        return responseDto;
    }

    public ResponseDto deleteOrder (String payload, int id) {
        ResponseDto responseDto = new ResponseDto();
        try {
            PreparedStatement pstmt = DB_CONN.prepareStatement("SELECT COUNT(*) FROM orders WHERE id = ?");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            // if record is not there it can't be deleted
            if (count == 0) {
                responseDto.setMessage("Order with id " + id + " does not exist");
                responseDto.setCode(HttpResponseType.BAD_REQUEST.getStatusCode());
            } else {
                // If the product ID exists, delete the product from the table
                pstmt = DB_CONN.prepareStatement("DELETE FROM orders WHERE id = ?");
                pstmt.setInt(1, id);
                pstmt.executeUpdate();

                // Set the response status to success
                responseDto.setMessage("Order with id " + id + " is deleted");
                responseDto.setCode(HttpResponseType.OK.getStatusCode());
            }
        } catch (Exception e) {
            responseDto.setMessage(e.getMessage());
            responseDto.setCode(HttpResponseType.INTERNAL_SERVER_ERROR.getStatusCode());
        }
        return responseDto;
    }
}
