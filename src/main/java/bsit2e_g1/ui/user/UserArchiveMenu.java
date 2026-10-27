package bsit2e_g1.ui.user;

import bsit2e_g1.components.Ticket;
import bsit2e_g1.components.User;
import bsit2e_g1.datastructures.TicketList;
import bsit2e_g1.utils.InputHelper;

public class UserArchiveMenu {

	// Archive / Unarchive / View Archived Tickets
	public static void manageArchivedTickets(User currentUser, TicketList ticketList) {
		while (true) {
			System.out.println("====================================================");
			System.out.println("              AGAPAY | TICKET ARCHIVES");
			System.out.println("====================================================");
			System.out.println("[1] View Archived Tickets");
			System.out.println("[2] View Details");
			System.out.println("[3] Archive Ticket (Resolved / Closed / Cancelled)");
			System.out.println("[4] Restore Ticket");
			System.out.println("[5] Search Archived Tickets");
			System.out.println("[0] Back");
			System.out.println("====================================================\n");

			int choice = InputHelper.readInt("Choice: ", 0, 5);
			System.out.println();

			if (choice == 0) {
				break;
			} else if (choice == 1) {
				currentUser.viewMyArchivedTickets(ticketList);
			} else if (choice == 2) {
				String ticketId = InputHelper.readString("[AGAPAY] Enter Archived Ticket ID: ").trim();
				System.out.println();
				Ticket ticket = ticketList.findTicketById(ticketId);
				if (ticket == null) {
					System.out.println("[AGAPAY] Ticket not found.\n");
				} else if (ticket.getSubmitter() == null || !ticket.getSubmitter().getAccountId().equalsIgnoreCase(currentUser.getAccountId())) {
					System.out.println("[AGAPAY] ACCESS DENIED: You can only view tickets submitted by yourself.\n");
				} else if (!ticket.isArchived()) {
					System.out.printf("[AGAPAY] Ticket %s is active (not archived). Use Option 3 from main menu to view.\n\n", ticketId);
				} else {
					ticket.displayDetails();
					if (InputHelper.choice("Would you like to restore this ticket to your active list?")) {
						System.out.println();
						if (currentUser.unarchiveTicket(ticketList, ticket.getTicketId())) {
							System.out.printf("[AGAPAY] Ticket %s has been restored to your active tickets.\n\n", ticket.getTicketId());
						}
					} else {
						System.out.println();
					}
				}
			} else if (choice == 3) {
				String ticketId = InputHelper.readString("[AGAPAY] Enter Ticket ID to Archive: ").trim();
				System.out.println();
				if (ticketId.isBlank()) {
					System.out.println("[AGAPAY] Ticket ID cannot be empty.\n");
				} else if (currentUser.archiveTicket(ticketList, ticketId)) {
					System.out.printf("[AGAPAY] Ticket %s has been archived.\n\n", ticketId.toUpperCase());
				}
			} else if (choice == 4) {
				String ticketId = InputHelper.readString("[AGAPAY] Enter Ticket ID to Restore: ").trim();
				System.out.println();
				if (ticketId.isBlank()) {
					System.out.println("[AGAPAY] Ticket ID cannot be empty.\n");
				} else if (currentUser.unarchiveTicket(ticketList, ticketId)) {
					System.out.printf("[AGAPAY] Ticket %s has been restored to your active tickets.\n\n", ticketId.toUpperCase());
				}
			} else if (choice == 5) {
				filterArchivedTickets(currentUser, ticketList);
			}
		}
	}

	public static void filterArchivedTickets(User currentUser, TicketList ticketList) {
		System.out.println("====================================================");
		System.out.println("         AGAPAY | SEARCH ARCHIVED TICKETS");
		System.out.println("====================================================");
		System.out.println("[1] Search Archived by Status");
		System.out.println("[2] Search Archived by Priority");
		System.out.println("[0] Back\n");

		int choice = InputHelper.readInt("Choice: ", 0, 2);
		System.out.println();

		if (choice == 1) {
			String status = InputHelper.choice("Select Status:", new String[]{
					"Resolved", "Closed", "Cancelled"
			});
			System.out.println();
			ticketList.filterByStatus(status, currentUser, null, Boolean.TRUE);
		} else if (choice == 2) {
			String priority = InputHelper.choice("Select Priority:", new String[]{
					"Low", "Medium", "High", "Urgent"
			});
			System.out.println();
			ticketList.filterByPriority(priority, currentUser, null, Boolean.TRUE);
		}
		System.out.println();
	}
}
