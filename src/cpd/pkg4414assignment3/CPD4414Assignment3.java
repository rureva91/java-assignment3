/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpd.pkg4414assignment3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.security.krb5.Credentials;

/**
 *
 * @author c0643680
 */
public class CPD4414Assignment3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        getConnection();
    }

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
}
