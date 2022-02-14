package carRentalSystem.model;

/**
 * Class is the abstraction of a user of the system
 */
public abstract class User {

    private String name, email, password,omang;
    private int id;


    public User(String name, String email,  String password){

        this.name = name;
        this.email = email;
        this.password = password;

    }

    public User(int id,String name, String email, String omang, String password){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.omang = omang;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setOmang(String omang) {
        this.omang = omang;
    }

    public String getOmang() {
        return omang;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return name;
    }
}
