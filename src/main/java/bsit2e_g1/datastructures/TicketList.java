package bsit2e_g1.datastructures;

import bsit2e_g1.components.Department;
import bsit2e_g1.components.SupportStaff;
import bsit2e_g1.components.Ticket;
import bsit2e_g1.components.User;
import bsit2e_g1.nodes.TicketNode;

public class TicketList {
	private TicketNode head;

	public TicketList() {
		this.head = null;
	}

	public TicketNode getHead() {
		return head;
	}

	public boolean isEmpty() {
		return head == null;
	}

	public void addTicket(Ticket ticket) {
		if (ticket == null) return;
		TicketNode newTicket = new TicketNode(ticket);

		if (head == null) {
			head = newTicket;
		} else {
			TicketNode current = head;
			while (current.next != null) {
				current = current.next;
			}
			current.next = newTicket;
		}
	}

	public boolean archiveTicket(String ticketId) {
		if (head == null) {
			System.out.println("[AGAPAY] No tickets submitted yet.\n");
			return false;
		}

		Ticket ticket = findTicketById(ticketId);
		if (ticket == null) {
			System.out.println("[AGAPAY] Ticket not found.\n");
			return false;
		}

		return ticket.archive();
	}

	public boolean unarchiveTicket(String ticketId) {
		if (head == null) {
			System.out.println("[AGAPAY] No tickets submitted yet.\n");
			return false;
		}

		Ticket ticket = findTicketById(ticketId);
		if (ticket == null) {
			System.out.println("[AGAPAY] Ticket not found.\n");
			return false;
		}

		return ticket.unarchive();
	}

	public void displayTickets(User user) {
		System.out.println("====================================================");
		System.out.println("              AGAPAY | MY TICKETS");
		System.out.println("====================================================");

		TicketNode currentTicket = head;
		boolean found = false;

		while (currentTicket != null) {
			Ticket t = currentTicket.ticket;

			if (t != null && !t.isArchived() && t.getSubmitter() != null && t.getSubmitter().getAccountId().equalsIgnoreCase(user.getAccountId())) {
				t.displaySummary();
				System.out.println("====================================================");
				found = true;
			}

			currentTicket = currentTicket.next;
		}

		if (!found) {
			System.out.println("[AGAPAY] You have no active tickets.\n");
		} else {
			System.out.println();
		}
	}

	public void displayArchivedTickets(User user) {
		System.out.println("====================================================");
		System.out.println("          AGAPAY | MY ARCHIVED TICKETS");
		System.out.println("====================================================");

		TicketNode currentTicket = head;
		boolean found = false;

		while (currentTicket != null) {
			Ticket t = currentTicket.ticket;

			if (t != null && t.isArchived() && t.getSubmitter() != null && t.getSubmitter().getAccountId().equalsIgnoreCase(user.getAccountId())) {
				t.displaySummary();
				System.out.println("====================================================");
				found = true;
			}

			currentTicket = currentTicket.next;
		}

		if (!found) {
			System.out.println("[AGAPAY] You have no archived tickets.\n");
		} else {
			System.out.println();
		}
	}

	public void displayTicketsByDepartment(Department dept) {
		if (dept == null) {
			displayAllTickets();
			return;
		}

		System.out.println("====================================================");
		System.out.printf("     AGAPAY | %s DEPARTMENT TICKET QUEUE\n", dept.getDepartmentId().toUpperCase());
		System.out.println("====================================================");

		if (head == null) {
			System.out.printf("[AGAPAY] No active tickets for %s.\n\n", dept.getDepartmentId());
			return;
		}

		TicketNode current = head;
		boolean hasTickets = false;

		while (current != null) {
			Ticket currentTicket = current.ticket;

			if (currentTicket != null && !currentTicket.isArchived() && matchesDepartment(currentTicket, dept)) {
				hasTickets = true;
				currentTicket.displaySummary();
				System.out.println("====================================================");
			}
			current = current.next;
		}

		if (!hasTickets) {
			System.out.printf("[AGAPAY] No active tickets for %s.\n\n", dept.getDepartmentId());
		} else {
			System.out.println();
		}
	}

	public void displayArchivedTicketsByDepartment(Department dept) {
		if (dept == null) {
			displayAllArchivedTickets();
			return;
		}

		System.out.println("====================================================");
		System.out.printf("   AGAPAY | %s DEPARTMENT ARCHIVED TICKETS\n", dept.getDepartmentId().toUpperCase());
		System.out.println("====================================================");

		if (head == null) {
			System.out.printf("[AGAPAY] No archived tickets for %s.\n\n", dept.getDepartmentId());
			return;
		}

		TicketNode current = head;
		boolean hasTickets = false;

		while (current != null) {
			Ticket currentTicket = current.ticket;

			if (currentTicket != null && currentTicket.isArchived() && matchesDepartment(currentTicket, dept)) {
				hasTickets = true;
				currentTicket.displaySummary();
				System.out.println("====================================================");
			}
			current = current.next;
		}

		if (!hasTickets) {
			System.out.printf("[AGAPAY] No archived tickets for %s.\n\n", dept.getDepartmentId());
		} else {
			System.out.println();
		}
	}

	public void displayAllTickets() {
		System.out.println("====================================================");
		System.out.println("           AGAPAY | ALL SUBMITTED TICKETS");
		System.out.println("====================================================");

		if (head == null) {
			System.out.println("[AGAPAY] No active tickets submitted yet.\n");
			return;
		}

		TicketNode current = head;
		boolean hasTickets = false;

		while (current != null) {
			Ticket currentTicket = current.ticket;

			if (currentTicket != null && !currentTicket.isArchived()) {
				hasTickets = true;
				currentTicket.displaySummary();
				System.out.println("====================================================");
			}
			current = current.next;
		}

		if (!hasTickets) {
			System.out.println("[AGAPAY] No active tickets submitted yet.\n");
		} else {
			System.out.println();
		}
	}

	public void displayAllArchivedTickets() {
		System.out.println("====================================================");
		System.out.println("           AGAPAY | ALL ARCHIVED TICKETS");
		System.out.println("====================================================");

		if (head == null) {
			System.out.println("[AGAPAY] No archived tickets found.\n");
			return;
		}

		TicketNode current = head;
		boolean hasTickets = false;

		while (current != null) {
			Ticket currentTicket = current.ticket;

			if (currentTicket != null && currentTicket.isArchived()) {
				hasTickets = true;
				currentTicket.displaySummary();
				System.out.println("====================================================");
			}
			current = current.next;
		}

		if (!hasTickets) {
			System.out.println("[AGAPAY] No archived tickets found.\n");
		} else {
			System.out.println();
		}
	}

	private boolean matchesDepartment(Ticket t, Department dept) {
		if (dept == null) return true;
		if (t == null) return false;
		if (t.getCategory() != null && t.getCategory().getDepartment() != null && t.getCategory().getDepartment().getDepartmentId() != null) {
			return t.getCategory().getDepartment().getDepartmentId().equalsIgnoreCase(dept.getDepartmentId());
		}
		if (t.getSubmitter() != null && t.getSubmitter().getDepartment() != null && t.getSubmitter().getDepartment().getDepartmentId() != null) {
			return t.getSubmitter().getDepartment().getDepartmentId().equalsIgnoreCase(dept.getDepartmentId());
		}
		return false;
	}

	public Ticket findTicketById(String ticketId) {
		if (ticketId == null || ticketId.isBlank()) return null;
		TicketNode current = head;
		while (current != null) {
			Ticket t = current.ticket;
			if (t != null && t.getTicketId() != null && t.getTicketId().equalsIgnoreCase(ticketId.trim())) {
				return t;
			}
			current = current.next;
		}
		return null;
	}

	public boolean hasInProgressTicketsForUser(User user) {
		if (user == null) return false;
		TicketNode current = head;
		while (current != null) {
			Ticket t = current.ticket;
			if (t != null && !t.isArchived()
					&& t.getSubmitter() != null
					&& t.getSubmitter().getAccountId() != null
					&& t.getSubmitter().getAccountId().equalsIgnoreCase(user.getAccountId())
					&& "In Progress".equalsIgnoreCase(t.getStatus())) {
				return true;
			}
			current = current.next;
		}
		return false;
	}

	public boolean hasInProgressTicketsForStaff(SupportStaff staff) {
		if (staff == null) return false;
		TicketNode current = head;
		while (current != null) {
			Ticket t = current.ticket;
			if (t != null && !t.isArchived()
					&& t.getAssignedStaff() != null
					&& t.getAssignedStaff().getAccountId() != null
					&& t.getAssignedStaff().getAccountId().equalsIgnoreCase(staff.getAccountId())
					&& "In Progress".equalsIgnoreCase(t.getStatus())) {
				return true;
			}
			current = current.next;
		}
		return false;
	}

	public int cancelPendingTicketsForUser(User user) {
		if (user == null) return 0;
		int count = 0;
		TicketNode current = head;
		while (current != null) {
			Ticket t = current.ticket;
			if (t != null && !t.isArchived()
					&& t.getSubmitter() != null
					&& t.getSubmitter().getAccountId() != null
					&& t.getSubmitter().getAccountId().equalsIgnoreCase(user.getAccountId())
					&& "Pending".equalsIgnoreCase(t.getStatus())) {
				if (t.cancel(user)) {
					count++;
				}
			}
			current = current.next;
		}
		return count;
	}

	public void filterByStatus(String status, User user) {
		filterByStatus(status, user, null, false);
	}

	public void filterByStatus(String status, User user, Department dept) {
		filterByStatus(status, user, dept, false);
	}

	public void filterByStatus(String status, User user, Department dept, Boolean archiveFilter) {
		String scope = (archiveFilter == null) ? "ALL TICKETS" : (archiveFilter ? "ARCHIVED TICKETS" : "ACTIVE TICKETS");
		System.out.println("====================================================");
		System.out.printf(" AGAPAY | SHOWING %s RELATED TO '%s'\n", scope, status.toUpperCase());
		System.out.println("====================================================");

		TicketNode currentTicket = head;
		boolean found = false;

		while (currentTicket != null) {
			Ticket t = currentTicket.ticket;

			if (t != null && t.getStatus() != null && t.getStatus().equalsIgnoreCase(status)) {
				boolean archiveMatch = (archiveFilter == null) || (archiveFilter.booleanValue() == t.isArchived());
				boolean userMatch = (user == null) || (t.getSubmitter() != null && t.getSubmitter().getAccountId() != null && t.getSubmitter().getAccountId().equalsIgnoreCase(user.getAccountId()));
				boolean deptMatch = matchesDepartment(t, dept);

				if (archiveMatch && userMatch && deptMatch) {
					if (found) {
						System.out.println("----------------------------------------------------");
					}
					t.displaySummary();
					found = true;
				}
			}

			currentTicket = currentTicket.next;
		}

		if (!found) {
			System.out.println("[AGAPAY] No matching tickets found.");
		}

		System.out.println("====================================================");
	}

	public void filterByPriority(String priority, User user) {
		filterByPriority(priority, user, null, false);
	}

	public void filterByPriority(String priority, User user, Department dept) {
		filterByPriority(priority, user, dept, false);
	}

	public void filterByPriority(String priority, User user, Department dept, Boolean archiveFilter) {
		String scope = (archiveFilter == null) ? "ALL TICKETS" : (archiveFilter ? "ARCHIVED TICKETS" : "ACTIVE TICKETS");
		System.out.println("====================================================");
		System.out.printf(" AGAPAY | SHOWING %s RELATED TO '%s'\n", scope, priority.toUpperCase());
		System.out.println("====================================================");

		TicketNode currentTicket = head;
		boolean found = false;

		while (currentTicket != null) {
			Ticket t = currentTicket.ticket;

			if (t != null && t.getPriority() != null && t.getPriority().equalsIgnoreCase(priority)) {
				boolean archiveMatch = (archiveFilter == null) || (archiveFilter.booleanValue() == t.isArchived());
				boolean userMatch = (user == null) || (t.getSubmitter() != null && t.getSubmitter().getAccountId() != null && t.getSubmitter().getAccountId().equalsIgnoreCase(user.getAccountId()));
				boolean deptMatch = matchesDepartment(t, dept);

				if (archiveMatch && userMatch && deptMatch) {
					if (found) {
						System.out.println("----------------------------------------------------");
					}
					t.displaySummary();
					found = true;
				}
			}

			currentTicket = currentTicket.next;
		}

		if (!found) {
			System.out.println("[AGAPAY] No matching tickets found.");
		}

		System.out.println("====================================================");
	}

	public void generateSummaryReport() {
		generateSummaryReport(null);
	}

	public void generateSummaryReport(Department deptFilter) {
		int total = 0, active = 0, archived = 0;
		int pending = 0, inProgress = 0, resolved = 0, closed = 0, cancelled = 0;
		int low = 0, med = 0, high = 0, urgent = 0;

		TicketNode currentTicket = head;

		while (currentTicket != null) {
			Ticket t = currentTicket.ticket;

			if (t != null && matchesDepartment(t, deptFilter)) {
				total++;
				if (t.isArchived()) {
					archived++;
				} else {
					active++;
				}

				if ("Pending".equalsIgnoreCase(t.getStatus()))
					pending++;
				else if ("In Progress".equalsIgnoreCase(t.getStatus()))
					inProgress++;
				else if ("Resolved".equalsIgnoreCase(t.getStatus()))
					resolved++;
				else if ("Closed".equalsIgnoreCase(t.getStatus()))
					closed++;
				else if ("Cancelled".equalsIgnoreCase(t.getStatus()))
					cancelled++;

				if ("Low".equalsIgnoreCase(t.getPriority()))
					low++;
				else if ("Medium".equalsIgnoreCase(t.getPriority()))
					med++;
				else if ("High".equalsIgnoreCase(t.getPriority()))
					high++;
				else if ("Urgent".equalsIgnoreCase(t.getPriority()))
					urgent++;
			}

			currentTicket = currentTicket.next;
		}

		String scope = (deptFilter != null) ? " (" + deptFilter.getDepartmentId() + ")" : " (ALL DEPARTMENTS)";
		System.out.println("====================================================");
		System.out.println("  AGAPAY | STATISTICAL REPORT" + scope);
		System.out.println("====================================================");
		System.out.printf("Total Tickets Filed: %d (Active: %d | Archived: %d)\n", total, active, archived);
		System.out.println("----------------------------------------------------");
		System.out.println("BREAKDOWN BY STATUS:");
		System.out.printf("Pending       : %d\n", pending);
		System.out.printf("In Progress   : %d\n", inProgress);
		System.out.printf("Resolved      : %d\n", resolved);
		System.out.printf("Closed        : %d\n", closed);
		System.out.printf("Cancelled     : %d\n", cancelled);
		System.out.println("----------------------------------------------------");
		System.out.println("BREAKDOWN BY PRIORITY:");
		System.out.printf("Low           : %d\n", low);
		System.out.printf("Medium        : %d\n", med);
		System.out.printf("High          : %d\n", high);
		System.out.printf("Urgent        : %d\n", urgent);
		System.out.println("====================================================\n");
	}
}
