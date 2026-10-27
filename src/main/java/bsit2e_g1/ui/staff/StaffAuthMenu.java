package bsit2e_g1.ui.staff;

import bsit2e_g1.components.SupportStaff;
import bsit2e_g1.datastructures.AccountList;
import bsit2e_g1.datastructures.DepartmentList;
import bsit2e_g1.datastructures.TicketList;
import bsit2e_g1.utils.InputHelper;

import java.util.function.Consumer;

public class StaffAuthMenu {

	// Staff Login
	public static void login(AccountList accountList, TicketList ticketList, DepartmentList departmentList, Consumer<SupportStaff> onLoginSuccess) {
		System.out.println("====================================================");
		System.out.println("                  AGAPAY | LOGIN");
		System.out.println("====================================================\n");

		String email = InputHelper.readString("[AGAPAY] Enter your University Email: ").trim().toLowerCase();
		System.out.println();

		if (!email.endsWith("@ms.bulsu.edu.ph") || email.isBlank()) {
			System.out.println("[AGAPAY] Email should contain \"@ms.bulsu.edu.ph\".\n");
			return;
		}

		SupportStaff supportStaff = accountList.findStaffByEmail(email);

		if (supportStaff == null) {
			System.out.println("[AGAPAY] Account not found.\n");
			return;
		}

		if (supportStaff.isArchived()) {
			System.out.printf("[AGAPAY] Your staff account (%s) was archived on %s.\n", supportStaff.getAccountId(), supportStaff.getDateArchived());
			if (InputHelper.choice("Would you like to reactivate (unarchive) your staff account and log in?")) {
				supportStaff.unarchiveAccount();
				System.out.println("\n[AGAPAY] Staff account reactivated successfully!\n");
			} else {
				System.out.println("\n[AGAPAY] Login cancelled. Staff account remains archived.\n");
				return;
			}
		}

		System.out.println("[AGAPAY] Login successful! Welcome, " + supportStaff.getName() + "!\n");
		if (onLoginSuccess != null) {
			onLoginSuccess.accept(supportStaff);
		}
	}
}
