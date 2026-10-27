package bsit2e_g1.ui.user;

import bsit2e_g1.components.User;
import bsit2e_g1.datastructures.AccountList;
import bsit2e_g1.datastructures.DepartmentList;
import bsit2e_g1.datastructures.TicketList;
import bsit2e_g1.ui.Operations;
import bsit2e_g1.utils.InputHelper;

public class UserMenu implements Operations {

	@Override
	public void run(AccountList accountList, TicketList ticketList, DepartmentList departmentList) {
		while (true) {
			System.out.println("====================================================");
			System.out.println("     AGAPAY | CAMPUS HELP DESK TICKETING SYSTEM");
			System.out.println("====================================================");
			System.out.println("[1] Log In");
			System.out.println("[2] Sign Up");
			System.out.println("[0] Back to Main Menu");
			System.out.println("====================================================\n");

			int choice = InputHelper.readInt("Choice: ", 0, 2);
			System.out.println();

			if (choice == 0) {
				break;
			} else if (choice == 1) {
				UserAuthMenu.login(accountList, ticketList, departmentList, user -> {
					userMenu(user, accountList, ticketList, departmentList);
				});
			} else if (choice == 2) {
				UserAuthMenu.signUp(accountList, departmentList);
			}
		}
	}

	// User dashboard menu
	private void userMenu(User currentUser, AccountList accountList, TicketList ticketList, DepartmentList departmentList) {
		while (true) {
			int unread = currentUser.getNotifications().getUnreadCount();
			String notifLabel = unread > 0 ? String.format("[9] View Notifications (%d NEW)", unread) : "[9] View Notifications";

			System.out.println("====================================================");
			System.out.println("     AGAPAY | CAMPUS HELP DESK TICKETING SYSTEM");
			System.out.println("====================================================");
			System.out.printf("Hello, %s! (%s)\n", currentUser.getName(), currentUser.getRoleTitle());
			System.out.printf("User ID: %s\n", currentUser.getAccountId());
			System.out.println("----------------------------------------------------");
			System.out.println("[1] Create Ticket/Report Issue");
			System.out.println("[2] View My Active Tickets");
			System.out.println("[3] View Ticket Details");
			System.out.println("[4] Search Tickets");
			System.out.println("[5] Cancel a Ticket");
			System.out.println("[6] Archive/Unarchive Tickets");
			System.out.println("[7] View Account Profile");
			System.out.println("[8] Account Settings");
			System.out.println(notifLabel);
			System.out.println("[0] Log Out");
			System.out.println("====================================================\n");

			int choice = InputHelper.readInt("Choice: ", 0, 9);
			System.out.println();

			if (choice == 0) {
				System.out.println("[AGAPAY] Logging out...\n");
				break;
			} else if (choice == 1) {
				UserTicketMenu.createTicket(currentUser, ticketList, departmentList);
			} else if (choice == 2) {
				currentUser.viewMyTickets(ticketList);
			} else if (choice == 3) {
				UserTicketMenu.viewTicket(currentUser, ticketList);
			} else if (choice == 4) {
				UserTicketMenu.filterTickets(currentUser, ticketList);
			} else if (choice == 5) {
				UserTicketMenu.cancelTicket(currentUser, ticketList);
			} else if (choice == 6) {
				UserArchiveMenu.manageArchivedTickets(currentUser, ticketList);
			} else if (choice == 7) {
				currentUser.displayInfo();
			} else if (choice == 8) {
				boolean archivedAccount = UserProfileMenu.updateProfile(currentUser, accountList, ticketList);
				
				if (archivedAccount) {
					break;
				}
			} else if (choice == 9) {
				UserNotificationMenu.notifications(currentUser);
			}
		}
	}
}
