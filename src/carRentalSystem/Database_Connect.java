package carRentalSystem;

import carRentalSystem.model.Car;
import carRentalSystem.model.RegisteredUser;
import carRentalSystem.model.AdminUser;
import carRentalSystem.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import carRentalSystem.model.Car;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javafx.scene.control.Alert;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.File;
import javax.imageio.ImageIO;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Logger;



public class Database_Connect {
    private Connection conn;
    private Statement stmt;
    private MessageDigest md;


// database connction class

    public Database_Connect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            conn = DriverManager.getConnection("jdbc:mysql://localhost/car_rental_db?autoReconnect=true","root", "");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

// end database connction class


    // method used to  insert and update

    private void insertStmt(String sql){
        stmt = null;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//end
    //method used to  create setetemnet
    private ResultSet executeStmt(String sql){
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            boolean test = rs.first();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

//end

// registr customer

    public RegisteredUser loginCustomer(String email, String password) throws SQLException, NoSuchAlgorithmException {
        String password_hashed = cryptWithMD5(password);

        String sql = "SELECT * FROM customer_table WHERE email=\""+email+"\" AND password=\""+password_hashed+"\"";

        RegisteredUser user= null;
        ResultSet rs = executeStmt(sql);
        rs.beforeFirst();//start from row 0;
        if(rs.next()){
            if (password_hashed.equals(rs.getString("password")) ) {
                user = new RegisteredUser(rs.getInt("customer_id"), rs.getString("name"), rs.getString("email"), rs.getString("omang"), password_hashed);
                Main.currentUser = user;
                return user;
            }
            else
                return null;

        }
        return null;

    }


    public AdminUser loginAdmin(String email, String password) throws SQLException, NoSuchAlgorithmException {

        String password_hashed = cryptWithMD5(password);
        String sql = "SELECT * FROM admin_table WHERE email=\""+email+"\" AND password=\""+password_hashed+"\"";

        AdminUser user= null;
        ResultSet rs =  executeStmt(sql);
        rs.beforeFirst();
        if(rs.next()){
            if (password_hashed.equals(rs.getString("password")) ) {
                user = new AdminUser("admin", rs.getString("email"), password_hashed);
                Main.currentUser = user;
                return user;
            }
            else
                return null;

        }
        return null;

    }



    public void register(String name, String email, String surname, String omang, String password) throws NoSuchAlgorithmException {
        String password_hash = cryptWithMD5(password);


        String sql = "INSERT INTO customer_table ( name, surname, omang , email, password) " +
                "VALUES (\""+name+"\", \""+surname+"\", \""+omang+"\", \""+email+"\",\""+password_hash+"\")";

        insertStmt(sql);
    }
 // car view bignen
    public ObservableList<Car> getCarsFromDB(){
        String sql = "SELECT * FROM car_table";
        ObservableList<Car> cars = FXCollections.observableArrayList();
        ResultSet rs = executeStmt(sql);
        try{
            rs.beforeFirst();
            while(rs.next()){
                cars.add(new Car(rs.getString("car_name"),rs.getDouble("price"),rs.getString("status"),rs.getInt("car_id"),rs.getString("image")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }
//
    public ObservableList<RegisteredUser> getClientsFromDB(){
        String sql = "SELECT * FROM customer_table";
        ObservableList<RegisteredUser> clients = FXCollections.observableArrayList();
        ResultSet rs = executeStmt(sql);
        try{
            rs.beforeFirst();
            while(rs.next()){
                clients.add(new RegisteredUser(rs.getInt("customer_id"),rs.getString("name"), rs.getString("email"), rs.getString("omang"), rs.getString("password")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }


    public ResultSet getBookedCarsFromDB(){
        String sql = "SELECT * FROM booked WHERE customer = \""+Main.currentUser.getId()+"\"";
        ResultSet rs = executeStmt(sql);
        return rs;
    }


    public void bookCar(int car_id ,String car_name,double price, LocalDate start_date, LocalDate return_date,String image){
        String sql = "INSERT INTO booked ( car_id , car_name, price, customer,book_from, book_until, image) " +
                "VALUES (\""+car_id+"\", \""+car_name+"\", \""+price+"\", \""+Main.currentUser.getId()+"\" , \""+start_date+"\" , \""+return_date+"\", \""+image+"\")";

        insertStmt(sql);
//        update car status in database to booked
         sql = "UPDATE car_table SET status = 'Booked' WHERE car_id = \""+car_id+"\"";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            conn = DriverManager.getConnection("jdbc:mysql://localhost/car_rental_db","root", "");


        } catch (Exception e) {
            e.printStackTrace();
        }


        insertStmt(sql);

    }

    public void returnCar(String car_id){
        String sql = "UPDATE car_table SET status = 'available' WHERE car_id = \""+car_id+"\"";
        insertStmt(sql);

        sql = "DELETE FROM booked WHERE car_id = \""+car_id+"\"";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            conn = DriverManager.getConnection("jdbc:mysql://localhost/car_rental_db","root", "");


        } catch (Exception e) {
            e.printStackTrace();
        }

        insertStmt(sql);
    }



    public void addCartoDB(String car_name ,String car_model,double price, String image){
        String sql = "INSERT INTO car_table ( car_name, car_model,status, price, image) " +
                "VALUES (\""+car_name+"\", \""+car_model+"\",'available', \""+price+"\", \""+image+"\")";

        insertStmt(sql);
    }

    public boolean alreadyBooked()throws SQLException{
        String sql = "SELECT * FROM booked WHERE customer = \""+Main.currentUser.getId()+"\"";
        ResultSet rs = executeStmt(sql);
        rs.beforeFirst();
        if(!rs.isBeforeFirst()){
            return false;
        }else{
            return true;
        }
    }

    public void changePrice(double nPrice,int carId){

        String sql = "UPDATE car_table SET price = \""+nPrice+"\" WHERE car_id = \""+carId+"\"";
        insertStmt(sql);

    }

    public void deleteCarFromDB(int id){
        String sql = "DELETE FROM car_table WHERE car_id = \""+id+"\"";
        stmt = null;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getBorrowedCarForAdmin(int user_id){
        Statement stmt = null;
        String sql = "SELECT * FROM booked WHERE customer = \""+user_id+"\"";
        ResultSet rs = executeStmt(sql);

        return rs;
    }

    public void showAlert(String head,String body){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(head);
        alert.setHeaderText(null);
        alert.setContentText(body);
        alert.showAndWait();
    }

    public  String cryptWithMD5(String pass) throws NoSuchAlgorithmException {

            md = MessageDigest.getInstance("MD5");
            byte[] passBytes = pass.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<digested.length;i++){
                sb.append(Integer.toHexString(0xff & digested[i]));
            }

            return sb.toString();




    }

    public void createTables(){


        String sql = "CREATE TABLE IF NOT EXISTS `admin_table` " +
                "(  `admin_id` int(11) NOT NULL AUTO_INCREMENT," +
                "`email` varchar(60) NOT NULL," +
                "  `username` varchar(25) NOT NULL," +
                "  `password` varchar(60) NOT NULL," +
                "        PRIMARY KEY (`admin_id`)" +
                ")";
        insertStmt(sql);


         sql = "INSERT INTO `admin_table` (`admin_id`, `email`, `username`, `password`) VALUES" +
                " (1, 'admin', 'admin', '5f4dcc3b5aa765d61d8327deb882cf99')";
        insertStmt(sql);



         sql = "CREATE TABLE IF NOT EXISTS `car_table` (" +
                "  `car_id` int(11) NOT NULL AUTO_INCREMENT," +
                "  `car_name` varchar(30) NOT NULL," +
                "  `car_model` varchar(30) NOT NULL," +
                "  `status` varchar(25) NOT NULL," +
                "  `price` double NOT NULL," +
                "  `image` blob NOT NULL," +
                "                PRIMARY KEY (`car_id`)" +
                ")";
        insertStmt(sql);


         sql = "CREATE TABLE IF NOT EXISTS `customer_table` (" +
                "  `customer_id` int(11) NOT NULL AUTO_INCREMENT," +
                "  `name` varchar(25) NOT NULL," +
                "  `surname` varchar(25) NOT NULL," +
                "  `omang` varchar(25) NOT NULL," +
                "  `email` varchar(60) NOT NULL," +
                "  `password` varchar(60) NOT NULL," +
                "        PRIMARY KEY (`customer_id`)" +
                ")";
        insertStmt(sql);


         sql = "CREATE TABLE IF NOT EXISTS `booked` " +
                "(  `id` int(11) NOT NULL AUTO_INCREMENT," +
                "  `car_id` int(11) NOT NULL," +
                "  `car_name` varchar(30) NOT NULL," +
                "  `price` double NOT NULL," +
                "  `customer` int(11) NOT NULL," +
                "  `book_from` date DEFAULT NULL," +
                "  `book_until` date DEFAULT NULL," +
                "  `image` blob NOT NULL," +
                "                PRIMARY KEY (`id`)," +
                "FOREIGN KEY (`car_id`) REFERENCES car_table(`car_id`)," +
                "FOREIGN KEY (`customer`) REFERENCES customer_table(`customer_id`)" +
                ")";

        insertStmt(sql);

    }






}
