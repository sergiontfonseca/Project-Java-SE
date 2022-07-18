//1.2.1 Creation of the account class
package components;

public abstract class Account {

    // variables
    protected String label;
    protected double balance;
    protected int accountNumber;
    protected Client client;

    protected static int accountNumberCount;

    // constructors
    public Account(String label, Client client) {
        this.label = label;
        this.balance = 0;
        this.client = client;
        this.accountNumber = ++accountNumberCount;
    }

    // methods
    public String toString() {
        return client + "\n Account Number: " + accountNumber + " Label: " + label + " Balance: " + balance;
    }

    // getters
    public String getLabel() {
        return label;
    }

    public double getBalance() {
        return balance;
    }

    public Client getClient() {
        return client;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    // setters
    public void setLabel(String label) {
        this.label = label;
    }

    public static int getAccountNumberCount() {
        return accountNumberCount;
    }

    // 1.3.5 Updating accounts
    public void setBalance(Flow flow) {

        if (flow instanceof Credit) {
            this.balance += flow.getAmount();
        }

        else if (flow instanceof Debit) {
            this.balance -= flow.getAmount();
        }

        else if (flow instanceof Transfer) {
            if (accountNumber == flow.getTargetAccountNumber()) {
                this.balance += flow.getAmount();
            } else if (accountNumber == ((Transfer) flow).getOriginAccountNumber()) {
                this.balance -= flow.getAmount();
            }
        }
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public static void setAccountNumberCount(int accountNumberCount) {
        Account.accountNumberCount = accountNumberCount;
    }
}
