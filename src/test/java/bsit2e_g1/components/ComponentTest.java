package bsit2e_g1.components;

import bsit2e_g1.datastructures.TicketList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Domain Components Unit Tests")
public class ComponentTest {

	private Department deptCICT;
	private Category catWifi;
	private User student;
	private SupportStaff staff;

	@BeforeEach
	void setUp() {
		deptCICT = new Department("CICT", "College of Information and Communications Technology", "Pimentel Hall");
		catWifi = new Category("CAT-01", "Wi-Fi Connectivity", "Campus LAN/Wi-Fi Access", deptCICT);
		deptCICT.addCategory(catWifi);

		student = new User("USR-001", "Juan Dela Cruz", "juan@bulsu.ph", "09123456789", "Student", deptCICT);
		staff = new SupportStaff("STF-001", "Maria Santos", "maria@bulsu.ph", "09987654321", "Network Admin", true, deptCICT);
		deptCICT.addSupportStaff(staff);
	}

	@Test
	@DisplayName("Department: Verify properties, updates and member bindings")
	void testDepartment() {
		assertEquals("CICT", deptCICT.getDepartmentId());
		assertEquals("College of Information and Communications Technology", deptCICT.getDepartmentName());
		assertEquals("Pimentel Hall", deptCICT.getOfficeLocation());
		assertEquals(1, deptCICT.getCategories().getSize());
		assertEquals(1, deptCICT.getSupportStaffList().getSize());

		deptCICT.updateDepartment("Updated Dept Name", "New Building 3F");
		assertEquals("Updated Dept Name", deptCICT.getDepartmentName());
		assertEquals("New Building 3F", deptCICT.getOfficeLocation());

		assertDoesNotThrow(() -> deptCICT.displayDepartmentInfo());
		assertDoesNotThrow(() -> deptCICT.displayDetails());
	}

	@Test
	@DisplayName("Category: Verify properties, updates and display")
	void testCategory() {
		assertEquals("CAT-01", catWifi.getCategoryId());
		assertEquals("Wi-Fi Connectivity", catWifi.getCategoryName());
		assertEquals(deptCICT, catWifi.getDepartment());

		catWifi.updateCategory("Campus Wi-Fi", "Updated desc");
		assertEquals("Campus Wi-Fi", catWifi.getCategoryName());
		assertEquals("Updated desc", catWifi.getCategoryDescription());

		assertDoesNotThrow(() -> catWifi.displayCategoryInfo());
		assertDoesNotThrow(() -> catWifi.displayDetails());
	}

	@Test
	@DisplayName("Account & User: Verify profile, role title, archiving and unarchiving")
	void testUser() {
		assertEquals("USR-001", student.getAccountId());
		assertEquals("Student - CICT", student.getRoleTitle());
		assertFalse(student.isArchived());

		student.updateProfile("Juan D. Cruz", "juandcruz@bulsu.ph", "09112223333");
		assertEquals("Juan D. Cruz", student.getName());
		assertEquals("juandcruz@bulsu.ph", student.getEmail());
		assertEquals("09112223333", student.getContactNumber());

		assertTrue(student.archiveAccount());
		assertTrue(student.isArchived());
		assertFalse(student.archiveAccount(), "Duplicate archive should fail");

		assertTrue(student.unarchiveAccount());
		assertFalse(student.isArchived());
		assertFalse(student.unarchiveAccount(), "Duplicate unarchive should fail");

		assertDoesNotThrow(() -> student.displayInfo());
		assertDoesNotThrow(() -> student.displayDetails());
	}

	@Test
	@DisplayName("SupportStaff: Verify specialization, availability and account archival")
	void testSupportStaff() {
		assertEquals("STF-001", staff.getAccountId());
		assertEquals("Support Staff (Network Admin - CICT)", staff.getRoleTitle());
		assertTrue(staff.isAvailable());

		staff.setSpecialization("Cloud Infrastructure");
		assertEquals("Cloud Infrastructure", staff.getSpecialization());

		// Archiving should toggle available to false
		assertTrue(staff.archiveAccount());
		assertTrue(staff.isArchived());
		assertFalse(staff.isAvailable());

		// Unarchiving should restore availability
		assertTrue(staff.unarchiveAccount());
		assertFalse(staff.isArchived());
		assertTrue(staff.isAvailable());

		assertDoesNotThrow(() -> staff.displayInfo());
		assertDoesNotThrow(() -> staff.displayDetails());
	}

	@Test
	@DisplayName("Ticket: Comprehensive lifecycle and state machine verification")
	void testTicketStateMachine() {
		Ticket ticket = new Ticket("Wi-Fi down", "Cannot reach portal", "2026-10-27 10:00:00", "High", catWifi, student);
		assertNotNull(ticket.getTicketId());
		assertEquals("Pending", ticket.getStatus());
		assertFalse(ticket.isArchived());

		ticket.submit();
		assertEquals(1, student.getNotifications().getSize());

		// Accept ticket
		assertTrue(ticket.accept(staff));
		assertEquals("In Progress", ticket.getStatus());
		assertEquals(staff, ticket.getAssignedStaff());

		// Cannot accept while in progress
		assertFalse(ticket.accept(staff));

		// Cannot resolve with blank notes
		assertFalse(ticket.resolve(staff, "   "));

		// Resolve ticket
		assertTrue(ticket.resolve(staff, "Rebooted access point"));
		assertEquals("Resolved", ticket.getStatus());
		assertEquals("Rebooted access point", ticket.getResolutionNotes());

		// Close ticket
		assertTrue(ticket.close(student));
		assertEquals("Closed", ticket.getStatus());

		// Reopen ticket
		assertTrue(ticket.reopen("Issue recurred"));
		assertEquals("In Progress", ticket.getStatus());

		// Resolve and close again
		assertTrue(ticket.resolve(staff, "Replaced cable"));
		assertTrue(ticket.close());
		assertEquals("Closed", ticket.getStatus());

		// Archive ticket
		assertTrue(ticket.canBeArchived());
		assertTrue(ticket.archive());
		assertTrue(ticket.isArchived());

		// Modifications blocked on archived ticket
		assertFalse(ticket.accept(staff));
		assertFalse(ticket.resolve(staff, "Notes"));
		assertFalse(ticket.close());
		assertFalse(ticket.reopen("Notes"));
		assertFalse(ticket.cancel(student));

		// Unarchive
		assertTrue(ticket.unarchive());
		assertFalse(ticket.isArchived());

		assertDoesNotThrow(() -> ticket.displaySummary());
		assertDoesNotThrow(() -> ticket.displayDetails());
	}

	@Test
	@DisplayName("Ticket: Cancellation rules and authorization")
	void testTicketCancellation() {
		Ticket ticket = new Ticket("Mistake Issue", "Cancel this", "2026-10-27 10:00:00", "Low", catWifi, student);
		assertTrue(ticket.canBeCancelled());
		assertTrue(ticket.cancel(student));
		assertEquals("Cancelled", ticket.getStatus());

		// Cannot cancel again
		assertFalse(ticket.cancel(student));
	}

	@Test
	@DisplayName("Comment: Creation, editing and string representations")
	void testComment() {
		Comment comment = new Comment("2026-10-27 10:00:00", "Investigation started", staff);
		assertNotNull(comment.getCommentId());
		assertEquals("Investigation started", comment.getContent());
		assertEquals(staff, comment.getAuthor());

		comment.editComment("Investigation completed");
		assertEquals("Investigation completed", comment.getContent());

		// Reject empty edit
		comment.editComment("");
		assertEquals("Investigation completed", comment.getContent());

		assertDoesNotThrow(() -> comment.displayComment());
		assertDoesNotThrow(() -> comment.displayDetails());
	}

	@Test
	@DisplayName("Attachment: Extension parsing and file detachment")
	void testAttachment() {
		Attachment att = new Attachment("ATT-01", "trace.log");
		assertEquals("trace.log", att.getFileName());
		assertEquals("LOG File", att.getFileType());
		assertTrue(att.getFileInfo().contains("ATT-01"));

		att.removeFile();
		assertNull(att.getFileName());
		assertTrue(att.getFileInfo().contains("[Empty / Detached]"));
	}

	@Test
	@DisplayName("Notification: Dispatch, mark as read and inbox tracking")
	void testNotification() {
		Notification notif = new Notification("NOTIF-1", "Notice", null, student);
		assertFalse(notif.isRead());

		notif.sendNotification();
		assertEquals(1, student.getNotifications().getSize());
		assertEquals(1, student.getNotifications().getUnreadCount());

		notif.markAsRead();
		assertTrue(notif.isRead());
	}
}
