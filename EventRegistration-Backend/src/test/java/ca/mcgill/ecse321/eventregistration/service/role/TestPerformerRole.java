package ca.mcgill.ecse321.eventregistration.service.role;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mcgill.ecse321.eventregistration.dao.*;
import ca.mcgill.ecse321.eventregistration.model.*;
import ca.mcgill.ecse321.eventregistration.service.EventRegistrationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPerformerRole {
    @Autowired
    private EventRegistrationService service;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private PerformerRepository performerRepository;

    @After
    public void clearDatabase() {
        // First, we clear registrations to avoid exceptions due to inconsistencies
        registrationRepository.deleteAll();
        // Then we can clear the other tables
        personRepository.deleteAll();
        eventRepository.deleteAll();
        performerRepository.deleteAll();
    }

    @Test
    public void test_01_CreatePerformer() {
        try {
            String name = "validname";
            service.createPerformer(name);
            List<Performer> performers = service.getAllPerformers();
            assertEquals(performers.size(), 1);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void test_02_CreatePerformerWithEmptyName() {
        try {
            String name = "";
            service.createPerformer(name);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Performer name cannot be empty!", e.getMessage());
            List<Performer> performers = service.getAllPerformers();
            assertEquals(performers.size(), 0);
        }
    }

    @Test
    public void test_04_CreatePerformerDuplicate() {
        try {
            String name = "validname";
            service.createPerformer(name);
            List<Performer> performers = service.getAllPerformers();
            assertEquals(performers.size(), 1);
        } catch (IllegalArgumentException e) {
            fail();
        }

        try {
            String name = "validname";
            service.createPerformer(name);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Performer has already been created!", e.getMessage());
            List<Performer> performers = service.getAllPerformers();
            assertEquals(performers.size(), 1);
        }
    }

    @Test
    public void test_05_PerformsEvent() {
        try {
            Performer performer = service.createPerformer("validname");
            Event event = PerformerRoleTestData.setupEvent(service, "eventname");
            service.performsEvent(performer, event);
            assertEquals(performer.getPerformsAt().size(), 1);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void test_06_PerformsEventWithNullPerformer() {
        try {
            Performer performer = null;
            Event event = PerformerRoleTestData.setupEvent(service, "eventname");
            service.performsEvent(performer, event);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Performer needs to be selected for performs!", e.getMessage());
        }
    }

    @Test
    public void test_09_PerformsEventWithNonExsistantEvent() {
        try {
            Performer performer = service.createPerformer("validname");
            Event event = new Event();
            event.setName("concert");
            service.performsEvent(performer, event);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Event does not exist!", e.getMessage());
        }
    }

    @Test
    public void test_10_GetAllPerformers() {
        try {
            service.createPerformer("validname1");
            service.createPerformer("validname2");
            List<Performer> performers = service.getAllPerformers();
            assertEquals(performers.size(), 2);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void test_11_GetPerformer() {
        try {
            service.createPerformer("performer");
            service.getPerformer("performer");
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void test_12_GetPerformerWithNullName() {
        try {
            service.getPerformer(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Person name cannot be empty!", e.getMessage());
        }
    }
}
