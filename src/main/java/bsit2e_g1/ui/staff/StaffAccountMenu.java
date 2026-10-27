package bsit2e_g1.ui.staff;

import bsit2e_g1.components.SupportStaff;
import bsit2e_g1.datastructures.AccountList;
import bsit2e_g1.datastructures.TicketList;
import bsit2e_g1.utils.InputHelper;

public class StaffAccountMenu {

	public static boolean manageStaffAccount(SupportStaff supportStaff, AccountList accountList, TicketList ticketList) {
		supportStaff.displayInfo();
		System.out.println("[1] Deactivate Account");
		System.out.println("[0] Back\n");

		int choice = InputHelper.readInt("Choice: ", 0, 1);
		System.out.println();

		if (choice == 1) {
			System.out.println("[AGAPAY] Deactivating your staff account will mark you as Archived and unavailable.");
			System.out.println("[AGAPAY] You can reactivate your account later by logging in again.\n");
			if (InputHelper.choice("Are you sure you want to deactivate your staff account?")) {
				System.out.println();
				if (accountList.archiveStaffAccount(supportStaff, ticketList)) {
					System.out.printf("[AGAPAY] Staff account %s has been deactivated. Logging out...\n\n", supportStaff.getAccountId());
					return true;
				}
			} else {
				System.out.println("\n[AGAPAY] Staff account deactivation cancelled.\n");
			}
		}
		return false;
	}
}
