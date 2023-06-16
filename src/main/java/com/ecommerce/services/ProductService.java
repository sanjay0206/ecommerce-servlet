package com.ecommerce.services;

import com.ecommerce.entities.Product;
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

public class ProductService {
    public static String SQL = "";
    private static final Connection DB_CONN = DBUtils.getConnection();
    public List<Product> getAllProducts() {
        List<Product> productsList = new ArrayList<>();
        try {
            // Get the list of products from the database
            Statement stmt = DB_CONN.createStatement();
            SQL = "SELECT * FROM products";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String description = rs.getString("description");
                Product product = new Product(name, price, description);
                productsList.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productsList;
    }

    public ResponseDto createProduct(String payload) {
        ResponseDto responseDto = new ResponseDto();
        try {
            JSONObject request = new JSONObject(payload);
            String name = request.get("name").toString();
            double price = request.getDouble("price");
            String description = request.get("description").toString();
            if (name == null || name.trim().length() == 0) {
                responseDto.setMessage("Product name is required");
                responseDto.setCode(HttpResponseType.BAD_REQUEST.getStatusCode());
            } else if (price <= 0) {
                responseDto.setMessage("Product price must be a positive number");
                responseDto.setCode(HttpResponseType.BAD_REQUEST.getStatusCode());
            } else if (description == null || description.trim().length() == 0) {
                responseDto.setMessage("Product description is required");
                responseDto.setCode(HttpResponseType.BAD_REQUEST.getStatusCode());
            } else {
                SQL = "INSERT INTO products (name, price, description) VALUES (?, ?, ?)";
                PreparedStatement pstmt = DB_CONN.prepareStatement(SQL);
                pstmt.setString(1, name);
                pstmt.setDouble(2, price);
                pstmt.setString(3, description);
                pstmt.executeUpdate();
                responseDto.setMessage("Product successfully added");
                responseDto.setCode(HttpResponseType.CREATED.getStatusCode());
            }
        } catch (Exception e) {
            responseDto.setMessage(e.getMessage());
            responseDto.setCode(HttpResponseType.INTERNAL_SERVER_ERROR.getStatusCode());
        }
        return responseDto;
    }

    public ResponseDto updateProduct (String payload, int id) {
        ResponseDto responseDto = new ResponseDto();
        try {
            JSONObject request = new JSONObject(payload);
            String name = request.get("name").toString();
            double price = request.getDouble("price");
            String description = request.get("description").toString();

            SQL = "SELECT COUNT(*) FROM products WHERE id = ?";
            PreparedStatement pstmt = DB_CONN.prepareStatement(SQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            // if record is not there it can't be updated
            if (count == 0) {
                responseDto.setMessage("Product with id " + id + " does not exist");
                responseDto.setCode(HttpResponseType.BAD_REQUEST.getStatusCode());
            } else {
                // If the product name does not exist, update the product in the table
                SQL = "UPDATE products SET name = ?, price = ?, description = ? WHERE id = ?";
                pstmt = DB_CONN.prepareStatement(SQL);
                pstmt.setString(1, name);
                pstmt.setDouble(2, price);
                pstmt.setString(3, description);
                pstmt.setInt(4, id);
                pstmt.executeUpdate();

                // Set the response status to success
                responseDto.setMessage("Product with id " + id + " is updated");
                responseDto.setCode(HttpResponseType.OK.getStatusCode());
            }
        } catch (Exception e) {
            responseDto.setMessage(e.getMessage());
            responseDto.setCode(HttpResponseType.INTERNAL_SERVER_ERROR.getStatusCode());
        }
        return responseDto;
    }

    public ResponseDto deleteProduct (String payload, int id) {
        ResponseDto responseDto = new ResponseDto();
        try {
            SQL = "SELECT COUNT(*) FROM products WHERE id = ?";
            PreparedStatement pstmt = DB_CONN.prepareStatement(SQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            // if record is not there it can't be deleted
            if (count == 0) {
                responseDto.setMessage("Product with id " + id + " does not exist");
                responseDto.setCode(HttpResponseType.BAD_REQUEST.getStatusCode());
            } else {
                // If the product ID exists, delete the product from the table
                SQL = "DELETE FROM products WHERE id = ?";
                pstmt = DB_CONN.prepareStatement(SQL);
                pstmt.setInt(1, id);
                pstmt.executeUpdate();

                // Set the response status to success
                responseDto.setMessage("Product with id " + id + " is deleted");
                responseDto.setCode(HttpResponseType.OK.getStatusCode());
            }
        } catch (Exception e) {
            responseDto.setMessage(e.getMessage());
            responseDto.setCode(HttpResponseType.INTERNAL_SERVER_ERROR.getStatusCode());
        }
        return responseDto;
    }
}
