package javaapp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;
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
public class AddEmployeeController implements Initializable {

    @FXML private TextField userNameTextField;
    @FXML private PasswordField pwField;
    @FXML private PasswordField confirmPwField;
    @FXML private Label errMsgLabel;
    @FXML private Label headerLabel;
    private Employee employee;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
                
        errMsgLabel.setText("");    //set the error message to be empty to start

    }  
    
    /**
     * This method will change back to the LogInView of employees without adding
     * a employee.  All data in the form will be lost
     */
    public void cancelButtonPushed(ActionEvent event) throws IOException
    {
        SceneChanger sc = new SceneChanger();
        
        sc.changeScene(event, "LogInView.fxml", "Log In");
        
    }
    /**
     * This method will read from the scene and try to create a new instance of a Employee.
     * If a Employee was successfully created, it is updated in the database.
     */
    public void saveButtonPushed(ActionEvent event)
    {
        if (validPassword())
        {
            if(pwLengthChecker() == true)
        {
            try
            {
                employee = new Employee(userNameTextField.getText(),pwField.getText());
                
                errMsgLabel.setText("");    //do not show errors if creating Volunteer was successful
                employee.insertIntoDB();

                SceneChanger sc = new SceneChanger();
                sc.changeScene(event, "EmployeeId.fxml", "Employee Id");
            }
            catch (Exception e)
            {
                errMsgLabel.setText(e.getMessage());
            }
        }
            else
            {
                errMsgLabel.setText("Password must be greater than or equal to 8 characters in length"); 
            }
        }
        
    }
    
    /**
     * This method will validate that the passwords match
     * @return true or false according to validation of password
     */
    public boolean pwLengthChecker()
    {
    Predicate<String> pwLengthChecker = (password) -> password.length() >= 8;
    return pwLengthChecker.test(pwField.getText());
    }
    
    public boolean validPassword()
    {
        if (pwField.getText().equals(confirmPwField.getText()))
        {
            return true;
        }
        else
        {
            errMsgLabel.setText("Password must be equal to confirm password");
            return false;
        }
    }
}
