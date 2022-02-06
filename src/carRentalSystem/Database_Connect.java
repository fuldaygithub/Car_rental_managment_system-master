package carRentalSystem;

import carRentalSystem.model.AdminUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.Alert;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;

//import org.mindrot.jbcrypt.BCrypt;

/**
 * Class that connects to the database.
 */
public class Database_Connect {
    private Connection conn;
    private Statement stmt;
    private MessageDigest md;



    /**
     * Class constructer specifying which database service to connect to.
     */
    public Database_Connect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            conn = DriverManager.getConnection("jdbc:mysql://localhost/car_rental_db?autoReconnect=true","root", "");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    /**
     * Method executes an sql statment to insert something into the dtabase
     * @param sql the insert sql statement to be executed
     */
    private void insertStmt(String sql){
        stmt = null;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
//            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Method exectutes sql statements to return some result.
     * Takes a statement and queries the database.
     * @param sql   the sql statement to be exececuted
     * @return  Returns a resultset containing the rows and columns specified in the sql query
     */
    private ResultSet executeStmt(String sql){
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            boolean test = rs.first();
            return rs;
            //return rs.getString("username");
        } catch (SQLException e) {
            e.printStackTrace();
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
