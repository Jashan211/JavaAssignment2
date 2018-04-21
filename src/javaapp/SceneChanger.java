package javaapp;

import java.io.IOException;
import java.util.function.Supplier;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author JWright
 */
public class SceneChanger
{
        private static Employee loggedInEmployee;

    /**
     * This method will allow the application to change scenes without
     * passing any data to the new scene
     */
    public static void changeScene(ActionEvent event, String fxmlFileName, String title) throws IOException
    {
        //get the Stage from the ActionEvent
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(new Object(){}.getClass().getResource(fxmlFileName));
        
        Parent root = loader.load();
        Scene scene = new Scene(root);
       
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
    

    public static Employee getLoggedInEmployee() {
        return loggedInEmployee;
    }

    public static void setLoggedInEmployee(Employee loggedInEmployee) {
        SceneChanger.loggedInEmployee = loggedInEmployee;
    }
}
