package ca.mcgill.ecse321.eventregistration.dto;

import java.util.Collections;
import java.util.List;

public class PerformerDto extends PersonDto {

    private List<EventDto> eventsPerformed;

    public PerformerDto(){
        super();
    }

    @SuppressWarnings("unchecked")
    public PerformerDto(String name)
    {
        this(name, Collections.EMPTY_LIST,Collections.EMPTY_LIST);
    }

    @SuppressWarnings("unchecked")
    public PerformerDto(String name, List<EventDto> eventsAttended)
    {
        super(name, eventsAttended);
        this.eventsPerformed = Collections.EMPTY_LIST;
    }
    public PerformerDto(String name, List<EventDto> eventsAttended, List<EventDto>eventsPerformed)
    {
        super(name, eventsAttended);
        this.eventsPerformed = eventsPerformed;
    }

    public List<EventDto> getEventsPerformed(){
        return eventsPerformed;
    }

    public void setEventsPerformed(List<EventDto> events){
        this.eventsPerformed = events;
    }

}