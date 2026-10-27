package bsit2e_g1.nodes;

import bsit2e_g1.components.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Node Classes Unit Tests")
public class NodeTest {

	@Test
	@DisplayName("Nodes: Verify all node constructors and payload bindings")
	void testAllNodes() {
		Department dept = new Department("CICT", "CICT", "Office");
		Category cat = new Category("CAT-1", "Cat", "Desc");
		User user = new User("U-1", "User", "u@bulsu.ph", "091", "Student", dept);
		SupportStaff staff = new SupportStaff("S-1", "Staff", "s@bulsu.ph", "092", "Role", true, dept);
		Ticket ticket = new Ticket("T-1", "Desc", "2026-10-27", "Low", cat, user);
		Comment comment = new Comment("2026-10-27", "Text", user);
		Attachment attachment = new Attachment("ATT-1", "file.png");
		Notification notification = new Notification("N-1", "Msg", ticket, user);

		AccountNode an = new AccountNode(user);
		assertEquals(user, an.account);
		assertNull(an.next);

		AttachmentNode atn = new AttachmentNode(attachment);
		assertEquals(attachment, atn.attachment);

		CategoryNode cn = new CategoryNode(cat);
		assertEquals(cat, cn.category);

		CommentNode cmn = new CommentNode(comment);
		assertEquals(comment, cmn.comment);

		DepartmentNode dn = new DepartmentNode(dept);
		assertEquals(dept, dn.department);

		NotificationNode nn = new NotificationNode(notification);
		assertEquals(notification, nn.notification);

		SupportStaffNode ssn = new SupportStaffNode(staff);
		assertEquals(staff, ssn.staff);

		TicketNode tn = new TicketNode(ticket);
		assertEquals(ticket, tn.ticket);
	}
}
