package bsit2e_g1.ui.user;

import bsit2e_g1.components.SupportStaff;
import bsit2e_g1.components.User;
import bsit2e_g1.datastructures.AccountList;
import bsit2e_g1.datastructures.TicketList;
import bsit2e_g1.utils.InputHelper;

public class UserProfileMenu {

	// Update profile - MENU BASED (returns true if account was archived)
	public static boolean updateProfile(User currentUser, AccountList accountList, TicketList ticketList) {
		String name = currentUser.getName();
		String email = currentUser.getEmail();
		String contactNumber = currentUser.getContactNumber();
		boolean hasChanges = false;

		while (true) {
			System.out.println("====================================================");
			System.out.println("             AGAPAY | UPDATE USER ACCOUNT");
			System.out.println("====================================================\n");

			System.out.println("CURRENT USER INFORMATION");
			System.out.println("[1] Full Name        : " + name);
			System.out.println("[2] University Email : " + email);
			System.out.println("[3] Contact Number   : " + contactNumber);
			System.out.println("[4] Save Changes");
			System.out.println("[5] Deactivate Account");
			System.out.println("[0] Cancel / Back\n");

			int choice = InputHelper.readInt("Choice: ", 0, 5);
			System.out.println();

			if (choice == 1) {
				String newName = InputHelper.readString("[AGAPAY] Enter your Full Name (Last Name, First Name, MI): ");
				System.out.println();

				if (!newName.isBlank()) {
					name = newName.trim();
					hasChanges = true;
					System.out.println("[AGAPAY] Full Name updated.\n");
				} else {
					System.out.println("[AGAPAY] Name cannot be blank. Keeping previous value.\n");
				}

			} else if (choice == 2) {
				while (true) {
					String newEmail = InputHelper.readString("[AGAPAY] Enter your University Email (press Enter to cancel): ");
					System.out.println();

					newEmail = newEmail.toLowerCase().trim();

					if (newEmail.isBlank()) {
						System.out.println("[AGAPAY] Email update cancelled. Keeping previous value.\n");
						break;
					}

					if (newEmail.endsWith("@ms.bulsu.edu.ph") && !newEmail.equals("@ms.bulsu.edu.ph")) {
						User existingUser = accountList.findUserByEmail(newEmail);
						SupportStaff existingStaff = accountList.findStaffByEmail(newEmail);
						if ((existingUser != null && !existingUser.getAccountId().equalsIgnoreCase(currentUser.getAccountId()))
								|| existingStaff != null) {
							System.out.println("[AGAPAY] This email is already registered to another account.\n");
							continue;
						}
						email = newEmail;
						hasChanges = true;
						System.out.println("[AGAPAY] Email updated.\n");
						break;
					}

					System.out.println("[AGAPAY] Email must end with \"@ms.bulsu.edu.ph\".\n");
				}

			} else if (choice == 3) {
				String newContact = InputHelper.readString("[AGAPAY] Enter your Contact Number (11 digits): ").trim();
				System.out.println();

				if (newContact.matches("\\d{11}")) {
					contactNumber = newContact;
					hasChanges = true;
					System.out.println("[AGAPAY] Contact Number updated.\n");
				} else {
					System.out.println("[AGAPAY] Valid 11-digit Contact Number required. Keeping previous value.\n");
				}
			} else if (choice == 4) {
				if (!hasChanges) {
					System.out.println("[AGAPAY] No changes were made.\n");
					return false;
				}

				boolean isUpdated = accountList.updateUser(currentUser, name, email, contactNumber);

				if (isUpdated) {
					System.out.println("[AGAPAY] User information updated successfully.\n");
					return false;
				} else {
					System.out.println("[AGAPAY] Failed to update user information.\n");
				}

			} else if (choice == 5) {
				System.out.println("[AGAPAY] Deactivating it means it will automatically cancel any \"Pending\" tickets.");
				System.out.println("[AGAPAY] You can reactivate your account later by logging in again.\n");

				if (InputHelper.choice("Are you sure you want to deactivate your account?")) {
					System.out.println();
					if (accountList.archiveUserAccount(currentUser, ticketList)) {
						System.out.printf("[AGAPAY] Account %s has been deactivated. Logging out...\n\n", currentUser.getAccountId());
						return true;
					}
				} else {
					System.out.println("\n[AGAPAY] Account deactivation cancelled.\n");
				}

			} else if (choice == 0) {
				if (hasChanges) {
					if (!InputHelper.choice("You have unsaved changes. Are you sure you want to discard them?")) {
						continue;
					}
				}

				System.out.println("[AGAPAY] Update cancelled.\n");
				return false;
			}
		}
	}
}
