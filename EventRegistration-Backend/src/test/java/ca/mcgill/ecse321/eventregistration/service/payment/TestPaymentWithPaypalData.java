package ca.mcgill.ecse321.eventregistration.service.payment;

public class TestPaymentWithPaypalData {
	// Error Message expected
	public static final String ID_FORMAT_ERROR = "Email is null or has wrong format!";
	public static final String AMOUNT_NEGATIVE_ERROR = "Payment amount cannot be negative!";
	public static final String PAY_WITH_NULL_ERROR = "Registration and payment cannot be null!";

	// test 1 testPayWithCreditCard
	public static final String TEST01_PERSON_NAME = "Person";
	public static final String TEST01_EVENT_NAME = "SoccerGame";
	public static final String TEST01_VALID_ID = "ecse321@mcgill.ca";
	public static final int TEST01_VALID_AMOUNT = 10;

	// test 2 testMultipleValidPays
	public static final String[] TEST02_VALID_IDS = { "ecse321@mcgill.ca", "person@gmail.com", "ecse321@mail.mcgill.ca",
			"person@yahoo.com", "pserson@outlook.com" };
	public static final int[] TEST02_VALID_AMOUNTS = { 567, 110, 1523, 321, 235 };
	public static final String[] TEST02_PERSON_NAMES = { "Bob", "Alice", "Jack", "Seven", "Tiga" };
	public static final String[] TEST02_EVENT_NAMES = { "Soccer Game", "Movie", "Car Show", "Concert", "Exam" };

	// test 3 testMultiplePaysBreakFormat
	public static final int TEST03_BREAK_INDEX = 2;
	public static final String[] TEST03_PPARTIAL_BREAK_IDS = { "ecse321@mcgill.ca", "person@gmail.com",
			"ecse321mail.mcgill.ca", "person@yahoo.com", "pserson@outlook.com" };
	public static final int[] TEST03_VALID_AMOUNTS = { 567, 234, 1523, 678, 235 };
	public static final String[] TEST03_PERSON_NAMES = { "Bob", "Alice", "Jack", "Seven", "Tiga" };
	public static final String[] TEST03_EVENT_NAMES = { "Soccer Game", "Movie", "Car Show", "Concert", "Exam" };

	// test 4 testMultiplePaysBreakNegative
	public static final int TEST04_BREAK_INDEX = 3;
	public static final String[] TEST04_VALID_IDS = { "ecse321@mcgill.ca", "person@gmail.com", "ecse321@mail.mcgill.ca",
			"person@yahoo.com", "pserson@outlook.com" };
	public static final int[] TEST04_PARTIAL_BREAK_AMOUNTS = { 567, 234, 1523, -678, 235 };
	public static final String[] TEST04_PERSON_NAMES = { "Bob", "Alice", "Jack", "Seven", "Tiga" };
	public static final String[] TEST04_EVENT_NAMES = { "Soccer Game", "Movie", "Car Show", "Concert", "Exam" };

	// test 5 testUpdatePay
	public static final String TEST05_PERSON_NAME = "Person";
	public static final String TEST05_EVENT_NAME = "SoccerGame";
	public static final String TEST05_INITIAL_ID = "ecse321@mcgill.ca";
	public static final int TEST05_INITIAL_AMOUNT = 10;
	public static final String TEST05_AFTER_ID = "person@yahoo.com";
	public static final int TEST05_AFTER_AMOUNT = 90;

	// test 06 testCreatePaypal
	public static final String TEST06_VALID_ID = "person@yahoo.com";
	public static final int TEST06_VALID_AMOUNT = 100;

	// test 07 testPayWithPaypalNull
	public static final String TEST07_PERSON_NAME = "Person";
	public static final String TEST07_EVENT_NAME = "SoccerGame";

	// test 08 testPayWithRegistrationNull
	public static final String TEST08_PERSON_NAME = "Person";
	public static final String TEST08_EVENT_NAME = "SoccerGame";

	// test 09 testCreatePaypalNoAt
	public static final String TEST09_WRONG_ID = "ThisIDIsWrong.com";
	public static final int TEST09_VALID_AMOUNT = 10;

	// test 10 testCreatePaypalNoDot
	public static final String TEST10_WRONG_ID = "person@yahoocom";
	public static final int TEST10_VALID_AMOUNT = 100;

	// test 11 testCreatePaypalNull
	public static final String TEST11_WRONG_ID = null;
	public static final int TEST11_VALID_AMOUNT = 10;

	// test 12 testCreatePaypalEmpty
	public static final String TEST12_WRONG_ID = "";
	public static final int TEST12_VALID_AMOUNT = 10;

	// test 13 testCreatePaypalSpace
	public static final String TEST13_WRONG_ID = " ";
	public static final int TEST13_VALID_AMOUNT = 100;

	// test 14 testCreatePaypalZero
	public static final String TEST14_VALID_ID = "person@gmail.com";
	public static final int TEST14_VALID_AMOUNT = 0;

	// test 15 testCreateBitcoinNegative
	public static final String TEST15_VALID_ID = "person@outlook.com";
	public static final int TEST15_WRONG_AMOUNT = -10;
}
