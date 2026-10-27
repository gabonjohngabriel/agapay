package bsit2e_g1.datastructures;

import bsit2e_g1.components.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Custom Linked List Data Structures Unit Tests")
public class DataStructureTest {

	private Department deptCICT;
	private Category catHardware;
	private User user;
	private SupportStaff staff;

	@BeforeEach
	void setUp() {
		deptCICT = new Department("CICT", "College of ICT", "Pimentel Hall");
		catHardware = new Category("CAT-HW", "Hardware Maintenance", "PC labs", deptCICT);
		user = new User("U-01", "Student A", "a@bulsu.ph", "0911", "Student", deptCICT);
		staff = new SupportStaff("S-01", "Technician B", "b@bulsu.ph", "0922", "Hardware", true, deptCICT);
	}

	@Test
	@DisplayName("TicketList: Add, find, archive, query and filter operations")
	void testTicketList() {
		TicketList list = new TicketList();
		assertTrue(list.isEmpty());

		Ticket t1 = new Ticket("Broken PC", "No power", "2026-10-27 08:00:00", "High", catHardware, user);
		Ticket t2 = new Ticket("Broken Mouse", "Click stuck", "2026-10-27 08:30:00", "Low", catHardware, user);
		list.addTicket(t1);
		list.addTicket(t2);

		assertFalse(list.isEmpty());
		assertEquals(t1, list.findTicketById(t1.getTicketId()));
		assertEquals(t2, list.findTicketById(t2.getTicketId().toLowerCase()));
		assertNull(list.findTicketById("SA-999"));

		// In progress state
		assertFalse(list.hasInProgressTicketsForUser(user));
		t1.accept(staff);
		assertTrue(list.hasInProgressTicketsForUser(user));
		assertTrue(list.hasInProgressTicketsForStaff(staff));

		// Cancel pending
		int cancelled = list.cancelPendingTicketsForUser(user);
		assertEquals(1, cancelled);
		assertEquals("Cancelled", t2.getStatus());

		// Display and filter calls without error
		assertDoesNotThrow(() -> list.filterByStatus("Pending", user));
		assertDoesNotThrow(() -> list.filterByPriority("High", user));
		assertDoesNotThrow(() -> list.generateSummaryReport());
		assertDoesNotThrow(() -> list.generateSummaryReport(deptCICT));
		assertDoesNotThrow(() -> list.displayTickets(user));
		assertDoesNotThrow(() -> list.displayArchivedTickets(user));
		assertDoesNotThrow(() -> list.displayTicketsByDepartment(deptCICT));
		assertDoesNotThrow(() -> list.displayAllTickets());
	}

	@Test
	@DisplayName("AccountList: User & Staff registration, retrieval and account archival safety")
	void testAccountList() {
		AccountList list = new AccountList();
		TicketList ticketList = new TicketList();

		list.addUser(user);
		list.addSupportStaff(staff);

		assertEquals(user, list.findAccountById("U-01"));
		assertEquals(staff, list.findAccountById("S-01"));
		assertEquals(user, list.findUserByEmail("a@bulsu.ph"));
		assertEquals(staff, list.findStaffByEmail("b@bulsu.ph"));

		// Update user
		assertTrue(list.updateUser(user, "Student A Updated", "a.updated@bulsu.ph", "0999"));
		assertEquals("Student A Updated", user.getName());

		// In progress ticket prevents archival
		Ticket activeTicket = new Ticket("Issue", "Desc", "2026-10-27 09:00:00", "High", catHardware, user);
		activeTicket.accept(staff);
		ticketList.addTicket(activeTicket);

		assertFalse(list.archiveUserAccount(user, ticketList));
		assertFalse(list.archiveStaffAccount(staff, ticketList));

		activeTicket.resolve(staff, "Fixed");
		assertTrue(list.archiveUserAccount(user, ticketList));
		assertTrue(list.archiveStaffAccount(staff, ticketList));
	}

	@Test
	@DisplayName("AttachmentList & CommentList: Add, size, and display")
	void testAttachmentAndCommentLists() {
		AttachmentList attList = new AttachmentList();
		assertTrue(attList.isEmpty());
		attList.addAttachment(new Attachment("ATT-01", "doc.pdf"));
		assertEquals(1, attList.getSize());
		assertFalse(attList.isEmpty());
		assertDoesNotThrow(attList::displayAllAttachments);

		CommentList cmtList = new CommentList();
		assertTrue(cmtList.isEmpty());
		cmtList.addComment(new Comment("2026-10-27", "Note", user));
		assertEquals(1, cmtList.getSize());
		assertFalse(cmtList.isEmpty());
		assertDoesNotThrow(cmtList::displayAllComments);
	}

	@Test
	@DisplayName("CategoryList & DepartmentList: Indexed lookup and search")
	void testCategoryAndDepartmentLists() {
		CategoryList catList = new CategoryList();
		catList.addCategory(catHardware);
		assertEquals(1, catList.getSize());
		assertEquals(catHardware, catList.getByIndex(0));
		assertEquals(catHardware, catList.findCategoryById("CAT-HW"));
		assertNull(catList.getByIndex(99));
		assertDoesNotThrow(catList::displayCategories);

		DepartmentList deptList = new DepartmentList();
		deptList.addDepartment(deptCICT);
		assertEquals(deptCICT, deptList.findDepartmentById("CICT"));
		assertNull(deptList.findDepartmentById("CAL"));
		assertDoesNotThrow(deptList::displayAllDepartments);
	}

	@Test
	@DisplayName("NotificationList & SupportStaffList: Unread counts and staff listings")
	void testNotificationAndSupportStaffLists() {
		NotificationList notifList = new NotificationList();
		notifList.addNotification(new Notification("N-1", "Msg", null, user));
		assertEquals(1, notifList.getSize());
		assertEquals(1, notifList.getUnreadCount());
		notifList.markAllAsRead();
		assertEquals(0, notifList.getUnreadCount());
		assertDoesNotThrow(notifList::displayAllNotifications);

		SupportStaffList staffList = new SupportStaffList();
		staffList.addStaff(staff);
		assertEquals(1, staffList.getSize());
		assertEquals(staff, staffList.findStaffById("S-01"));
		assertDoesNotThrow(staffList::displayStaff);
	}
}
