/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapp;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Supplier;

/**
 *
 * @author kaurvirk
 */
public class Employee {
    
    private String userName, password;
    private int employeeId;
    private byte[] salt;

    public Employee(String userName, String password) throws NoSuchAlgorithmException {
        this.userName = userName;
        salt = PasswordGenerator.getSalt();
        this.password = PasswordGenerator.getSHA512Password(password, salt);
    }

    Supplier<String> s = ()-> userName;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getSalt() {
        return salt;
    }

     public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        if (employeeId >= 0)
            this.employeeId = employeeId;
        else
            throw new IllegalArgumentException("EmployeeId must be >= 0");
    }
    
    /**
     * This method will write the instance of the employee into the database
     */
    public void insertIntoDB() throws SQLException
    {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        
        try
        {
            //1. Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://aws.computerstudi.es:3306/" + "gc200361558", "gc200361558", "IWrvd53ZK3");
            
            //2. Create a String that holds the query with ? as user inputs
            String sql = "INSERT INTO employees (userName, password, salt)"
                    + "VALUES (?,?,?)";
                    
            //3. prepare the query
            preparedStatement = conn.prepareStatement(sql);
            
            //4. Bind the values to the parameters
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            preparedStatement.setBlob(3, new javax.sql.rowset.serial.SerialBlob(salt));
            
            //5.Execute query
            preparedStatement.executeUpdate();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            if (preparedStatement != null)
                preparedStatement.close();
            
            if (conn != null)
                conn.close();
        }
    }
       
}
