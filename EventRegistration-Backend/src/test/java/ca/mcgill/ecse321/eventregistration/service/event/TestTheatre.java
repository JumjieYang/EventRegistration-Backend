package ca.mcgill.ecse321.eventregistration.service.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mcgill.ecse321.eventregistration.dao.*;
import ca.mcgill.ecse321.eventregistration.service.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTheatre {

    @Autowired
    private EventRegistrationService service;

    @Autowired
    private TheatreRepository theatreRepository;

    @After
    public void clearDatabase() {
        theatreRepository.deleteAll();
    }

    @Test
    public void test_01_CreateTheatre() {
        assertEquals(0, service.getAllTheatres().size());

        String name = "Montreal Theatre Hub";
        String title = "Romeo and Juliet";

        Calendar c = Calendar.getInstance();
        c.set(2019, Calendar.JANUARY, 18);
        Date theatreDate = new Date(c.getTimeInMillis());

        LocalTime startTime = LocalTime.parse("09:00");
        LocalTime endTime = LocalTime.parse("18:00");

        try {
            service.createTheatre(name, theatreDate, Time.valueOf(startTime) , Time.valueOf(endTime), title);
        } catch (IllegalArgumentException e) {
            fail();
        }

        checkResultTheatre(name, theatreDate, startTime, endTime, title);
    }

    private void checkResultTheatre(String name, Date theatreDate, LocalTime startTime, LocalTime endTime, String title) {
        assertEquals(0, service.getAllPersons().size());
        assertEquals(1, service.getAllTheatres().size());
        assertEquals(name, service.getAllTheatres().get(0).getName());
        assertEquals(theatreDate.toString(), service.getAllTheatres().get(0).getDate().toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        assertEquals(startTime.format(formatter).toString(), service.getAllTheatres().get(0).getStartTime().toString());
        assertEquals(endTime.format(formatter).toString(), service.getAllTheatres().get(0).getEndTime().toString());
        assertEquals(title, service.getAllTheatres().get(0).getTitle());
        assertEquals(0, service.getAllRegistrations().size());
    }

    @Test
    public void test_02_CreateTheatreNull() {
        assertEquals(0, service.getAllRegistrations().size());

        String name = null;
        String title = null;
        Date theatreDate = null;
        Time startTime = null;
        Time endTime = null;

        String error = null;
        try {
            service.createTheatre(name, theatreDate, startTime, endTime, title);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // Check error
        assertTrue(error.contains("Event name cannot be empty!"));
        assertTrue(error.contains("Event date cannot be empty!"));
        assertTrue(error.contains("Event start time cannot be empty!"));
        assertTrue(error.contains("Event end time cannot be empty!"));
        // Check model in memory
        assertEquals(0, service.getAllTheatres().size());
    }

    @Test
    public void test_03_CreateTheatreNameEmpty() {
        assertEquals(0, service.getAllTheatres().size());

        String name = "";
        String title = "Othello";

        Calendar c = Calendar.getInstance();
        c.set(2019, Calendar.JANUARY, 18);
        Date theatreDate = new Date(c.getTimeInMillis());

        LocalTime startTime = LocalTime.parse("09:00");
        LocalTime endTime = LocalTime.parse("18:00");

        String error = null;
        try {
            service.createTheatre(name, theatreDate, Time.valueOf(startTime), Time.valueOf(endTime), title);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // Check error
        assertEquals("Event name cannot be empty!", error);
        // Check model in memory
        assertEquals(0, service.getAllTheatres().size());
    }

    @Test
    public void test_05_CreateTheatreEndTimeBeforeStartTime() {
        assertEquals(0, service.getAllTheatres().size());

        String name = "Centaur";
        String title = "Nutcracker";

        Calendar c = Calendar.getInstance();
        c.set(2019, Calendar.MARCH, 7);
        Date theatreDate = new Date(c.getTimeInMillis());

        LocalTime startTime = LocalTime.parse("18:00");
        LocalTime endTime = LocalTime.parse("09:00");

        String error = null;
        try {
            service.createTheatre(name, theatreDate, Time.valueOf(startTime), Time.valueOf(endTime), title);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // Check error
        assertEquals("Event end time cannot be before event start time!", error);

        // Check model in memory
        assertEquals(0, service.getAllTheatres().size());
    }

    @Test
    public void test_07_CreateTheatreTitleSpaces() {
        assertEquals(0, service.getAllTheatres().size());

        String name = "Segal Centre";
        String title = " ";

        Calendar c = Calendar.getInstance();
        c.set(2019, Calendar.NOVEMBER, 22);
        Date theatreDate = new Date(c.getTimeInMillis());

        LocalTime startTime = LocalTime.parse("09:00");
        LocalTime endTime = LocalTime.parse("18:00");

        String error = null;
        try {
            service.createTheatre(name, theatreDate, Time.valueOf(startTime), Time.valueOf(endTime), title);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // Check error
        assertEquals("Theatre title cannot be empty!", error);
        // Check model in memory
        assertEquals(0, service.getAllTheatres().size());
    }
}
