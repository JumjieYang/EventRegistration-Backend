package ca.mcgill.ecse321.eventregistration.model;


import java.util.Set;

import javax.persistence.*;


@Entity
public class RegistrationManager {
	private Set<Person> person;

	@OneToMany(cascade = { CascadeType.ALL })
	public Set<Person> getPerson() {
		return this.person;
	}

	public void setPerson(Set<Person> persons) {
		this.person = persons;
	}

	private Set<Registration> registration;

	@OneToMany(cascade = { CascadeType.ALL })
	public Set<Registration> getRegistration() {
		return this.registration;
	}

	public void setRegistration(Set<Registration> registrations) {
		this.registration = registrations;
	}

	private Set<Event> event;

	@OneToMany(cascade = { CascadeType.ALL })
	public Set<Event> getEvent() {
		return this.event;
	}

	public void setEvent(Set<Event> events) {
		this.event = events;
	}

	private Integer id;

	public void setId(Integer value) {
		this.id = value;
	}

	@Id
	public Integer getId() {
		return this.id;
	}
}
