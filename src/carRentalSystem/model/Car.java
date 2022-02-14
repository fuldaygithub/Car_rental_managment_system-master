package carRentalSystem.model;

public class Car {
    private double price;
    private String model;
    private String name;
    private String status;
    private int car_id;
    private String image;
    private String num_seats;
    private boolean isAvailable;

    public Car(String name,double price,String status,int car_id,String image){
        this.name = name;
        this.price = price;
        this.status = status;
        this.car_id = car_id;
        this.image = image;
    }

//    set Methods
    public void setPrice(double price){
        this.price = price;
    }

    public void setModel(String model){
        this.model = model;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public void setImage(String image) {
        this.image = image;
    }

    public void setNum_seats(String num_seats) {
        this.num_seats = num_seats;
    }

    public void setisAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
//    end set methods
//    ---
//    get methods
    public double getPrice() {
        return price;
    }

    public String getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public String getImage() {
        return image;
    }

    public int getCar_id() {
        return car_id;
    }

    @Override
    public String toString() {
        return name;
    }
}
