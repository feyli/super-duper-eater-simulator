package fr.univamu.iut.dishes_users.model;
/**
 * Class representing a user
 */
public class Users {
    /**
     * User id
     */
    protected int id;
    /**
     * User lastname
     */
    protected String lastName;
    /**
     * User firstname
     */
    protected String firstName;
    /**
     * User email
     */
    protected String email;
    /**
     * User address
     */
    protected String address;
    /**
     * Default constructor
     */
    public Users() {

    }
    /**
     * User constructor
     * @param id user id
     * @param lastName user lastname
     * @param firstName user firstname
     * @param email user email
     * @param address user address
     */
    public Users(int id, String lastName, String firstName, String email, String address) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.address = address;
    }
    /**
     * Method to get user id
     * @return int id
     */
    public int getId() {
        return id;
    }
    /**
     * Method to get user last name
     * @return string lastName
     */
    public String getLastName() {
        return lastName;
    }
    /**
     * Method to get user first name
     * @return string firstName
     */
    public String getFirstName() {
        return firstName;
    }
    /**
     * Method to get user email
     * @return string email
     */
    public String getEmail() {
        return email;
    }
    /**
     * Method to get user address
     * @return string address
     */
    public String getAddress() {
        return address;
    }
    /**
     * Method to set user id
     * @param id user id
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Method to set user last name
     * @param lastName user lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    /**
     * Method to set user first name
     * @param firstName user firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    /**
     * Method to set user email
     * @param email user email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Method to set user address
     * @param address user address
     */
    public void setAddress(String address) {
        this.address = address;
    }
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", email=" + email + '\'' +
                ", address='" + address +
                '}';
    }
}
