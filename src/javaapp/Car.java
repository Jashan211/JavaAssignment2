package javaapp;

/**
 *
 * @author kaurvirk
 */
public class Car {
    
    private String make, model;
    private int year, mileage;

    /**
     * This method construct an object car with following parameters
     * @param make
     * @param model
     * @param year
     * @param mileage 
     */
    public Car(String make, String model, int year, int mileage) {
        setMake(make);
        setModel(model);
        setYear(year);
        setMileage(mileage);
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }
    
    public Car() {
        
    }
    
    
}
