package bsit2e_g1.ui.staff;

import bsit2e_g1.components.Comment;
import bsit2e_g1.components.Department;
import bsit2e_g1.components.SupportStaff;
import bsit2e_g1.components.Ticket;
import bsit2e_g1.datastructures.TicketList;
import bsit2e_g1.utils.InputHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StaffTicketMenu {

	// Accept ticket
	public static void acceptTicket(SupportStaff supportStaff, TicketList ticketList) {
		System.out.println("====================================================");
		System.out.println("               AGAPAY | ACCEPT TICKET");
		System.out.println("====================================================\n");

		String ticketId = InputHelper.readString("[AGAPAY] Enter Ticket ID: ");
		System.out.println();

		boolean isAccepted = supportStaff.acceptTicket(ticketList, ticketId.trim());

		if (isAccepted) {
			System.out.printf("[AGAPAY] Ticket %s accepted by %s. Status changed to \"In Progress\".\n\n", ticketId.trim().toUpperCase(), supportStaff.getName());
		}
	}

	// Resolve ticket
	public static void resolveTicket(SupportStaff supportStaff, TicketList ticketList) {
		System.out.println("====================================================");
		System.out.println("               AGAPAY | RESOLVE TICKET");
		System.out.println("====================================================\n");

		String ticketId = InputHelper.readString("[AGAPAY] Enter Ticket ID: ");
		System.out.println();

		Ticket currentTicket = ticketList.findTicketById(ticketId);

		if (currentTicket == null) {
			System.out.println("[AGAPAY] Ticket not found.\n");
			return;
		}

		if (currentTicket.isArchived()) {
			System.out.printf("[AGAPAY] Ticket %s is ARCHIVED and READ-ONLY. Restore it first to modify.\n\n", currentTicket.getTicketId());
			return;
		}

		String notes;
		while (true) {
			notes = InputHelper.readString("[AGAPAY] Enter Resolution Notes/Solution: ").trim();
			System.out.println();
			if (!notes.isBlank()) {
				break;
			}
			System.out.println("[AGAPAY] Resolution notes cannot be blank.\n");
			if (InputHelper.choice("Do you want to cancel resolving this ticket?")) {
				System.out.println();
				return;
			}
			System.out.println();
		}

		boolean isResolved = supportStaff.resolveTicket(ticketList, ticketId.trim(), notes);

		if (isResolved) {
			System.out.printf("[AGAPAY] Ticket %s successfully resolved!\n\n", currentTicket.getTicketId());
		}
	}

	// Close resolved ticket
	public static void closeTicketUI(SupportStaff staff, TicketList ticketList) {
		System.out.println("====================================================");
		System.out.println("            AGAPAY | CLOSE RESOLVED TICKET          ");
		System.out.println("====================================================\n");

		String ticketId = InputHelper.readString("[AGAPAY] Enter Ticket ID to Close: ");
		System.out.println();

		if (staff.closeTicket(ticketList, ticketId.trim())) {
			System.out.printf("[AGAPAY] Ticket %s has been officially closed.\n\n", ticketId.trim().toUpperCase());
		}
	}

	// View active tickets (department-scoped vs campus-wide)
	public static void viewActiveTickets(SupportStaff supportStaff, TicketList ticketList, boolean filterByOwnDepartmentOnly) {
		if (filterByOwnDepartmentOnly) {
			supportStaff.viewTickets(ticketList);
		} else {
			supportStaff.viewAllCampusTickets(ticketList);
		}
	}

	// Ticket details & comment
	public static void ticketDetails(SupportStaff staff, TicketList ticketList) {
		String ticketId = InputHelper.readString("[AGAPAY] Enter Ticket ID to View Details: ");
		System.out.println();

		Ticket currentTicket = ticketList.findTicketById(ticketId);

		if (currentTicket == null) {
			System.out.println("[AGAPAY] Ticket not found.\n");
			return;
		}

		currentTicket.displayDetails();

		if (currentTicket.isArchived()) {
			System.out.println("[AGAPAY] This ticket is ARCHIVED and READ-ONLY.\n");
			if (InputHelper.choice("Would you like to restore this ticket to the active queue?")) {
				System.out.println();
				if (staff.unarchiveTicket(ticketList, currentTicket.getTicketId())) {
					System.out.printf("[AGAPAY] Ticket %s has been restored to the active queue.\n\n", currentTicket.getTicketId());
				}
			} else {
				System.out.println();
			}
			return;
		}

		System.out.println("[1] Post a Comment");
		System.out.println("[0] Back\n");

		int sub = InputHelper.readInt("Choice: ", 0, 1);
		System.out.println();

		if (sub == 1) {
			if (staff.getDepartment() != null && currentTicket.getCategory() != null && currentTicket.getCategory().getDepartment() != null
					&& !staff.getDepartment().getDepartmentId().equalsIgnoreCase(currentTicket.getCategory().getDepartment().getDepartmentId())) {
				System.out.printf("[AGAPAY] ACCESS DENIED: Ticket %s belongs to %s. You can only comment on tickets for your department (%s).\n\n",
						currentTicket.getTicketId(),
						currentTicket.getCategory().getDepartment().getDepartmentId(),
						staff.getDepartment().getDepartmentId());
				return;
			}

			String content = InputHelper.readString("[AGAPAY] Enter Comment: ");
			System.out.println();

			if (content.isBlank()) {
				System.out.println("[AGAPAY] Comment cannot be blank!\n");
				return;
			}

			String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			Comment c = new Comment(now, content.trim(), staff);

			currentTicket.addComment(c);

			System.out.println("[AGAPAY] Comment added to ticket!\n");
		}
	}

	// Search and filter tickets
	public static void searchFilter(TicketList ticketList, Department activeDeptFilter) {
		System.out.println("====================================================");
		System.out.println("        AGAPAY | SEARCH AND FILTER TICKETS");
		System.out.println("====================================================\n");
		System.out.println("[1] Search by Ticket ID");
		System.out.println("[2] Filter by Status");
		System.out.println("[3] Filter by Priority");
		System.out.println("[0] Back\n");

		int choice = InputHelper.readInt("Choice: ", 0, 3);
		System.out.println();

		if (choice == 1) {
			String id = InputHelper.readString("Enter Ticket ID: ");
			System.out.println();

			Ticket currentTicket = ticketList.findTicketById(id);

			if (currentTicket != null) {
				currentTicket.displayDetails();
			} else {
				System.out.println("[AGAPAY] Ticket not found.\n");
			}
		} else if (choice == 2) {
			String status = InputHelper.choice("Select Status:", new String[]{
					"Pending",
					"In Progress",
					"Resolved",
					"Closed",
					"Cancelled"
			});
			System.out.println();

			ticketList.filterByStatus(status, null, activeDeptFilter);
		} else if (choice == 3) {
			String priority = InputHelper.choice("Select Priority:", new String[]{
					"Low",
					"Medium",
					"High",
					"Urgent"
			});
			System.out.println();

			ticketList.filterByPriority(priority, null, activeDeptFilter);
		}

		System.out.println();
	}
}
