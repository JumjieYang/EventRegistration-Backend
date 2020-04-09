package ca.mcgill.ecse321.eventregistration.dto;

import java.sql.Date;
import java.sql.Time;

public class TheatreDto extends EventDto {
    
    private String title;

    public TheatreDto(){
        super();
    }

    public TheatreDto(String name){
        super(name);
        this.title = null;
    }

    public TheatreDto(String name, Date date, Time startTime, Time endTime, String title)
    {
        super(name, date, startTime, endTime);
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String pTitle){
        title = pTitle;
    }
}