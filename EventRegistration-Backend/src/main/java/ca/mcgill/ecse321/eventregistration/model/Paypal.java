package ca.mcgill.ecse321.eventregistration.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Paypal{

    private int paypalId;

    @Id
    @GeneratedValue
    public int getPaypalId()
    {
        return paypalId;
    }

    public void setPaypalId(int value) {
		this.paypalId = value;
	}

    private String email;
    
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String pEmail)
    {
        email =pEmail;
    }

    private int amount;
    
    public int getAmount()
    {
        return amount;
    }

    public void setAmount(int pAmount)
    {
        amount = pAmount;
    }

    List<Event> events;
    
    @OneToMany(cascade = { CascadeType.ALL })
    public List<Event> getEvents(){
        return events;
    }

    public void setEvents(List<Event> pEvents)
    {
        this.events = pEvents;
    }
}