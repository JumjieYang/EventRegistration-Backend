package ca.mcgill.ecse321.eventregistration.dto;

import java.util.Collections;
import java.util.List;

public class PersonDto {

	private String name;
	private List<EventDto> eventsAttended;
	private List<PaypalDto> paypals;
	public PersonDto() {
	}

	@SuppressWarnings("unchecked")
	public PersonDto(String name) {
		this(name, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
	}
	@SuppressWarnings("unchecked")
	public PersonDto(String name, List<EventDto> events) {
		this(name, events, Collections.EMPTY_LIST);
	}

	public PersonDto(String name, List<EventDto> events, List<PaypalDto> paypals){
		this.name = name;
		this.eventsAttended = events;
		this.paypals = paypals;
	}

	public String getName() {
		return name;
	}

	public List<EventDto> getEventsAttended() {
		return eventsAttended;
	}

	public void setEventsAttended(List<EventDto> events) {
		this.eventsAttended = events;
	}

	public List<PaypalDto> getPaypals(){
		return paypals;
	}

	public void setPaypals(List<PaypalDto> pPaypals){
		this.paypals = pPaypals;
	}
}
