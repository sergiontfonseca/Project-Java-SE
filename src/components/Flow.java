//1.3.2 Creation of the Flow class
package components;

import java.time.LocalDate;

public abstract class Flow {

    // variables
    private String comment;
    private int identifier;
    private double amount;
    private int targetAccountNumber;
    private boolean effect;
    private LocalDate dateOfFlow;

    // constructors
    public Flow(String comment, double amount, int targetAccountNumber) {
        this.amount = amount;
        this.targetAccountNumber = targetAccountNumber;
        this.dateOfFlow = LocalDate.now().plusDays(2);
    }

    // getters
    public String getComment() {
        return comment;
    }

    public int getIdentifier() {
        return identifier;
    }

    public double getAmount() {
        return amount;
    }

    public int getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public boolean isEffect() {
        return effect;
    }

    public LocalDate getDateOfFlow() {
        return dateOfFlow;
    }

    // setters
    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTargetAccountNumber(int targetAccountNumber) {
        this.targetAccountNumber = targetAccountNumber;
    }

    public void setEffect(boolean effect) {
        this.effect = effect;
    }

    public void setDateOfFlow(LocalDate dateOfFlow) {
        this.dateOfFlow = dateOfFlow;
    }

}
