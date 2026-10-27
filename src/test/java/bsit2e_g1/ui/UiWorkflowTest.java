package bsit2e_g1.ui;

import bsit2e_g1.components.*;
import bsit2e_g1.datastructures.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UI End-to-End Workflow Integration Tests")
public class UiWorkflowTest {

	@Test
	@DisplayName("Workflow: User creates ticket, staff accepts, resolves, user closes and archives")
	void testCompleteHelpdeskWorkflow() {
		Department dept = new Department("CICT", "College of Information and Communications Technology", "Pimentel Hall");
		Category cat = new Category("CAT-NET", "Campus Wi-Fi", "Wi-Fi Connectivity Issues", dept);
		dept.addCategory(cat);

		DepartmentList deptList = new DepartmentList();
		deptList.addDepartment(dept);

		SupportStaff staff = new SupportStaff("STF-99", "Support Specialist", "specialist@bulsu.ph", "09111", "Network", true, dept);
		dept.addSupportStaff(staff);

		AccountList accountList = new AccountList();
		accountList.addSupportStaff(staff);
		TicketList ticketList = new TicketList();

		User student = new User("USR-88", "Student Juan", "juan@bulsu.ph", "09222", "Student", dept);
		accountList.addUser(student);

		// 1. Student creates ticket
		student.createTicket(ticketList, "Cannot connect in Room 204", "No IP address assigned", "High", cat);
		Ticket ticket = student.getMyTickets().getHead().ticket;
		assertNotNull(ticket);
		assertEquals("Pending", ticket.getStatus());
		assertEquals(1, student.getNotifications().getSize());

		// 2. Staff accepts ticket
		assertTrue(staff.acceptTicket(ticketList, ticket.getTicketId()));
		assertEquals("In Progress", ticket.getStatus());
		assertEquals(staff, ticket.getAssignedStaff());

		// 3. Staff adds work comment
		ticket.addComment(new Comment("2026-10-27 10:30:00", "Access Point reset in progress", staff));
		assertEquals(1, ticket.getComments().getSize());

		// 4. Staff resolves ticket
		assertTrue(staff.resolveTicket(ticketList, ticket.getTicketId(), "Reconfigured DHCP lease"));
		assertEquals("Resolved", ticket.getStatus());

		// 5. User verifies and closes ticket
		assertTrue(ticket.close(student));
		assertEquals("Closed", ticket.getStatus());

		// 6. User archives ticket
		assertTrue(student.archiveTicket(ticketList, ticket.getTicketId()));
		assertTrue(ticket.isArchived());

		// 7. User unarchives ticket
		assertTrue(student.unarchiveTicket(ticketList, ticket.getTicketId()));
		assertFalse(ticket.isArchived());
	}
}
