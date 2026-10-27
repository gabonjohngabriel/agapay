package bsit2e_g1.datastructures;

import bsit2e_g1.components.Account;
import bsit2e_g1.components.SupportStaff;
import bsit2e_g1.components.User;
import bsit2e_g1.nodes.AccountNode;

public class AccountList {
	private AccountNode head;

	public AccountList() {
		this.head = null;
	}

	public AccountList(SupportStaff[] initialSupportStaff) {
		if (initialSupportStaff != null) {
			for (int i = 0; i < initialSupportStaff.length; i++) {
				this.addSupportStaff(initialSupportStaff[i]);
			}
		}
	}

	public void addUser(User user) {
		if (user == null) return;
		AccountNode newUser = new AccountNode(user);

		if (head == null) {
			head = newUser;
		} else {
			AccountNode current = head;
			while (current.next != null) {
				current = current.next;
			}
			current.next = newUser;
		}
	}

	public void addSupportStaff(SupportStaff staff) {
		if (staff == null) return;
		AccountNode newUser = new AccountNode(staff);

		if (head == null) {
			head = newUser;
		} else {
			AccountNode current = head;
			while (current.next != null) {
				current = current.next;
			}
			current.next = newUser;
		}
	}

	public Account findAccountById(String accountId) {
		if (accountId == null || accountId.isBlank()) return null;
		AccountNode current = head;

		while (current != null) {
			if (current.account != null && current.account.getAccountId() != null
					&& current.account.getAccountId().equalsIgnoreCase(accountId.trim())) {
				return current.account;
			}
			current = current.next;
		}
		return null;
	}

	public User findUserByEmail(String email) {
		if (email == null || email.isBlank()) return null;
		AccountNode current = head;

		while (current != null) {
			if (current.account instanceof User currentUser && currentUser.getEmail() != null && currentUser.getEmail().equalsIgnoreCase(email.trim())) {
				return currentUser;
			}
			current = current.next;
		}
		return null;
	}

	public SupportStaff findStaffByEmail(String email) {
		if (email == null || email.isBlank()) return null;
		AccountNode current = head;

		while (current != null) {
			if (current.account instanceof SupportStaff supportStaff && supportStaff.getEmail() != null && supportStaff.getEmail().equalsIgnoreCase(email.trim())) {
				return supportStaff;
			}
			current = current.next;
		}
		return null;
	}

	public boolean updateUser(User currentUser, String name, String email, String contactNumber) {
		if (currentUser == null) {
			System.out.println("[AGAPAY] User does not exist.\n");
			return false;
		} else {
			currentUser.updateProfile(name, email, contactNumber);
			return true;
		}
	}

	public boolean archiveUserAccount(User user, TicketList ticketList) {
		if (user == null) {
			System.out.println("[AGAPAY] User does not exist.\n");
			return false;
		}

		if (ticketList != null && ticketList.hasInProgressTicketsForUser(user)) {
			System.out.println("[AGAPAY] Cannot archive account while you have tickets currently 'In Progress'. Please wait for staff to resolve them first.\n");
			return false;
		}

		if (ticketList != null) {
			int cancelled = ticketList.cancelPendingTicketsForUser(user);
			if (cancelled > 0) {
				System.out.printf("[AGAPAY] %d pending ticket(s) were automatically cancelled.\n", cancelled);
			}
		}

		return user.archiveAccount();
	}

	public boolean archiveStaffAccount(SupportStaff staff, TicketList ticketList) {
		if (staff == null) {
			System.out.println("[AGAPAY] Support Staff account does not exist.\n");
			return false;
		}

		if (ticketList != null && ticketList.hasInProgressTicketsForStaff(staff)) {
			System.out.println("[AGAPAY] Cannot archive staff account while you have assigned tickets 'In Progress'. Please resolve them first.\n");
			return false;
		}

		return staff.archiveAccount();
	}
}
