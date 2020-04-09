package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.Entity;

@Entity
public class Theatre extends Event{
    private String title;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String pTitle)
    {
        title = pTitle;
    }
}