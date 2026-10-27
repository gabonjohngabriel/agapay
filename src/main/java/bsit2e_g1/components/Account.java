package bsit2e_g1.components;

import bsit2e_g1.datastructures.NotificationList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Account implements Displayable {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private String accountId;
	private String name;
	private String email;
	private String contactNumber;
	private Department department;
	private NotificationList notifications;
	private boolean isArchived = false;
	private String dateArchived = "N/A";

	public Account() {
		this.notifications = new NotificationList();
	}

	public Account(String accountId, String name, String email, String contactNumber) {
		this();
		this.accountId = accountId;
		this.name = name;
		this.email = email;
		this.contactNumber = contactNumber;
	}

	public Account(String accountId, String name, String email, String contactNumber, Department department) {
		this(accountId, name, email, contactNumber);
		this.department = department;
	}

	public void updateProfile(String name, String email, String contactNumber) {
		if (name != null && !name.isBlank()) {
			setName(name.trim());
		}
		if (email != null && !email.isBlank()) {
			setEmail(email.trim());
		}
		if (contactNumber != null && !contactNumber.isBlank()) {
			setContactNumber(contactNumber.trim());
		}
	}

	public boolean archiveAccount() {
		if (this.isArchived) {
			System.out.printf("[AGAPAY] Account %s is already deactivated.\n\n", accountId);
			return false;
		}
		this.isArchived = true;
		this.dateArchived = LocalDateTime.now().format(FORMATTER);
		return true;
	}

	public boolean unarchiveAccount() {
		if (!this.isArchived) {
			System.out.printf("[AGAPAY] Account %s is already active.\n\n", accountId);
			return false;
		}
		this.isArchived = false;
		this.dateArchived = "N/A";
		return true;
	}

	public void receiveNotification(Notification notification) {
		if (notification != null && this.notifications != null) {
			this.notifications.addNotification(notification);
		}
	}

	public void viewNotifications() {
		if (this.notifications != null) {
			this.notifications.displayAllNotifications();
		}
	}

	public void markNotificationsAsRead() {
		if (this.notifications != null) {
			this.notifications.markAllAsRead();
		}
	}

	public abstract void displayInfo();

	public abstract String getRoleTitle();

	@Override
	public void displayDetails() {
		displayInfo();
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name != null && !name.isBlank()) {
			this.name = name.trim();
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email != null && !email.isBlank()) {
			this.email = email.toLowerCase().trim();
		}
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		if (contactNumber != null && !contactNumber.isBlank()) {
			this.contactNumber = contactNumber.trim();
		}
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public NotificationList getNotifications() {
		return notifications;
	}

	public boolean isArchived() {
		return isArchived;
	}

	public void setArchived(boolean isArchived) {
		this.isArchived = isArchived;
	}

	public String getDateArchived() {
		return dateArchived;
	}

	public void setDateArchived(String dateArchived) {
		this.dateArchived = dateArchived;
	}
}
