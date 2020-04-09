package ca.mcgill.ecse321.eventregistration.service.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mcgill.ecse321.eventregistration.dao.EventRepository;
import ca.mcgill.ecse321.eventregistration.dao.PaypalRepository;
import ca.mcgill.ecse321.eventregistration.dao.PersonRepository;
import ca.mcgill.ecse321.eventregistration.dao.RegistrationRepository;
import ca.mcgill.ecse321.eventregistration.model.Event;
import ca.mcgill.ecse321.eventregistration.model.Paypal;
import ca.mcgill.ecse321.eventregistration.model.Person;
import ca.mcgill.ecse321.eventregistration.model.Registration;
import ca.mcgill.ecse321.eventregistration.service.EventRegistrationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public final class TestPaymentWithPaypal {
	@Autowired
	private EventRegistrationService service;

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private RegistrationRepository registrationRepository;
	@Autowired
	private PaypalRepository paypalRepository;

	@After
	public void clearDatabase() {
		// Fisrt, we clear registrations to avoid exceptions due to inconsistencies
		registrationRepository.deleteAll();
		// Then we can clear the other tables
		personRepository.deleteAll();
		eventRepository.deleteAll();
		paypalRepository.deleteAll();
	}

	@Test
	public void test_01_testPayWithPaypal() {
		try {
			Person person = TestUtils.setupPerson(service, TestPaymentWithPaypalData.TEST01_PERSON_NAME);
			Event event = TestUtils.setupEvent(service, TestPaymentWithPaypalData.TEST01_EVENT_NAME);
			Registration r = TestUtils.register(service, person, event);
			Paypal ap = service.createPaypalPay(TestPaymentWithPaypalData.TEST01_VALID_ID,
					TestPaymentWithPaypalData.TEST01_VALID_AMOUNT);
			service.pay(r, ap);
			List<Registration> allRs = service.getAllRegistrations();
			assertEquals(allRs.size(), 1);
			paypalAsserts(ap, allRs.get(0).getPaypal());

		} catch (IllegalArgumentException e) {
			fail();
		}
	}
	
	@Test
	public void test_04_testMultiplePaysBreakNegative() {
		int breakIndex = 3;
		String[] ids = TestPaymentWithPaypalData.TEST04_VALID_IDS;
		int[] amounts = TestPaymentWithPaypalData.TEST04_PARTIAL_BREAK_AMOUNTS;
		String[] names = TestPaymentWithPaypalData.TEST03_PERSON_NAMES;
		String[] events = TestPaymentWithPaypalData.TEST03_EVENT_NAMES;

		int length = ids.length;
		Paypal[] pays = new Paypal[length];

		try {
			for (int i = 0; i < length; i++) {
				Person person = TestUtils.setupPerson(service, names[i]);
				Event event = TestUtils.setupEvent(service, events[i]);
				Registration r = TestUtils.register(service, person, event);
				pays[i] = service.createPaypalPay(ids[i], amounts[i]);
				service.pay(r, pays[i]);
			}
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(TestPaymentWithPaypalData.AMOUNT_NEGATIVE_ERROR, e.getMessage());
			List<Registration> allRs = service.getAllRegistrations();
			assertEquals(allRs.size(), breakIndex + 1);
			for (int i = 0; i < breakIndex; i++) {
				if (!contains(allRs, pays[i])) {
					fail();
				}
			}
		}
	}

	@Test
	public void test_05_testUpdatePay() {
		try {
			Person person = TestUtils.setupPerson(service, TestPaymentWithPaypalData.TEST05_PERSON_NAME);
			Event event = TestUtils.setupEvent(service, TestPaymentWithPaypalData.TEST05_EVENT_NAME);
			Registration r = TestUtils.register(service, person, event);
			Paypal ap1 = service.createPaypalPay(TestPaymentWithPaypalData.TEST05_INITIAL_ID,
					TestPaymentWithPaypalData.TEST05_INITIAL_AMOUNT);
			Paypal ap2 = service.createPaypalPay(TestPaymentWithPaypalData.TEST05_AFTER_ID,
					TestPaymentWithPaypalData.TEST05_AFTER_AMOUNT);
			service.pay(r, ap1);
			List<Registration> allRs1 = service.getAllRegistrations();
			assertEquals(allRs1.size(), 1);
			paypalAsserts(ap1, allRs1.get(0).getPaypal());
			service.pay(r, ap2);
			List<Registration> allRs2 = service.getAllRegistrations();
			assertEquals(allRs2.size(), 1);
			paypalAsserts(ap2, allRs2.get(0).getPaypal());
		} catch (IllegalArgumentException e) {
			fail();
		}
	}

	@Test
	public void test_06_testCreatePaypal() {
		try {
			Paypal ap = service.createPaypalPay(TestPaymentWithPaypalData.TEST06_VALID_ID,
					TestPaymentWithPaypalData.TEST06_VALID_AMOUNT);
			assertEquals(1, paypalRepository.count());
			for (Paypal pay : paypalRepository.findAll()) {
				paypalAsserts(ap, pay);
			}
		} catch (IllegalArgumentException e) {
			fail();
		}
	}

	@Test
	public void test_08_testPayWithRegistrationNull() {
		try {
			Person person = TestUtils.setupPerson(service, TestPaymentWithPaypalData.TEST08_PERSON_NAME);
			Event event = TestUtils.setupEvent(service, TestPaymentWithPaypalData.TEST08_EVENT_NAME);
			Registration r = TestUtils.register(service, person, event);
			Paypal ap = null;
			service.pay(r, ap);
			fail();

		} catch (IllegalArgumentException e) {
			assertEquals(TestPaymentWithPaypalData.PAY_WITH_NULL_ERROR, e.getMessage());
		}
	}

	@Test
	public void test_09_testCreatePaypalNoAt() {
		try {
			service.createPaypalPay(TestPaymentWithPaypalData.TEST09_WRONG_ID,
					TestPaymentWithPaypalData.TEST10_VALID_AMOUNT);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(TestPaymentWithPaypalData.ID_FORMAT_ERROR, e.getMessage());
		}
	}

	@Test
	public void test_10_testCreatePaypalNoDot() {
		try {
			service.createPaypalPay(TestPaymentWithPaypalData.TEST10_WRONG_ID,
					TestPaymentWithPaypalData.TEST10_VALID_AMOUNT);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(TestPaymentWithPaypalData.ID_FORMAT_ERROR, e.getMessage());
		}
	}

	@Test
	public void test_11_testCreatePaypalNull() {
		try {
			service.createPaypalPay(TestPaymentWithPaypalData.TEST11_WRONG_ID,
					TestPaymentWithPaypalData.TEST11_VALID_AMOUNT);
		} catch (IllegalArgumentException e) {
			assertEquals(TestPaymentWithPaypalData.ID_FORMAT_ERROR, e.getMessage());
		}
	}

	@Test
	public void test_12_testCreatePaypalEmpty() {
		try {
			service.createPaypalPay(TestPaymentWithPaypalData.TEST12_WRONG_ID,
					TestPaymentWithPaypalData.TEST12_VALID_AMOUNT);
		} catch (IllegalArgumentException e) {
			assertEquals(TestPaymentWithPaypalData.ID_FORMAT_ERROR, e.getMessage());
		}
	}

	@Test
	public void test_13_testCreatePaypalSpace() {
		try {
			service.createPaypalPay(TestPaymentWithPaypalData.TEST13_WRONG_ID,
					TestPaymentWithPaypalData.TEST13_VALID_AMOUNT);
		} catch (IllegalArgumentException e) {
			assertEquals(TestPaymentWithPaypalData.ID_FORMAT_ERROR, e.getMessage());
		}
	}

	@Test
	public void test_14_testCreatePaypalZero() {
		try {
			Paypal ap = service.createPaypalPay(TestPaymentWithPaypalData.TEST14_VALID_ID,
					TestPaymentWithPaypalData.TEST14_VALID_AMOUNT);
			assertEquals(1, paypalRepository.count());
			for (Paypal pay : paypalRepository.findAll()) {
				paypalAsserts(ap, pay);
			}
		} catch (IllegalArgumentException e) {
			fail();
		}
	}

	// Util Methods, no test
	public void paypalAsserts(Paypal expected, Paypal actual) {
		assertNotEquals(null, actual);
		assertEquals(expected.getAmount(), actual.getAmount());
		assertEquals(expected.getEmail(), actual.getEmail());
	}

	public boolean paypalEquals(Paypal pay1, Paypal pay2) {
		return pay2 != null && pay2.getAmount() == pay1.getAmount() && pay2.getEmail().equals(pay1.getEmail());
	}

	public boolean contains(List<Registration> rs, Paypal pay) {
		for (Registration r : rs) {
			if (paypalEquals(pay, r.getPaypal())) {
				return true;
			}
		}
		return false;
	}
}
