package ca.mcgill.ecse321.eventregistration.controller;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ca.mcgill.ecse321.eventregistration.model.*;
import ca.mcgill.ecse321.eventregistration.dto.*;
import ca.mcgill.ecse321.eventregistration.service.EventRegistrationService;

@CrossOrigin(origins = "*")
@RestController
public class EventRegistrationRestController {

	@Autowired
	private EventRegistrationService service;

	// POST Mappings

	// @formatter:off
	// Turning off formatter here to ease comprehension of the sample code by
	// keeping the linebreaks
	// Example REST call:
	// http://localhost:8088/persons/John
	@PostMapping(value = { "/persons/{name}", "/persons/{name}/" })
	public PersonDto createPerson(@PathVariable("name") String name) throws IllegalArgumentException {
		// @formatter:on
		Person person = service.createPerson(name);
		return convertToDto(person);
	}
	@PostMapping(value = { "/performers/{name}", "/performers/{name}/" })
	public PerformerDto createPerformer(@PathVariable("name") String name) throws IllegalArgumentException {
		// @formatter:on
		Performer performer = service.createPerformer(name);
		return convertToDto(performer);
	}

	// @formatter:off
	// Example REST call:
	// http://localhost:8080/events/testevent?date=2013-10-23&startTime=00:00&endTime=23:59
	@PostMapping(value = { "/events/{name}", "/events/{name}/" })
	public EventDto createEvent(@PathVariable("name") String name, @RequestParam Date date,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime)
			throws IllegalArgumentException {
		// @formatter:on
		Event event = service.createEvent(name, date, Time.valueOf(startTime), Time.valueOf(endTime));
		return convertToDto(event);
	}

	@PostMapping(value = { "/theatres/{name}", "/theatres/{name}/" })
	public TheatreDto createTheatre(@PathVariable("name") String name, @RequestParam Date date,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime,
			@RequestParam String title)
			throws IllegalArgumentException {
		// @formatter:on
		Theatre event = service.createTheatre(name, date, Time.valueOf(startTime), Time.valueOf(endTime),title);
		return convertToDto(event);
	}

	// @formatter:off
	@PostMapping(value = { "/register", "/register/" })
	public RegistrationDto registerPersonForEvent(@RequestParam(name = "person") PersonDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// @formatter:on

		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());
		Registration r = service.register(p, e);
		return convertToDto(r, p, e);
	}

	@PostMapping(value = { "/pay", "/pay/"})
	public PaypalDto payForRegistration(@RequestParam(name = "person") PersonDto pDto,
			@RequestParam(name = "event") EventDto eDto,
			@RequestParam(name = "paypalId") String Id,
			@RequestParam(name = "amount") String amount) throws IllegalArgumentException {
				Person p = service.getPerson(pDto.getName());
				Event e = service.getEvent(eDto.getName());
				Paypal pp = service.createPaypalPay(Id, Integer.parseInt(amount));
				Registration r = service.getRegistrationByPersonAndEvent(p, e);
				r = service.pay(r, pp);
				return convertToDto(pp);
			}

	@PostMapping(value = { "/assign", "/assign/"})
	public EventDto assignPerformer(@RequestParam(name ="performer") PerformerDto pDto,
	@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		Performer p =service.getPerformer(pDto.getName());
		Event e  = service.getEvent(eDto.getName());
		e = service.setPerformerForEvent(e, p);
		return convertToDto(e);

	}

	// GET Mappings
	@GetMapping(value = { "/events", "/events/" })
	public List<EventDto> getAllEvents() {
		List<EventDto> eventDtos = new ArrayList<>();
		for (Event event : service.getAllEvents()) {
			eventDtos.add(convertToDto(event));
		}
		return eventDtos;
	}

	@GetMapping(value = {"/theatres", "/theatres/"})
	public List<TheatreDto> getAllTheatres(){
		List<TheatreDto> theatreDtos = new ArrayList<>();
		for (Theatre t : service.getAllTheatres()) {
			theatreDtos.add(convertToDto(t));
		}
		return theatreDtos;
	}

	@GetMapping(value ={"/paypals/{name}", "/paypals/{name}/"})
	public List<PaypalDto> getPaypalsOfPerson(@PathVariable("name") PersonDto pDto){
		Person p = convertToDomainObject(pDto);
		List<Registration> rs = p.getRegistrations();
		List<PaypalDto> ppDto = new ArrayList<>();
		for (Registration r : rs)
			ppDto.add(convertToDto(r.getPaypal()));
		return ppDto;
	}


	// Example REST call:
	// http://localhost:8088/events/person/JohnDoe
	@GetMapping(value = { "/registrations/person/{name}", "/events/person/{name}/" })
	public List<RegistrationDto> getRegistrationsOfPerson(@PathVariable("name") PersonDto pDto) {
		Person p = convertToDomainObject(pDto);
		return createRegistrationDtosForPerson(p);
	}

	@GetMapping(value = { "/persons/{name}", "/persons/{name}/" })
	public PersonDto getPersonByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getPerson(name));
	}

	@GetMapping(value = { "/performers/{name}", "/performers/{name}/" })
	public PerformerDto getPerformerByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getPerformer(name));
	}

	@GetMapping(value = { "/registrations", "/registrations/" })
	public RegistrationDto getRegistrations(@RequestParam(name = "person") PersonDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.getRegistrationByPersonAndEvent(p, e);
		return convertToDto(r);
	}


	@GetMapping(value = { "/persons", "/persons/" })
	public List<PersonDto> getAllPersons() {
		List<PersonDto> persons = new ArrayList<>();
		for (Person person : service.getAllPersons()) {
			persons.add(convertToDto(person));
		}
		return persons;
	}

	@GetMapping(value = { "/performers", "/performers/" })
	public List<PerformerDto> getAllPerformers() {
		List<PerformerDto> persons = new ArrayList<>();
		for (Performer person : service.getAllPerformers()) {
			persons.add(convertToDto(person));
		}
		return persons;
	}



	@GetMapping(value = { "/events/{name}", "/events/{name}/" })
	public EventDto getEventByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getEvent(name));
	}

	@GetMapping(value = { "/theatres/{name}", "/theatres/{name}/" })
	public TheatreDto getTheatreByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getTheatre(name));
	}

	// Model - DTO conversion methods (not part of the API)

	private EventDto convertToDto(Event e) {
		if (e == null) {
			throw new IllegalArgumentException("There is no such Event!");
		}
		EventDto eventDto = new EventDto(e.getName(), e.getDate(), e.getStartTime(), e.getEndTime());
		return eventDto;
	}

	private PersonDto convertToDto(Person p) {
		if (p == null) {
			throw new IllegalArgumentException("There is no such Person!");
		}
		List<Registration> registrations = service.getRegistrationsForPerson(p);
		List<EventDto> eventsAttended = new ArrayList<>();
		List<PaypalDto> paypals = new ArrayList<>();
		for (Registration r : registrations)
		{
			eventsAttended.add(convertToDto(r.getEvent()));
			paypals.add(convertToDto(r.getPaypal()));
		}
		PersonDto pDto = new  PersonDto(p.getName());
		pDto.setEventsAttended(eventsAttended);
		pDto.setPaypals(paypals);
		return pDto;
	}


	private PerformerDto convertToDto(Performer p) {
		if (p == null) {
			throw new IllegalArgumentException("There is no such Person!");
		}
		List<Event> eventsPorformed = service.getEventsPerformedByPerformer(p);
		List<EventDto> performed = new ArrayList<>();
		for(Event e: eventsPorformed) {
			performed.add(convertToDto(e));
		}
		List<Registration> registrations = service.getRegistrationsForPerson(p);
		List<EventDto> eventsAttended = new ArrayList<>();
		List<PaypalDto> paypals = new ArrayList<>();
		for (Registration r : registrations)
		{
			eventsAttended.add(convertToDto(r.getEvent()));
			paypals.add(convertToDto(r.getPaypal()));
		}
		PerformerDto pDto = new PerformerDto(p.getName());
		pDto.setEventsAttended(eventsAttended);
		pDto.setPaypals(paypals);
		pDto.setEventsPerformed(performed);
		return pDto;
	}
	private PaypalDto convertToDto(Paypal p){
		PaypalDto paypalDto = null;
		if (p == null) {
			paypalDto = new  PaypalDto();
		}
		else
			paypalDto = new PaypalDto(p.getEmail(),p.getAmount());
		return paypalDto;
	}

	private TheatreDto convertToDto(Theatre t){
		if (t == null) {
			throw new IllegalArgumentException("There is no such Event!");
		}
		TheatreDto theatreDto = new TheatreDto(t.getName(), t.getDate(),t.getStartTime(), t.getEndTime(), t.getTitle());
		return theatreDto;
	}

	// DTOs for registrations
	private RegistrationDto convertToDto(Registration r, Person p, Event e) {
		EventDto eDto = convertToDto(e);
		PersonDto pDto = convertToDto(p);
		return new RegistrationDto(pDto, eDto);
	}


	private RegistrationDto convertToDto(Registration r) {
		EventDto eDto = convertToDto(r.getEvent());
		PersonDto pDto = convertToDto(r.getPerson());
		PaypalDto ppDto = convertToDto(r.getPaypal());
		RegistrationDto rDto = new RegistrationDto(pDto, eDto,ppDto);
		return rDto;
	}


	private Person convertToDomainObject(PersonDto pDto) {
		List<Person> allPersons = service.getAllPersons();
		for (Person person : allPersons) {
			if (person.getName().equals(pDto.getName())) {
				return person;
			}
		}
		return null;
	}


	// Other extracted methods (not part of the API)






	private List<RegistrationDto> createRegistrationDtosForPerson(Person p) {
		List<Registration> registrationsForPerson = service.getRegistrationsForPerson(p);
		List<RegistrationDto> registrations = new ArrayList<RegistrationDto>();
		for (Registration r : registrationsForPerson) {
			registrations.add(convertToDto(r));
		}
		return registrations;
	}
}
