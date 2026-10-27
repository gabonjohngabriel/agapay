package bsit2e_g1.ui.staff;

import bsit2e_g1.components.Department;
import bsit2e_g1.components.SupportStaff;
import bsit2e_g1.components.Ticket;
import bsit2e_g1.datastructures.TicketList;
import bsit2e_g1.utils.InputHelper;

public class StaffArchiveMenu {

	// Staff Archive Management
	public static void manageArchivedTickets(SupportStaff supportStaff, TicketList ticketList, boolean filterByOwnDepartmentOnly) {
		while (true) {
			System.out.println("====================================================");
			System.out.println("              AGAPAY | TICKET ARCHIVES");
			System.out.println("====================================================");
			System.out.println("[1] View Archived Tickets");
			System.out.println("[2] View Details of an Archived Ticket");
			System.out.println("[3] Archive Ticket");
			System.out.println("[4] Unarchive/Restore Ticket");
			System.out.println("[5] Filter Archived Tickets");
			System.out.println("[0] Back");
			System.out.println("====================================================\n");

			int choice = InputHelper.readInt("Choice: ", 0, 5);
			System.out.println();

			if (choice == 0) {
				break;
			} else if (choice == 1) {
				viewArchivedTickets(supportStaff, ticketList, filterByOwnDepartmentOnly);
			} else if (choice == 2) {
				String ticketId = InputHelper.readString("[AGAPAY] Enter Archived Ticket ID: ").trim();
				System.out.println();
				Ticket ticket = ticketList.findTicketById(ticketId);
				if (ticket == null) {
					System.out.println("[AGAPAY] Ticket not found.\n");
				} else if (!ticket.isArchived()) {
					System.out.printf("[AGAPAY] Ticket %s is active. Use Option 5 from main menu to view.\n\n", ticketId);
				} else {
					ticket.displayDetails();
					if (InputHelper.choice("Would you like to restore this ticket to the active queue?")) {
						System.out.println();
						if (supportStaff.unarchiveTicket(ticketList, ticket.getTicketId())) {
							System.out.printf("[AGAPAY] Ticket %s has been restored to the active queue.\n\n", ticket.getTicketId());
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
				} else if (supportStaff.archiveTicket(ticketList, ticketId)) {
					System.out.printf("[AGAPAY] Ticket %s has been archived.\n\n", ticketId.toUpperCase());
				}
			} else if (choice == 4) {
				String ticketId = InputHelper.readString("[AGAPAY] Enter Ticket ID to Unarchive: ").trim();
				System.out.println();
				if (ticketId.isBlank()) {
					System.out.println("[AGAPAY] Ticket ID cannot be empty.\n");
				} else if (supportStaff.unarchiveTicket(ticketList, ticketId)) {
					System.out.printf("[AGAPAY] Ticket %s has been restored to the active queue.\n\n", ticketId.toUpperCase());
				}
			} else if (choice == 5) {
				Department activeDeptFilter = filterByOwnDepartmentOnly ? supportStaff.getDepartment() : null;
				filterArchivedTickets(ticketList, activeDeptFilter);
			}
		}
	}

	public static void viewArchivedTickets(SupportStaff supportStaff, TicketList ticketList, boolean filterByOwnDepartmentOnly) {
		if (filterByOwnDepartmentOnly) {
			supportStaff.viewArchivedTickets(ticketList);
		} else {
			supportStaff.viewAllCampusArchivedTickets(ticketList);
		}
	}

	public static void filterArchivedTickets(TicketList ticketList, Department activeDeptFilter) {
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
			ticketList.filterByStatus(status, null, activeDeptFilter, Boolean.TRUE);
		} else if (choice == 2) {
			String priority = InputHelper.choice("Select Priority:", new String[]{
					"Low", "Medium", "High", "Urgent"
			});
			System.out.println();
			ticketList.filterByPriority(priority, null, activeDeptFilter, Boolean.TRUE);
		}
		System.out.println();
	}
}
