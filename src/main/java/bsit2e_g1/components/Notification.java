package bsit2e_g1.components;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notification {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private String notificationId;
	private String message;
	private String dateSent;
	private boolean isRead;

	// Association
	// 1 Ticket — 0..* Notification
	private Ticket ticket;

	// Association
	// 1 Account — 0..* Notification (Recipient)
	private Account recipient;

	public Notification() {
	}

	public Notification(String notificationId, String message, Ticket ticket, Account recipient) {
		this.notificationId = notificationId;
		this.message = message;
		this.dateSent = LocalDateTime.now().format(FORMATTER);
		this.isRead = false;
		this.ticket = ticket;
		this.recipient = recipient;
	}

	public void sendNotification() {
		if (recipient != null) {
			recipient.receiveNotification(this);
		}
	}

	public void markAsRead() {
		this.isRead = true;
	}

	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDateSent() {
		return dateSent;
	}

	public void setDateSent(String dateSent) {
		this.dateSent = dateSent;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean read) {
		isRead = read;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public Account getRecipient() {
		return recipient;
	}

	public void setRecipient(Account recipient) {
		this.recipient = recipient;
	}
}