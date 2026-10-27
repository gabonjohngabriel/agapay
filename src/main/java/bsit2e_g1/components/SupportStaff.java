package bsit2e_g1.components;

import bsit2e_g1.datastructures.TicketList;

public class SupportStaff extends Account {
	private String specialization;
	private boolean isAvailable;

	public SupportStaff() {
		super();
		this.isAvailable = true;
	}

	public SupportStaff(String accountId, String name, String email, String contactNumber, String specialization, boolean isAvailable) {
		super(accountId, name, email, contactNumber, null);
		this.specialization = specialization;
		this.isAvailable = isAvailable;
	}

	public SupportStaff(String accountId, String name, String email, String contactNumber, String specialization, boolean isAvailable, Department department) {
		super(accountId, name, email, contactNumber, department);
		this.specialization = specialization;
		this.isAvailable = isAvailable;
	}

	@Override
	public String getRoleTitle() {
		String deptCode = (getDepartment() != null) ? getDepartment().getDepartmentId() : "General";
		return String.format("Support Staff (%s - %s)", specialization != null ? specialization : "General", deptCode);
	}

	@Override
	public boolean archiveAccount() {
		boolean archived = super.archiveAccount();
		if (archived) {
			this.isAvailable = false;
		}
		return archived;
	}

	@Override
	public boolean unarchiveAccount() {
		boolean unarchived = super.unarchiveAccount();
		if (unarchived) {
			this.isAvailable = true;
		}
		return unarchived;
	}

	@Override
	public void displayInfo() {
		Department dept = getDepartment();
		String statusLabel = isArchived()
				? "Archived (Since " + getDateArchived() + ")"
				: (isAvailable ? "Available" : "Busy / Offline");
		System.out.println("====================================================");
		System.out.println("        AGAPAY | SUPPORT STAFF ACCOUNT INFO");
		System.out.println("====================================================");
		System.out.printf("Staff ID       : %s\n", getAccountId());
		System.out.printf("Name           : %s\n", getName());
		System.out.printf("Email          : %s\n", getEmail());
		System.out.printf("Contact Number : %s\n", getContactNumber());
		System.out.printf("Specialization : %s\n", specialization);
		System.out.printf("Status         : %s\n", statusLabel);
		if (dept != null && !dept.getDepartmentName().isBlank()) {
			System.out.printf("Department     : %s (%s)\n", dept.getDepartmentName(), dept.getDepartmentId());
		}
		System.out.println("====================================================");
	}

	public boolean acceptTicket(TicketList ticketList, String ticketId) {
		Ticket ticket = ticketList.findTicketById(ticketId);
		if (ticket == null) {
			System.out.println("[AGAPAY] Ticket not found.\n");
			return false;
		}
		if (!belongsToStaffDepartment(ticket)) {
			System.out.printf("[AGAPAY] ACCESS DENIED: Ticket %s belongs to a different department.\n\n", ticket.getTicketId());
			return false;
		}
		return ticket.accept(this);
	}

	public boolean resolveTicket(TicketList ticketList, String ticketId, String resolutionNotes) {
		Ticket ticket = ticketList.findTicketById(ticketId);
		if (ticket == null) {
			System.out.println("[AGAPAY] Ticket not found.\n");
			return false;
		}
		if (!belongsToStaffDepartment(ticket)) {
			System.out.printf("[AGAPAY] ACCESS DENIED: Ticket %s belongs to a different department.\n\n", ticket.getTicketId());
			return false;
		}
		return ticket.resolve(this, resolutionNotes);
	}

	public boolean closeTicket(TicketList ticketList, String ticketId) {
		Ticket ticket = ticketList.findTicketById(ticketId);
		if (ticket == null) {
			System.out.println("[AGAPAY] Ticket not found.\n");
			return false;
		}
		if (!belongsToStaffDepartment(ticket)) {
			System.out.printf("[AGAPAY] ACCESS DENIED: Ticket %s belongs to a different department.\n\n", ticket.getTicketId());
			return false;
		}
		return ticket.close();
	}

	public boolean archiveTicket(TicketList ticketList, String ticketId) {
		Ticket ticket = ticketList.findTicketById(ticketId);
		if (ticket == null) {
			System.out.println("[AGAPAY] Ticket not found.\n");
			return false;
		}
		if (!belongsToStaffDepartment(ticket)) {
			System.out.printf("[AGAPAY] ACCESS DENIED: Ticket %s belongs to a different department.\n\n", ticket.getTicketId());
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
		if (!belongsToStaffDepartment(ticket)) {
			System.out.printf("[AGAPAY] ACCESS DENIED: Ticket %s belongs to a different department.\n\n", ticket.getTicketId());
			return false;
		}
		return ticket.unarchive();
	}

	public void updateTicketStatus(String ticketId, String status, TicketList ticketList) {
		Ticket ticket = ticketList.findTicketById(ticketId);
		if (ticket == null) {
			System.out.println("[AGAPAY] Ticket not found.\n");
			return;
		}
		if (!belongsToStaffDepartment(ticket)) {
			System.out.printf("[AGAPAY] ACCESS DENIED: Ticket %s belongs to a different department.\n\n", ticket.getTicketId());
			return;
		}
		if (ticket.isArchived()) {
			System.out.printf("[AGAPAY] Ticket %s is archived and read-only. Unarchive it first to modify.\n\n", ticket.getTicketId());
			return;
		}
		ticket.setStatus(status);
		System.out.printf("[AGAPAY] Ticket %s status updated to '%s'.\n\n", ticketId, status);
	}

	private boolean belongsToStaffDepartment(Ticket ticket) {
		if (getDepartment() == null || ticket.getCategory() == null || ticket.getCategory().getDepartment() == null) {
			return true;
		}
		return getDepartment().getDepartmentId().equalsIgnoreCase(ticket.getCategory().getDepartment().getDepartmentId());
	}

	// Views tickets filtered by this staff member's assigned department
	public void viewTickets(TicketList ticketList) {
		if (getDepartment() != null) {
			ticketList.displayTicketsByDepartment(getDepartment());
		} else {
			ticketList.displayAllTickets();
		}
	}

	public void viewAllCampusTickets(TicketList ticketList) {
		ticketList.displayAllTickets();
	}

	public void viewArchivedTickets(TicketList ticketList) {
		if (getDepartment() != null) {
			ticketList.displayArchivedTicketsByDepartment(getDepartment());
		} else {
			ticketList.displayAllArchivedTickets();
		}
	}

	public void viewAllCampusArchivedTickets(TicketList ticketList) {
		ticketList.displayAllArchivedTickets();
	}

	public void viewAllArchivedCampusTickets(TicketList ticketList) {
		ticketList.displayAllArchivedTickets();
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean available) {
		isAvailable = available;
	}
}
