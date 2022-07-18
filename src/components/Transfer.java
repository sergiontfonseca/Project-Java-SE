//1.3.3 Creation of the Transfer, Credit, Debit classes
package components;

public class Transfer extends Flow {

    // variables
    private int originAccountNumber;

    // constructors
    public Transfer(String comment, double amount, int targetAccountNumber, int originAccountNumber) {
        super(comment, amount, targetAccountNumber);
        this.originAccountNumber = originAccountNumber;
    }

    // getters
    public int getOriginAccountNumber() {
        return originAccountNumber;
    }

    // setters
    public void setOriginAccountNumber(int originAccountNumber) {
        this.originAccountNumber = originAccountNumber;
    }

}