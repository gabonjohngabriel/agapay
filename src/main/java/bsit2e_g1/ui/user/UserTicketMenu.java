package bsit2e_g1.ui.user;

import bsit2e_g1.components.*;
import bsit2e_g1.datastructures.CategoryList;
import bsit2e_g1.datastructures.DepartmentList;
import bsit2e_g1.datastructures.TicketList;
import bsit2e_g1.utils.InputHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserTicketMenu {

	// Create ticket
	public static void createTicket(User currentUser, TicketList ticketList, DepartmentList departmentList) {
		System.out.println("====================================================");
		System.out.println("              AGAPAY | CREATE TICKET");
		System.out.println("====================================================\n");

		String title;

		while (true) {
			title = InputHelper.readString("[AGAPAY] Enter Ticket Title/Subject: ");
			System.out.println();

			if (!title.isBlank()) {
				break;
			}

			System.out.println("[AGAPAY] Ticket title required!\n");
		}

		String description = InputHelper.readString("[AGAPAY] Enter Description of the Issue: ");
		System.out.println();

		String deptCode = InputHelper.choice("Select Target Department for this Issue:", new String[]{
				"CICT",
				"CON",
				"CAL"
		});
		System.out.println();

		Department targetDept = (departmentList != null) ? departmentList.findDepartmentById(deptCode) : currentUser.getDepartment();

		if (targetDept == null) {
			targetDept = currentUser.getDepartment();
		}

		Category selectedCategory = null;

		if (targetDept != null && !targetDept.getCategories().isEmpty()) {
			CategoryList catList = targetDept.getCategories();

			System.out.println("Select Category (" + targetDept.getDepartmentId() + "):");
			catList.displayCategories();
			System.out.println();

			int catChoice = InputHelper.readInt("Choice: ", 1, catList.getSize());
			System.out.println();

			selectedCategory = catList.getByIndex(catChoice - 1);
		} else {
			selectedCategory = new Category("CAT-01", "General Campus Inquiries", "General Help Desk");
		}

		String priority = InputHelper.choice("Who is affected by this situation?", new String[]{
				"Only Me",
				"Section",
				"College",
				"Entire University",
				});
		System.out.println();

		if (priority.equalsIgnoreCase("Only Me")) {
			priority = "Low";
		} else if (priority.equalsIgnoreCase("Section")) {
			priority = "Medium";
		} else if (priority.equalsIgnoreCase("College")) {
			priority = "High";
		} else if (priority.equalsIgnoreCase("Entire University")) {
			priority = "Urgent";
		}

		if (!InputHelper.choice("Submit this ticket now?")) {
			System.out.println("\n[AGAPAY] Ticket submission cancelled.\n");
			return;
		}

		System.out.println();

		currentUser.createTicket(ticketList, title, description, priority, selectedCategory);
	}

	// View ticket - With Submitter Authorization, Archive Handling & Direct Close Option
	public static void viewTicket(User currentUser, TicketList ticketList) {
		String ticketId = InputHelper.readString("[AGAPAY] Enter Ticket ID to View: ");
		System.out.println();

		Ticket ticket = ticketList.findTicketById(ticketId);

		if (ticket == null) {
			System.out.println("[AGAPAY] Ticket not found.\n");
			return;
		}

		if (ticket.getSubmitter() == null || !ticket.getSubmitter().getAccountId().equalsIgnoreCase(currentUser.getAccountId())) {
			System.out.println("[AGAPAY] ACCESS DENIED: You can only view tickets submitted by yourself.\n");
			return;
		}

		ticket.displayDetails();

		if (ticket.isArchived()) {
			System.out.println("[AGAPAY] This ticket is ARCHIVED and READ-ONLY.\n");
			System.out.println("[1] Restore Ticket");
			System.out.println("[0] Back\n");

			int archChoice = InputHelper.readInt("Choice: ", 0, 1);
			System.out.println();

			if (archChoice == 1) {
				if (currentUser.unarchiveTicket(ticketList, ticket.getTicketId())) {
					System.out.printf("[AGAPAY] Ticket %s has been restored to your active tickets.\n\n", ticket.getTicketId());
				} else {
					return;
				}
			} else {
				return;
			}
		}

		boolean isResolved = "Resolved".equalsIgnoreCase(ticket.getStatus());
		boolean isClosed = "Closed".equalsIgnoreCase(ticket.getStatus());

		System.out.println("[1] Post a Comment / Follow-up");
		System.out.println("[2] Attach a File");

		if (isResolved) {
			System.out.println("[3] Accept Solution & Close Ticket");
			System.out.println("[4] Reopen Ticket");
		} else if (isClosed) {
			System.out.println("[3] Reopen Ticket");
			System.out.println("[4] Archive this Ticket");
		}

		System.out.println("[0] Back\n");

		int maxChoice = (isResolved || isClosed) ? 4 : 2;
		int choice = InputHelper.readInt("Choice: ", 0, maxChoice);

		System.out.println();

		if (choice == 1) {
			while (true) {
				String content = InputHelper.readString("[AGAPAY] Enter Comment: ");
				System.out.println();

				if (content.isBlank()) {
					if (InputHelper.choice("Comment cannot be empty. Want to cancel and go back?")) {
						break;
					} else {
						continue;
					}
				}

				String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				Comment comment = new Comment(now, content.trim(), currentUser);
				ticket.addComment(comment);

				System.out.println("[AGAPAY] Comment added successfully!\n");
				break;
			}
		} else if (choice == 2) {
			while (true) {
				String path = InputHelper.readString("[AGAPAY] Enter File Path or File Name (e.g., error_screenshot.png): ");
				System.out.println();

				if (path.isBlank()) {
					if (InputHelper.choice("Attachment cannot be empty. Want to cancel and go back?")) {
						break;
					} else {
						continue;
					}
				}

				Attachment att = new Attachment(path);
				ticket.addAttachment(att);

				System.out.println("[AGAPAY] File attached successfully!\n");
				break;
			}
		} else if (isResolved && choice == 3) {
			if (ticket.close(currentUser)) {
				System.out.printf("[AGAPAY] Ticket %s has been successfully closed!\n\n", ticket.getTicketId());

				if (InputHelper.choice("Would you like to archive this closed ticket now?")) {
					System.out.println();
					if (currentUser.archiveTicket(ticketList, ticket.getTicketId())) {
						System.out.printf("[AGAPAY] Ticket %s has been archived.\n\n", ticket.getTicketId());
					}
				} else {
					System.out.println();
				}
			}
		} else if (isResolved && choice == 4) {
			String reason = InputHelper.readString("[AGAPAY] Enter Reason for Reopening: ");
			System.out.println();

			if (ticket.reopen(reason)) {
				System.out.printf("[AGAPAY] Ticket %s has been reopened and set to 'In Progress'.\n\n", ticket.getTicketId());
			}
		} else if (isClosed && choice == 3) {
			String reason = InputHelper.readString("[AGAPAY] Enter Reason for Reopening: ");
			System.out.println();

			if (ticket.reopen(reason)) {
				System.out.printf("[AGAPAY] Ticket %s has been reopened and set to 'In Progress'.\n\n", ticket.getTicketId());
			}
		} else if (isClosed && choice == 4) {
			if (currentUser.archiveTicket(ticketList, ticket.getTicketId())) {
				System.out.printf("[AGAPAY] Ticket %s has been archived.\n\n", ticket.getTicketId());
			}
		}
	}

	// Filter ticket
	public static void filterTickets(User currentUser, TicketList ticketList) {
		System.out.println("====================================================");
		System.out.println("               AGAPAY | SEARCH TICKET");
		System.out.println("====================================================");
		System.out.println("[1] Search by Status");
		System.out.println("[2] Search by Priority");
		System.out.println("[0] Back");
		System.out.println("====================================================\n");

		int choice = InputHelper.readInt("Choice: ", 0, 2);
		System.out.println();

		if (choice == 1) {
			String status = InputHelper.choice("Select Status:", new String[]{
					"Pending",
					"In Progress",
					"Resolved",
					"Closed",
					"Cancelled"
			});
			System.out.println();

			ticketList.filterByStatus(status, currentUser);
		} else if (choice == 2) {
			String priority = InputHelper.choice("Select Priority:", new String[]{
					"Low", "Medium", "High", "Urgent"
			});
			System.out.println();

			ticketList.filterByPriority(priority, currentUser);
		}

		System.out.println();
	}

	// Cancel ticket
	public static void cancelTicket(User currentUser, TicketList ticketList) {
		System.out.println("====================================================");
		System.out.println("               AGAPAY | CANCEL TICKET");
		System.out.println("====================================================\n");

		String ticketId = InputHelper.readString("[AGAPAY] Enter Ticket ID: ");
		System.out.println();

		if (ticketId.isBlank()) {
			System.out.println("[AGAPAY] Ticket ID cannot be empty.\n");
		} else {
			boolean success = currentUser.cancelTicket(ticketList, ticketId.trim());

			if (success) {
				System.out.println("[AGAPAY] Ticket " + ticketId.trim().toUpperCase() + " cancelled successfully.\n");
			}
		}
	}
}
