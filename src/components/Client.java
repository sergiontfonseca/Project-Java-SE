// 1.1.1 Creation of the client class
package components;

public class Client {

    // variables
    private String name;
    private String firstName;
    private int clientNumber;

    private static int clientCount;

    // constructors
    public Client(String firstName, String name) {
        this.name = name;
        this.firstName = firstName;
        this.clientNumber = ++clientCount;
    }

    // methods
    public String toString() {
        return "Client number: " + getClientNumber() + " Name: " + getFirstName()+ " " + getName();
    }

    // getters
    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getClientNumber() {
        return clientNumber;
    }

    public static int getClientCount() {
        return clientCount;
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setClientNumber(int clientNumber) {
        this.clientNumber = clientNumber;
    }

    public static void setClientCount(int clientCount) {
        Client.clientCount = clientCount;
    }

}
