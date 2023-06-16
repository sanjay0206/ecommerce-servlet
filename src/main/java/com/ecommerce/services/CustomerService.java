package com.ecommerce.services;

import com.ecommerce.entities.Customer;
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

public class CustomerService {
    public static String SQL = "";
    private static final Connection DB_CONN = DBUtils.getConnection();

    public List<Customer> getAllCustomers() {
        List<Customer> customersList = new ArrayList<>();
        try {
            // Get the list of products from the database
            Statement stmt = DB_CONN.createStatement();
            SQL = "SELECT * FROM customers" ;
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String shippingAddress = rs.getString("shipping_address");
                Customer customer = new Customer(name, email, shippingAddress);
                customersList.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customersList;
    }

    public ResponseDto createCustomer(String payload) {
        ResponseDto responseDto = new ResponseDto();
        try {
            JSONObject request = new JSONObject(payload);
            String name = request.getString("name");
            String email = request.getString("email");
            String shippingAddress = request.getString("shipping_address");
            if (name == null || name.trim().length() == 0) {
                responseDto.setMessage("Customer name is required");
                responseDto.setCode(HttpResponseType.BAD_REQUEST.getStatusCode());
            } else if (email == null || email.trim().length() == 0) {
                responseDto.setMessage("Customer email is required");
                responseDto.setCode(HttpResponseType.BAD_REQUEST.getStatusCode());
            } else if (shippingAddress == null || shippingAddress.trim().length() == 0) {
                responseDto.setMessage("Customer shipping address is required");
                responseDto.setCode(HttpResponseType.BAD_REQUEST.getStatusCode());
            } else {
                SQL = "INSERT INTO customers (name, email, shipping_address) VALUES (?, ?, ?)";
                PreparedStatement stmt = DB_CONN.prepareStatement(SQL);
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, shippingAddress);
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

    public ResponseDto updateCustomer (String payload, int id) {
        ResponseDto responseDto = new ResponseDto();
        try {
            JSONObject request = new JSONObject(payload);
            String name = request.getString("name");
            String email = request.getString("email");
            String shippingAddress = request.getString("shipping_address");

            PreparedStatement pstmt = DB_CONN.prepareStatement("SELECT COUNT(*) FROM customers WHERE id = ?");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            // if record is not there it can't be updated
            if (count == 0) {
                responseDto.setMessage("Customer with id " + id + " does not exist");
                responseDto.setCode(HttpResponseType.BAD_REQUEST.getStatusCode());
            } else {
                // If the product name does not exist, update the product in the table
                SQL = "INSERT INTO customers (name, email, shipping_address) VALUES (?, ?, ?)";
                PreparedStatement stmt = DB_CONN.prepareStatement(SQL);
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, shippingAddress);

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

    public ResponseDto deleteCustomer (String payload, int id) {
        ResponseDto responseDto = new ResponseDto();
        try {
            PreparedStatement pstmt = DB_CONN.prepareStatement("SELECT COUNT(*) FROM customers WHERE id = ?");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            // if record is not there it can't be deleted
            if (count == 0) {
                responseDto.setMessage("Customer with id " + id + " does not exist");
                responseDto.setCode(HttpResponseType.BAD_REQUEST.getStatusCode());
            } else {
                // If the product ID exists, delete the product from the table
                pstmt = DB_CONN.prepareStatement("DELETE FROM customers WHERE id = ?");
                pstmt.setInt(1, id);
                pstmt.executeUpdate();

                // Set the response status to success
                responseDto.setMessage("Customer with id " + id + " is deleted");
                responseDto.setCode(HttpResponseType.OK.getStatusCode());
            }
        } catch (Exception e) {
            responseDto.setMessage(e.getMessage());
            responseDto.setCode(HttpResponseType.INTERNAL_SERVER_ERROR.getStatusCode());
        }
        return responseDto;
    }
}
