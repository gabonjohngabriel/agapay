package bsit2e_g1.datastructures;

import bsit2e_g1.components.Notification;
import bsit2e_g1.nodes.NotificationNode;

public class NotificationList {
	private NotificationNode head;
	private int size;

	public NotificationList() {
		this.head = null;
		this.size = 0;
	}

	public void addNotification(Notification notification) {
		if (notification == null) return;
		NotificationNode newNode = new NotificationNode(notification);

		if (head == null) {
			head = newNode;
		} else {
			NotificationNode current = head;

			while (current.next != null) {
				current = current.next;
			}

			current.next = newNode;
		}

		size++;
	}

	public void displayAllNotifications() {
		if (head == null) {
			System.out.println("[AGAPAY] You have no notifications.");
			return;
		}

		System.out.println("====================================================");
		System.out.println("              AGAPAY | MY NOTIFICATIONS");
		System.out.println("====================================================");

		NotificationNode current = head;
		int count = 1;

		while (current != null) {
			Notification n = current.notification;

			if (n != null) {
				String statusBadge = n.isRead() ? "[READ]" : "[NEW]";
				String ticketId = (n.getTicket() != null) ? n.getTicket().getTicketId() : "General";
				System.out.printf("%d. %s [%s] (Ticket: %s)\n", count, statusBadge, n.getDateSent(), ticketId);
				System.out.printf("   Message: %s\n", n.getMessage());
				System.out.println("----------------------------------------------------");
			}

			current = current.next;
			count++;
		}
	}

	public void markAllAsRead() {
		NotificationNode current = head;

		while (current != null) {
			if (current.notification != null) {
				current.notification.markAsRead();
			}

			current = current.next;
		}
	}

	public int getUnreadCount() {
		int unread = 0;

		NotificationNode current = head;

		while (current != null) {
			if (current.notification != null && !current.notification.isRead()) {
				unread++;
			}

			current = current.next;
		}
		return unread;
	}

	public int getSize() {
		return size;
	}
}
