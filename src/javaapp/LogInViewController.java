package javaapp;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Function;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author kaurvirk
 */
public class LogInViewController implements Initializable {
    
    @FXML private TextField employeeIdTextField;
    @FXML private PasswordField pwField;
    @FXML private Label errMsgLabel;
    
    public void addNewButtonPushed(ActionEvent event) throws IOException
    {
        SceneChanger sc = new SceneChanger();
        sc.changeScene(event, "AddEmployee.fxml", "Add new employee");
    }

    public void loginButtonPushed(ActionEvent event) throws IOException, NoSuchAlgorithmException, SQLException
    {
                
        //query the database with the volunteerID provided, get the salt
        //and encrypted password stored in the database
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        
        //Lambda expression to convert string to integer
        Function<String, Integer> converter = (stringtoConvert) -> Integer.parseInt(stringtoConvert);
        int employeeNum = converter.apply(employeeIdTextField.getText());
        
        try{
            //1.  connect to the DB
            conn = DriverManager.getConnection("jdbc:mysql://aws.computerstudi.es:3306/" + "gc200361558", "gc200361558", "IWrvd53ZK3");
            
            //2.  create a query string with ? used instead of the values given by the user
            String sql = "SELECT * FROM employees WHERE employeeId = ?";
            
            //3.  prepare the statement
            ps = conn.prepareStatement(sql);
            
            //4.  bind the volunteerID to the ?
            ps.setInt(1, employeeNum);
            
            //5. execute the query
            resultSet = ps.executeQuery();
            
            //6.  extract the password and salt from the resultSet
            String dbPassword=null;
            byte[] salt = null;
            Employee employee = null;
            
            while (resultSet.next())
            {
                dbPassword = resultSet.getString("password");
                
                Blob blob = resultSet.getBlob("salt");
                
                //convert into a byte array
                int blobLength = (int)blob.length();
                salt = blob.getBytes(1, blobLength);
                
                employee = new Employee(resultSet.getString("userName"),
                                                       resultSet.getString("password"));
                employee.setEmployeeId(resultSet.getInt("employeeId"));
                
            }
            
            //convert the password given by the user into an encryted password
            //using the salt from the database
            String userPW = PasswordGenerator.getSHA512Password(pwField.getText(), salt);
            
            SceneChanger sc = new SceneChanger();
            
            if (userPW.equals(dbPassword))
            {
                sc.setLoggedInEmployee(employee);
                sc.changeScene(event, "CarsTableView.fxml", "Cars Inventory");
            }
            else
            {
                //if the do not match, update the error message
                errMsgLabel.setText("The employeeId and password do not match");
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
        
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errMsgLabel.setText("");
    }       
    
}
