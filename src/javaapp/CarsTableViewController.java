package javaapp;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.function.BiFunction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author kaurvirk
 */
public class CarsTableViewController implements Initializable {

    @FXML private Slider minYearSlider;
    @FXML private Label minYear;
    @FXML private Slider maxYearSlider;
    @FXML private Label maxYear;
    @FXML private ComboBox<String> makeComboBox;
    @FXML private TableView<Car> carsTable;
    @FXML private TableColumn<Car, String> makeColumn;
    @FXML private TableColumn<Car, String> modelColumn;
    @FXML private TableColumn<Car, Integer> yearColumn;
    @FXML private TableColumn<Car, Integer> mileageColumn;
    @FXML private Label streamResult;
    @FXML private Label loggedInEmployee;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         
        SceneChanger sc = new SceneChanger();
        Employee loggedIn = sc.getLoggedInEmployee();
       
        //Bi function Lambda Expression
        BiFunction<String, String, String> concat = (a, b) -> a+b;
        loggedInEmployee.setText(concat.apply("Hello, ",loggedIn.s.get()));
        
        this.makeColumn.setCellValueFactory(
                new PropertyValueFactory<Car, String>("make"));
        this.modelColumn.setCellValueFactory(
                new PropertyValueFactory<Car, String>("model"));
        this.yearColumn.setCellValueFactory(
                new PropertyValueFactory<Car, Integer>("year"));
        this.mileageColumn.setCellValueFactory(
                new PropertyValueFactory<Car, Integer>("mileage"));
        
        try{
            maxValueForSliders();
            minValueForSliders();
            updateComboBoxFromdb();
            loadCars();
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }
        
        //default value for min year and maximum year
        String minimumYear = String.format("%.0f", minYearSlider.getValue());
        minYear.setText(minimumYear);
        
        //default value for max year and maximum year
        String maximumYear = String.format("%.0f", maxYearSlider.getValue());
        maxYear.setText(maximumYear);

    }    
    
    public void logOutButtonPushed(ActionEvent event) throws IOException
    {
        SceneChanger sc = new SceneChanger();
        sc.changeScene(event, "LogInView.fxml", "Log In");
    }
    
    public void loadCars() throws SQLException {
        
        ObservableList<Car> cars = FXCollections.observableArrayList();
        Double dMinYear = minYearSlider.getValue();
        Integer minimumYear = dMinYear.intValue();
        Double dMaxYear = maxYearSlider.getValue();
        Integer maximumYear = dMaxYear.intValue();
        
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            
            //1. Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://aws.computerstudi.es:3306/" + "gc200361558", "gc200361558", "IWrvd53ZK3");
            
           //2. Create a Statement Object
           statement = conn.createStatement();
           
           //3. Create the SQL Querry
           resultSet = statement.executeQuery("SELECT * FROM Cars WHERE year BETWEEN "+ minimumYear + " AND " + maximumYear);
           
           //4. Create volunteer object from each record
           while(resultSet.next())
           {
                Car newCar = new Car(resultSet.getString("make"),
                                    resultSet.getString("model"),
                                    Integer.parseInt(resultSet.getString("year")),
                                    Integer.parseInt(resultSet.getString("milage")));
               
               cars.add(newCar);
           }
           carsTable.getItems().addAll(cars);
           
           streamResult.setText("Cars of Dodge are: ");
           
           cars.stream().filter(car -> car.getMake().equals("Dodge"))
                        .forEach(car -> streamResult.setText(streamResult.getText()+ car.getModel()+ ", " ));
           
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            if (conn != null)
                conn.close();
            if (statement != null)
                statement.close();
            if (resultSet != null)
                resultSet.close();
        }
        
    }
    
    /**
     * This method set maximum year of car by getting it from database
     */
    public void maxValueForSliders() throws SQLException{
        
        Connection conn = null;
        Statement statement = null;
        ResultSet result = null;
        
        try{
           //1. Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://aws.computerstudi.es:3306/" + "gc200361558", "gc200361558", "IWrvd53ZK3");
        
         //2.  Prepare the query
           statement = (Statement) conn.createStatement();
          
         //3 create and execute sql query
           result = statement.executeQuery("Select MAX(year) from Cars");
           
         //set values for slider
         while(result.next()){
            minYearSlider.setMax(Integer.parseInt(result.getString("MAX(year)")));
            maxYearSlider.setMax(Integer.parseInt(result.getString("MAX(year)")));
            maxYearSlider.setValue(Integer.parseInt(result.getString("MAX(year)"))); 
         }
        }
         catch (SQLException e)
        {
            System.err.println(e);
        }
        finally
        {
            if (conn != null)
                conn.close();
            if (statement != null)
                statement.close();
            if(result  != null)
               result.close();
        } 
    }
    
    /**
     * This method set min year of car by getting it from database
     */
    public void minValueForSliders() throws SQLException{
        
        Connection conn = null;
        Statement statement = null;
        ResultSet result = null;
        
        try{
           //1. Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://aws.computerstudi.es:3306/" + "gc200361558", "gc200361558", "IWrvd53ZK3");
        
         //2.  Prepare the query
           statement = (Statement) conn.createStatement();
          
         //3 create and execute sql query
           result = statement.executeQuery("Select MIN(year) from Cars");
           
           
         //set values for slider
         while(result.next()){
            minYearSlider.setMin(Integer.parseInt(result.getString("MIN(year)")));
            maxYearSlider.setMin(Integer.parseInt(result.getString("MIN(year)")));
            minYearSlider.setValue(Integer.parseInt(result.getString("MIN(year)"))); 
         }
        }
         catch (SQLException e)
        {
            System.err.println(e);
        }
        finally
        {
            if (conn != null)
                conn.close();
            if (statement != null)
                statement.close();
            if(result  != null)
               result.close();
        } 
    }
    
    /**
     * This method will update the min year slider label and should be called
     * when the slider is dragged
     */
    public void minYearSliderMoved() throws SQLException
    {
        String label = String.format("%.0f", minYearSlider.getValue());
        minYear.setText(label);
        carsTable.getItems().clear();
        if(this.makeComboBox.getValue() == null)
        loadCars();
        else
        makeSelectedFromComboBox();    
    }
    
    /**
     * This method will update the max year slider label and should be called
     * when the slider is dragged
     */
    public void maxYearSliderMoved() throws SQLException
    {
        String label = String.format("%.0f", maxYearSlider.getValue());
        maxYear.setText(label);
        carsTable.getItems().clear();
        if(this.makeComboBox.getValue() == null)
        loadCars();
        else
        makeSelectedFromComboBox();
    }
    
    /**
     * This method update combo box from make of cars in database 
     */
    public void updateComboBoxFromdb() throws SQLException{
        
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try{
           //1. Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://aws.computerstudi.es:3306/" + "gc200361558", "gc200361558", "IWrvd53ZK3");
        
         //2.  Prepare the query
           statement = (Statement) conn.createStatement();
          
         //3 create and execute sql query
           resultSet = statement.executeQuery("Select make from Cars");
           
         //populate the combobox
         while(resultSet.next()){
          makeComboBox.getItems().add(resultSet.getString("make"));
         }
        }
         catch (SQLException e)
        {
            System.err.println(e);
        }
        finally
        {
            if (conn != null)
                conn.close();
            if (statement != null)
                statement.close();
            if(resultSet  != null)
               resultSet.close();
        }         
    }
    
    public void makeSelectedFromComboBox() throws SQLException{
        
        String make = makeComboBox.getValue();
        
        this.carsTable.getItems().clear();
        
        ObservableList<Car> cars = FXCollections.observableArrayList();
        Double dMinYear = minYearSlider.getValue();
        Integer minimumYear = dMinYear.intValue();
        Double dMaxYear = maxYearSlider.getValue();
        Integer maximumYear = dMaxYear.intValue();
        
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            
            //1. Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://aws.computerstudi.es:3306/" + "gc200361558", "gc200361558", "IWrvd53ZK3");
            
           //2. Create a Statement Object
           statement = conn.createStatement();
           
           //3. Create the SQL Querry
           resultSet = statement.executeQuery("SELECT * FROM Cars WHERE year BETWEEN "+minimumYear+" AND "+maximumYear+" AND make = '"+make+"'");
           
           //4. Create volunteer object from each record
           while(resultSet.next())
           {
                Car newCar = new Car(resultSet.getString("make"),
                                    resultSet.getString("model"),
                                    Integer.parseInt(resultSet.getString("year")),
                                    Integer.parseInt(resultSet.getString("milage")));
               
               cars.add(newCar);
           }
           carsTable.getItems().addAll(cars);
           
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            if (conn != null)
                conn.close();
            if (statement != null)
                statement.close();
            if (resultSet != null)
                resultSet.close();
        }
    }
    
    
   
}
