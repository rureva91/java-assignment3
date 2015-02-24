/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import cpd.pkg4414assignment3.CPD4414Assignment3;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sun.security.krb5.Credentials;

/**
 *
 * @author c0643680
 */
@WebServlet("/product")

public class productservlet extends HttpServlet {

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found exception! " + e.getMessage());
        }

        String url = "jdbc:mysql://localhost/product";
        try {
            connection = DriverManager.getConnection(url,
                    "root", "");

            String query = "SELECT * FROM productdetails";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                System.out.printf("%d\t%s\t%s\t%d\n", rs.getInt("productID"), rs.getString("name"), rs.getString("description"), rs.getInt("quantity"));
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Failed to Connect! " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Content-Type", "text/plain-text");
        try (PrintWriter out = response.getWriter()) {
            if (!request.getParameterNames().hasMoreElements()) {
                // There are no parameters at all
                out.println(getResults("SELECT * FROM productdetails"));
            } else {
                // There are some parameters
                int id = Integer.parseInt(request.getParameter("id"));
                out.println(getResults("SELECT * FROM productdetails WHERE id = ?", String.valueOf(id)));
            }
        } catch (IOException ex) {
            Logger.getLogger(productservlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        Set<String> keySet = request.getParameterMap().keySet();
        try (PrintWriter out = response.getWriter()) {
            if (keySet.contains("productID") && keySet.contains("name") && keySet.contains("description") && keySet.contains("quantity")) {
                // There are some parameters  
                String productID = request.getParameter("productID");
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                String quantity = request.getParameter("quantity");
                doUpdate("INSERT INTO productdetails (productID, name, description, quantity) VALUES (?, ?, ?, ?)", productID, name, description, quantity);
            } else {
                // There are no parameters at all
                out.println("Error: Not enough data to input. Please use a URL of the form /servlet?name=XXX&age=XXX");
            }
        } catch (IOException ex) {
            Logger.getLogger(productservlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int doUpdate(String query, String... params) {
        int numChanges = 0;
        try (Connection conn = getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }
            numChanges = pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(productservlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numChanges;
    }
        private String getResults(String query, String... params) {
        StringBuilder sb = new StringBuilder();
        try (Connection conn = CPD4414Assignment3.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                sb.append(String.format("%s\t%s\t%s\n", rs.getInt("id"), rs.getString("name"), rs.getInt("age")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(productservlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }
}
