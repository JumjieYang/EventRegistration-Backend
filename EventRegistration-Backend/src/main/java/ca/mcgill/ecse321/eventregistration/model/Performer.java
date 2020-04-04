package ca.mcgill.ecse321.eventregistration.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Performer extends Person{
    private List<Event> performsAt = new ArrayList<>();

    @OneToMany(cascade = { CascadeType.ALL })
    public List<Event> getPerformsAt()
    {
        return performsAt;
    }

    public void setPerformsAt(List<Event> pPerformsAt)
    {
        performsAt = pPerformsAt;
    }
    
}