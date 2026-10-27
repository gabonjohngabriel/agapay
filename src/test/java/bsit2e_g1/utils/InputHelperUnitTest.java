package bsit2e_g1.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InputHelper Console Input Simulation Tests")
public class InputHelperUnitTest {

	@AfterEach
	void tearDown() {
		InputHelper.resetScanner();
	}

	@Test
	@DisplayName("choice: Verify boolean yes/no choice")
	void testChoiceYesNo() {
		InputHelper.setScanner(new Scanner("1\n2\n"));
		assertTrue(InputHelper.choice("Confirm?"));
		assertFalse(InputHelper.choice("Cancel?"));
	}

	@Test
	@DisplayName("choice: Verify array options selection")
	void testChoiceOptions() {
		String[] options = new String[]{"First", "Second", "Third"};
		InputHelper.setScanner(new Scanner("2\n"));
		String selected = InputHelper.choice("Choose:", options);
		assertEquals("Second", selected);
	}

	@Test
	@DisplayName("readInt: Unbounded integer parsing and recovery from invalid input")
	void testReadIntUnbounded() {
		InputHelper.setScanner(new Scanner("abc\n123\n"));
		assertEquals(123, InputHelper.readInt("Enter number: "));
	}

	@Test
	@DisplayName("readInt: Bounded range validation")
	void testReadIntBounded() {
		InputHelper.setScanner(new Scanner("0\n15\n7\n"));
		assertEquals(7, InputHelper.readInt("Enter between 1 and 10: ", 1, 10));
	}

	@Test
	@DisplayName("readString: Reading single line input")
	void testReadString() {
		InputHelper.setScanner(new Scanner("Ticket Subject Here\n"));
		assertEquals("Ticket Subject Here", InputHelper.readString("Prompt: "));
	}
}
