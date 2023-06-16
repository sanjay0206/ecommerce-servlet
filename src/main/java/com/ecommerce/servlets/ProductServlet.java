package com.ecommerce.servlets;

import com.ecommerce.entities.Product;
import com.ecommerce.enums.HttpResponseType;
import com.ecommerce.enums.Status;
import com.ecommerce.services.ProductService;
import com.ecommerce.utilities.ResponseDto;
import com.ecommerce.utilities.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;
@WebServlet("/product")
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    static JSONObject responseDto = new JSONObject();
    private static final ProductService productService = new ProductService();
    static ResponseMessage responseMessage;
    static ObjectMapper MAPPER = new ObjectMapper();

    static String message = "";
    static int code = 0;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<Product> products = productService.getAllProducts();
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            if (products.isEmpty()) {
                message = "Oops! No products are present in our database";
                responseMessage = new ResponseMessage(Status.FAILURE, message);
                out.println(MAPPER.writeValueAsString(responseMessage));
            } else {
                out.println(MAPPER.writeValueAsString(products));
            }
            response.setStatus(HttpResponseType.OK.getStatusCode());
        } catch (Exception e) {
            message = e.getMessage();
            responseMessage = new ResponseMessage(Status.FAILURE, message);
            response.setStatus(HttpResponseType.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String payload = new BufferedReader(new InputStreamReader(request.getInputStream()))
                    .lines().collect(Collectors.joining(System.lineSeparator()));
            System.out.println(payload);
            ResponseDto resp = productService.createProduct(payload);
            code = resp.getCode();
            message = resp.getMessage();
            if (code == HttpResponseType.CREATED.getStatusCode()) {
                responseMessage = new ResponseMessage(Status.SUCCESS, message);
                response.setStatus(code);
            } else {
                responseMessage = new ResponseMessage(Status.FAILURE, message);
                response.setStatus(code);
            }
        } catch (Exception e) {
            message = e.getMessage();
            responseMessage = new ResponseMessage(Status.FAILURE, message);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        out.println(MAPPER.writeValueAsString(responseMessage));
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String payload = new BufferedReader(new InputStreamReader(request.getInputStream()))
                    .lines().collect(Collectors.joining(System.lineSeparator()));
            System.out.println(payload);

            int id = Integer.parseInt(request.getParameter("id"));
            ResponseDto resp = productService.updateProduct(payload, id);
            code = resp.getCode();
            message = resp.getMessage();
            if (code == HttpResponseType.OK.getStatusCode()) {
                responseMessage = new ResponseMessage(Status.SUCCESS, message);
                response.setStatus(code);
            } else {
                responseMessage = new ResponseMessage(Status.FAILURE, message);
                response.setStatus(code);
            }
        } catch (Exception e) {
            message = e.getMessage();
            responseMessage = new ResponseMessage(Status.FAILURE, message);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        out.println(MAPPER.writeValueAsString(responseMessage));
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String payload = new BufferedReader(new InputStreamReader(request.getInputStream()))
                    .lines().collect(Collectors.joining(System.lineSeparator()));
            System.out.println(payload);

            int id = Integer.parseInt(request.getParameter("id"));
            ResponseDto resp = productService.deleteProduct(payload, id);
            code = resp.getCode();
            message = resp.getMessage();
            if (code == HttpResponseType.OK.getStatusCode()) {
                responseMessage = new ResponseMessage(Status.SUCCESS, message);
                response.setStatus(code);
            } else {
                responseMessage = new ResponseMessage(Status.FAILURE, message);
                response.setStatus(code);
            }
        } catch (Exception e) {
            message = e.getMessage();
            responseMessage = new ResponseMessage(Status.FAILURE, message);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        out.println(MAPPER.writeValueAsString(responseMessage));
    }
}
