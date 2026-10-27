package bsit2e_g1.utils;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InputHelper {
	static Scanner s = new Scanner(System.in);

	// CHOICE (Yes / No)
	public static boolean choice(String prompt) {
		System.out.printf("[AGAPAY] %s\n", prompt);
		System.out.println("[1] Yes");
		System.out.println("[2] No");
		return readInt("Choice: ", 1, 2) == 1;
	}

	// CHOICE (Options)
	public static String choice(String prompt, String[] options) {
		System.out.printf("[AGAPAY] %s\n", prompt);

		for (int i = 0; i < options.length; i++) {
			System.out.printf("[%d] %s\n", i + 1, options[i]);
		}

		System.out.println();

		int selected = readInt("Choice: ", 1, options.length);

		return options[selected - 1];
	}

	// READ INT (Unbounded)
	public static int readInt(String prompt) {
		while (true) {
			System.out.print(prompt);
			try {
				int value = s.nextInt();
				s.nextLine(); // Clear buffer
				return value;
			} catch (InputMismatchException e) {
				s.nextLine(); // Clear buffer
				System.out.println();
				System.out.println("[AGAPAY] Invalid input. Please enter a valid number.\n");
			}
		}
	}

	// READ INT (With Min & Max Bounds)
	public static int readInt(String prompt, int min, int max) {
		while (true) {
			System.out.print(prompt);

			try {
				int choice = s.nextInt();
				s.nextLine(); // Clear buffer

				if (choice >= min && choice <= max) {
					return choice;
				}

				System.out.println();
				System.out.printf("[AGAPAY] Please choose between %d and %d.\n\n", min, max);
			} catch (InputMismatchException e) {
				s.nextLine(); // Clear buffer

				System.out.println();

				System.out.println("[AGAPAY] Invalid input. Please enter a valid number.\n");
			}
		}
	}

	// READ STRING
	public static String readString(String prompt) {
		System.out.print(prompt);
		return s.nextLine();
	}

	public static void setScanner(Scanner scanner) {
		s = (scanner != null) ? scanner : new Scanner(System.in);
	}

	public static void resetScanner() {
		s = new Scanner(System.in);
	}
}
