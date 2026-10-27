package bsit2e_g1.ui.user;

import bsit2e_g1.components.Department;
import bsit2e_g1.components.User;
import bsit2e_g1.datastructures.AccountList;
import bsit2e_g1.datastructures.DepartmentList;
import bsit2e_g1.datastructures.TicketList;
import bsit2e_g1.utils.InputHelper;

import java.util.function.Consumer;

public class UserAuthMenu {

	// Sign up - With Guard Rails & Confirmation
	public static void signUp(AccountList accountList, DepartmentList departmentList) {
		System.out.println("====================================================");
		System.out.println("              AGAPAY | CREATE ACCOUNT");
		System.out.println("====================================================");

		String id;
		while (true) {
			id = InputHelper.readString("[AGAPAY] Enter your User ID (Student/Faculty): ").trim();
			System.out.println();

			if (id.isBlank()) {
				if (InputHelper.choice("User ID cannot be empty. Do you want to cancel sign up?")) {
					System.out.println();
					return;
				}

				System.out.println();
				continue;
			}

			if (accountList.findAccountById(id) != null) {
				System.out.println("[AGAPAY] An account with this ID is already registered.\n");

				if (InputHelper.choice("Do you want to cancel sign up and go back?")) {
					System.out.println();
					return;
				}

				System.out.println();
				continue;
			}

			if (id.matches("\\d{10}")) {
				break;
			}

			System.out.println("[AGAPAY] Invalid User ID. Please enter a valid ID (e.g., 202500****).\n");
		}

		String name;

		while (true) {
			name = InputHelper.readString("[AGAPAY] Enter your Full Name (Last Name, First Name, MI): ").trim();
			System.out.println();

			if (!name.isBlank()) {
				break;
			}

			if (InputHelper.choice("Full name cannot be empty. Do you want to cancel sign up?")) {
				System.out.println();
				return;
			}

			System.out.println();
		}

		String email;

		while (true) {
			email = InputHelper.readString("[AGAPAY] Enter your University Email: ").trim().toLowerCase();
			System.out.println();

			if (email.endsWith("@ms.bulsu.edu.ph") && !email.equals("@ms.bulsu.edu.ph")) {
				if (accountList.findUserByEmail(email) != null || accountList.findStaffByEmail(email) != null) {
					System.out.println("[AGAPAY] This email is already registered.\n");
				} else {
					break;
				}
			} else {
				System.out.println("[AGAPAY] Valid university email required (@ms.bulsu.edu.ph).\n");
			}

			// Guard rail if the user does not want to continue
			if (InputHelper.choice("Do you want to cancel registration and go back?")) {
				System.out.println();
				return;
			}
			System.out.println();
		}

		String contactNumber;
		while (true) {
			contactNumber = InputHelper.readString("[AGAPAY] Enter your Contact Number (11 digits): ").trim();
			System.out.println();

			if (contactNumber.matches("\\d{11}")) {
				break;
			}

			System.out.println("[AGAPAY] Valid contact number required (e.g., 09123456789).\n");

			if (InputHelper.choice("Do you want to cancel registration and go back?")) {
				System.out.println();
				return;
			}

			System.out.println();
		}

		String userType = InputHelper.choice("Select User Type:", new String[]{"Student", "Faculty", "Guest"});
		System.out.println();

		String deptCode = InputHelper.choice("Select your Department:", new String[]{"CICT", "CON", "CAL"});
		System.out.println();

		Department selectedDept = (departmentList != null) ? departmentList.findDepartmentById(deptCode) : null;

		// Guard rail displaying current information before confirming registration
		System.out.println("====================================================");
		System.out.println("        AGAPAY | REVIEW REGISTRATION DETAILS");
		System.out.println("====================================================");
		System.out.printf("User ID        : %s\n", id);
		System.out.printf("Full Name      : %s\n", name);
		System.out.printf("Email          : %s\n", email);
		System.out.printf("Contact Number : %s\n", contactNumber);
		System.out.printf("User Type      : %s\n", userType);
		System.out.printf("Department     : %s\n", selectedDept != null ? selectedDept.getDepartmentName() : deptCode);
		System.out.println("====================================================\n");

		if (!InputHelper.choice("Do you want to proceed with creating this account?")) {
			System.out.println("\n[AGAPAY] Account registration cancelled.\n");
			return;
		}

		System.out.println();

		User newUser = new User(id, name, email, contactNumber, userType, selectedDept);
		accountList.addUser(newUser);

		System.out.println("[AGAPAY] Account registered successfully!\n");
	}

	// Login
	public static void login(AccountList accountList, TicketList ticketList, DepartmentList departmentList, Consumer<User> onLoginSuccess) {
		System.out.println("====================================================");
		System.out.println("                  AGAPAY | LOGIN");
		System.out.println("====================================================\n");

		String email = InputHelper.readString("[AGAPAY] Enter your University Email: ").trim().toLowerCase();
		System.out.println();

		if (!email.endsWith("@ms.bulsu.edu.ph") || email.isBlank()) {
			System.out.println("[AGAPAY] Valid university email required (@ms.bulsu.edu.ph).\n");
			return;
		}

		User user = accountList.findUserByEmail(email);

		if (user == null) {
			System.out.println("[AGAPAY] Account not found. Please sign up first.\n");
			return;
		}

		if (user.isArchived()) {
			System.out.printf("[AGAPAY] Your account (%s) was deactivated on %s.\n", user.getAccountId(), user.getDateArchived());
			if (InputHelper.choice("Would you like to reactivate your account and log in?")) {
				user.unarchiveAccount();
				System.out.println("\n[AGAPAY] Account reactivated successfully!\n");
			} else {
				System.out.println("\n[AGAPAY] Login cancelled. Account remains archived.\n");
				return;
			}
		}

		System.out.println("[AGAPAY] Login successful! Welcome, " + user.getName() + "!\n");

		if (onLoginSuccess != null) {
			onLoginSuccess.accept(user);
		}
	}
}
