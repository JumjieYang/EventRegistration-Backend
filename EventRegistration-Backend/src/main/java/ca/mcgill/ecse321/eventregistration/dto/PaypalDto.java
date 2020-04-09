package ca.mcgill.ecse321.eventregistration.dto;

public class PaypalDto {

    private String accountId;
    private int amount;

    public PaypalDto(){
    }

    public PaypalDto(String pId, int pAmount)
    {
        accountId = pId;
        amount = pAmount;
    }

    public String getAccountId(){
        return accountId;
    }

    public void setAccountId(String pId){
        accountId = pId;
    }

    public int getAmount(){
        return amount;
    }

    public void setAmount(int pAmount){
        amount = pAmount;
    }


}

