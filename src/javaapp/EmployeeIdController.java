/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapp;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author kaurvirk
 */
public class EmployeeIdController implements Initializable {

    @FXML private Label employeeID;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            loadEmployeeId();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeIdController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    public void loadEmployeeId() throws SQLException{
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        
        try
        {
            //1.  connect to the DB
            conn = DriverManager.getConnection("jdbc:mysql://aws.computerstudi.es:3306/" + "gc200361558", "gc200361558", "IWrvd53ZK3");
            
            //2.  create a query string with ? used instead of the values given by the user
            String sql = "SELECT employeeId FROM employees";
            
            //3.  prepare the statement
            ps = conn.prepareStatement(sql);
            
            //4. execute the query
            resultSet = ps.executeQuery();
            
            while(resultSet.next()){
                employeeID.setText(Integer.toString(resultSet.getInt("employeeId")));
            }
                        
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());            
        }
        finally
        {
            if(conn != null)
                conn.close();
            if(ps != null)
                ps.close();
            if(resultSet != null)
                resultSet.close();
        }
        
    }
    
    public void logInPushed(ActionEvent event) throws IOException
    {
        SceneChanger sc = new SceneChanger();
        sc.changeScene(event, "LogInView.fxml", "Log In");
    }
    
}
