package bsit2e_g1.components;

import bsit2e_g1.datastructures.TicketList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class User extends Account {
	private String userType;
	private TicketList myTickets;

	public User() {
		super();
		this.myTickets = new TicketList();
	}

	public User(String accountId, String name, String email, String contactNumber, String userType, Department department) {
		super(accountId, name, email, contactNumber, department);
		this.userType = userType;
		this.myTickets = new TicketList();
	}

	@Override
	public String getRoleTitle() {
		String deptCode = (getDepartment() != null) ? getDepartment().getDepartmentId() : "General";
		return String.format("%s - %s", userType != null ? userType : "User", deptCode);
	}

	@Override
	public void displayInfo() {
		String deptDisplay = (getDepartment() != null)
				? getDepartment().getDepartmentName() + " (" + getDepartment().getDepartmentId() + ")"
				: "N/A";
		System.out.println("====================================================");
		System.out.println("            AGAPAY | USER ACCOUNT INFO");
		System.out.println("====================================================");
		System.out.printf("Account ID     : %s\n", getAccountId());
		System.out.printf("Name           : %s\n", getName());
		System.out.printf("Email          : %s\n", getEmail());
		System.out.printf("Contact Number : %s\n", getContactNumber());
		System.out.printf("User Type      : %s\n", userType);
		System.out.printf("Department     : %s\n", deptDisplay);
		System.out.printf("Account Status : %s\n", isArchived() ? "Deactivated (Since " + getDateArchived() + ")" : "Active");
		System.out.println("====================================================\n");
	}

	public void createTicket(TicketList ticketList, String title, String description, String priority, Category category) {
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		Ticket newTicket = new Ticket(title, description, timestamp, priority, category, this);
		newTicket.submit();

		if (this.myTickets != null) {
			this.myTickets.addTicket(newTicket);
		}
		ticketList.addTicket(newTicket);

		System.out.printf("[AGAPAY] Ticket %s successfully submitted!\n\n", newTicket.getTicketId());
	}

	public void viewMyTickets(TicketList ticketList) {
		ticketList.displayTickets(this);
	}

	public void viewMyArchivedTickets(TicketList ticketList) {
		ticketList.displayArchivedTickets(this);
	}

	public void viewArchivedTickets(TicketList ticketList) {
		ticketList.displayArchivedTickets(this);
	}

	public boolean cancelTicket(TicketList ticketList, String ticketId) {
		Ticket ticket = ticketList.findTicketById(ticketId);

		if (ticket == null) {
			System.out.println("[AGAPAY] Ticket not found.\n");
			return false;
		}

		if (ticket.getSubmitter() == null || !ticket.getSubmitter().getAccountId().equalsIgnoreCase(this.getAccountId())) {
			System.out.println("[AGAPAY] ACCESS DENIED: You can only cancel tickets submitted by yourself.\n");
			return false;
		}

		// Keep ticket in list with status = "Cancelled" so history and statistical summary remain accurate
		return ticket.cancel(this);
	}

	public boolean archiveTicket(TicketList ticketList, String ticketId) {
		Ticket ticket = ticketList.findTicketById(ticketId);

		if (ticket == null) {
			System.out.println("[AGAPAY] Ticket not found.\n");
			return false;
		}

		if (ticket.getSubmitter() == null || !ticket.getSubmitter().getAccountId().equalsIgnoreCase(this.getAccountId())) {
			System.out.println("[AGAPAY] ACCESS DENIED: You can only archive tickets submitted by yourself.\n");
			return false;
		}

		return ticket.archive();
	}

	public boolean unarchiveTicket(TicketList ticketList, String ticketId) {
		Ticket ticket = ticketList.findTicketById(ticketId);

		if (ticket == null) {
			System.out.println("[AGAPAY] Ticket not found.\n");
			return false;
		}

		if (ticket.getSubmitter() == null || !ticket.getSubmitter().getAccountId().equalsIgnoreCase(this.getAccountId())) {
			System.out.println("[AGAPAY] ACCESS DENIED: You can only restore tickets submitted by yourself.\n");
			return false;
		}

		return ticket.unarchive();
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public TicketList getMyTickets() {
		return myTickets;
	}

	public void setMyTickets(TicketList myTickets) {
		this.myTickets = myTickets;
	}
}
