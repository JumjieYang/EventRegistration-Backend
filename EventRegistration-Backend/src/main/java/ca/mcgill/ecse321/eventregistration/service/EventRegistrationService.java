package ca.mcgill.ecse321.eventregistration.service;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.eventregistration.dao.*;
import ca.mcgill.ecse321.eventregistration.model.*;

@Service
public class EventRegistrationService {

	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private RegistrationRepository registrationRepository;
	@Autowired
	private TheatreRepository theatreRepository;
	@Autowired
	private PerformerRepository performerRepository;
	@Autowired
	private PaypalRepository paypalRepository;

	@Transactional
	public Person createPerson(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
		} else if (personRepository.existsById(name)) {
			throw new IllegalArgumentException("Person has already been created!");
		}
		Person person = new Person();
		person.setName(name);
		personRepository.save(person);
		return person;
	}


	@Transactional
	public Person getPerson(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
		}
		Person person = personRepository.findByName(name);
		return person;
	}

	@Transactional
	public List<Person> getAllPersons() {
		return toList(personRepository.findAll());
	}

	@Transactional
	public Event buildEvent(Event event, String name, Date date, Time startTime, Time endTime) {
		// Input validation
		String error = "";
		if (name == null || name.trim().length() == 0) {
			error = error + "Event name cannot be empty! ";
		} else if (eventRepository.existsById(name)) {
			throw new IllegalArgumentException("Event has already been created!");
		}
		if (date == null) {
			error = error + "Event date cannot be empty! ";
		}
		if (startTime == null) {
			error = error + "Event start time cannot be empty! ";
		}
		if (endTime == null) {
			error = error + "Event end time cannot be empty! ";
		}
		if (endTime != null && startTime != null && endTime.before(startTime)) {
			error = error + "Event end time cannot be before event start time!";
		}
		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		event.setName(name);
		event.setDate(date);
		event.setStartTime(startTime);
		event.setEndTime(endTime);
		return event;
	}

	@Transactional
	public Event createEvent(String name, Date date, Time startTime, Time endTime) {
		Event event = new Event();
		buildEvent(event, name, date, startTime, endTime);
		eventRepository.save(event);
		return event;
	}

	@Transactional
	public Event getEvent(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Event name cannot be empty!");
		}
		Event event = eventRepository.findByName(name);
		return event;
	}

	@Transactional
	public Theatre getTheatre(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Event name cannot be empty!");
		}
		Theatre theatre = theatreRepository.findByName(name);
		return theatre;
		
	}

	// This returns all objects of instance "Event" (Subclasses are filtered out)
	@Transactional
	public List<Event> getAllEvents() {
		return toList(eventRepository.findAll()).stream().filter(e -> e.getClass().equals(Event.class)).collect(Collectors.toList());
	}

	@Transactional
	public Registration register(Person person, Event event) {
		String error = "";
		if (person == null) {
			error = error + "Person needs to be selected for registration! ";
		} else if (!personRepository.existsById(person.getName())) {
			error = error + "Person does not exist! ";
		}
		if (event == null) {
			error = error + "Event needs to be selected for registration!";
		} else if (!eventRepository.existsById(event.getName())) {
			error = error + "Event does not exist!";
		}
		if (registrationRepository.existsByPersonAndEvent(person, event)) {
			error = error + "Person is already registered to this event!";
		}

		error = error.trim();

		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}

		Registration registration = new Registration();
		registration.setId(person.getName().hashCode() * event.getName().hashCode());
		registration.setPerson(person);
		registration.setEvent(event);
		registrationRepository.save(registration);

		return registration;
	}

	@Transactional
	public List<Registration> getAllRegistrations() {
		return toList(registrationRepository.findAll());
	}

	@Transactional
	public Registration getRegistrationByPersonAndEvent(Person person, Event event) {
		if (person == null || event == null) {
			throw new IllegalArgumentException("Person or Event cannot be null!");
		}

		return registrationRepository.findByPersonAndEvent(person, event);
	}
	@Transactional
	public List<Registration> getRegistrationsForPerson(Person person){
		if(person == null) {
			throw new IllegalArgumentException("Person cannot be null!");
		}
		return registrationRepository.findByPerson(person);
	}

	@Transactional
	public List<Event> getEventsAttendedByPerson(Person person) {
		if (person == null) {
			throw new IllegalArgumentException("Person cannot be null!");
		}
		List<Event> eventsAttendedByPerson = new ArrayList<>();
		for (Registration r : registrationRepository.findByPerson(person)) {
			eventsAttendedByPerson.add(r.getEvent());
		}
		return eventsAttendedByPerson;
	}

	@Transactional
	public List<Event> getEventsPerformedByPerformer(Performer performer) {
		if (performer == null) {
			throw new IllegalArgumentException("Person cannot be null!");
		}
		List<Event> eventsPerformedByPerson = new ArrayList<>();
		for (Event e : eventRepository.findByPerformer(performer)) {
			eventsPerformedByPerson.add(e);
		}
		return eventsPerformedByPerson;
	}

	private <T> List<T> toList(Iterable<T> iterable) {
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}

	@Transactional
	public List<Theatre> getAllTheatres() {
		return toList(theatreRepository.findAll());
	}

	@Transactional
	public Theatre createTheatre(String name, Date date, Time startTime, Time endTime, String title) {
		Theatre theatre = new Theatre();
		buildTheatre(theatre, name, date, startTime, endTime,title);
		theatreRepository.save(theatre);
		return theatre;
	}

	@Transactional
	public Theatre buildTheatre(Theatre theatre, String name, Date date, Time startTime, Time endTime, String title) {
		String error = "";
		if (name == null || name.trim().length() == 0) {
			error = error + "Event name cannot be empty! ";
		} else if (theatreRepository.existsById(name)) {
			throw new IllegalArgumentException("Event has already been created!");
		}
		if (date == null) {
			error = error + "Event date cannot be empty! ";
		}
		if (startTime == null) {
			error = error + "Event start time cannot be empty! ";
		}
		if (endTime == null) {
			error = error + "Event end time cannot be empty! ";
		}
		if (endTime != null && startTime != null && endTime.before(startTime)) {
			error = error + "Event end time cannot be before event start time!";
		}
		if (title == null || title.trim().length() == 0) {
			error = error + "Theatre title cannot be empty! ";
		} 
		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		theatre.setName(name);
		theatre.setDate(date);
		theatre.setStartTime(startTime);
		theatre.setEndTime(endTime);
		theatre.setTitle(title);
		return theatre;
	}

	@Transactional
	public Performer createPerformer(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Performer name cannot be empty!");
		} else if (performerRepository.existsById(name)) {
			throw new IllegalArgumentException("Performer has already been created!");
		}
		Performer performer = new Performer();
		performer.setName(name);
		performerRepository.save(performer);
		return performer;
	}

	@Transactional
	public List<Performer> getAllPerformers() {
		return toList(performerRepository.findAll());
	}

	@Transactional
	public Performer performsEvent(Performer performer, Event event) {
		if(performer == null || performerRepository.findByName(performer.getName()) == null)
			throw new IllegalArgumentException("Performer needs to be selected for performs!");
		if(event == null || eventRepository.findByName(event.getName()) == null)
			throw new IllegalArgumentException("Event does not exist!");
		List<Event> eventList = new ArrayList<>();
		for (Event e : performer.getPerformsAt())
			eventList.add(e);
		eventList.add(event);
		performer.setPerformsAt(eventList);
		performerRepository.save(performer);
		return performer;
	}

	@Transactional
	public Performer getPerformer(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
		}
		Performer performer = performerRepository.findByName(name);
		return performer;
	}

	@Transactional
	public Paypal createPaypalPay(String email, int amount) {
		
		if(email == null || email.trim().length() == 0)
			throw new IllegalArgumentException("Email is null or has wrong format!");
		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";  
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(email);
		if(!matcher.matches())
			throw new IllegalArgumentException("Email is null or has wrong format!");
		if(amount < 0)
			throw new IllegalArgumentException("Payment amount cannot be negative!");
		Paypal paypal = new Paypal();
		paypal.setEmail(email);
		paypal.setAmount(amount);
		paypalRepository.save(paypal);
		return paypal;
	}

	@Transactional
	public Registration pay(Registration r, Paypal paypal) {
		if (r == null || !registrationRepository.existsById(r.getId()) || paypal == null || paypalRepository.findByEmail(paypal.getEmail()) == null)
			throw new IllegalArgumentException("Registration and payment cannot be null!");
		r.setPaypal(paypal);
		registrationRepository.save(r);
		return r;
			
	}


	@Transactional
	public Paypal getPaypal(String accountId) {
		if (accountId == null || accountId.trim().length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
		}
		Paypal paypal = paypalRepository.findByEmail(accountId);
		return paypal;
	}

	@Transactional
	public Paypal getPaypalFromPersonAndEvent(Person p, Event e) {
		Registration r = getRegistrationByPersonAndEvent(p,e);
		Paypal pp = r.getPaypal();
		return pp;
	}


	@Transactional
	public Event setPerformerForEvent(Event e, Performer  p)
	{
		if (e == null)
			throw new  IllegalArgumentException("Event not exist");
		if (p == null)
			throw new IllegalArgumentException("Performer not exist");
		e = eventRepository.findByName(e.getName());
		e.setPerformer(p);
		eventRepository.save(e);
		return e;
	}
}
