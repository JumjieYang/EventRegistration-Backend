package ca.mcgill.ecse321.eventregistration.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.DiscriminatorType;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType=DiscriminatorType.STRING, name="TYPE")
public class Person{
    
    private String name;

    public void setName(String value) {
        this.name = value;
    }
    @Id
    public String getName() {
        return this.name;
}

    private RegistrationManager manager;

    @ManyToOne()
    public RegistrationManager getManager(){
        return this.manager;
    }

    public void setManager(RegistrationManager pManager)
    {
        manager = pManager;
    }

    private List<Registration> registrations;

    @OneToMany(cascade = { CascadeType.ALL })
    public List<Registration> getRegistrations(){
        return registrations;
    }

    public void setRegistrations(List<Registration> pRegistrations){
        this.registrations = pRegistrations;
    }
}
