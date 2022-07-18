package components;

public class FlowDTO {
    private String comment;
    private double amount;
    private int targetAccountNumber;
    private int originAccountNumber;

    public String getComment() {
        return comment;
    }
    public double getAmount() {
        return amount;
    }
    public int getTargetAccountNumber() {
        return targetAccountNumber;
    }
    public int getOriginAccountNumber() {
        return originAccountNumber;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setTargetAccountNumber(int targetAccountNumber) {
        this.targetAccountNumber = targetAccountNumber;
    }
    public void setOriginAccountNumber(int originAccountNumber) {
        this.originAccountNumber = originAccountNumber;
    }


}

