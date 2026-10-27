package bsit2e_g1.datastructures;

import bsit2e_g1.components.SupportStaff;
import bsit2e_g1.nodes.SupportStaffNode;

public class SupportStaffList {
	private SupportStaffNode head;
	private int size;

	public SupportStaffList() {
		this.head = null;
		this.size = 0;
	}

	public void addStaff(SupportStaff staff) {
		if (staff == null) return;
		SupportStaffNode newNode = new SupportStaffNode(staff);

		if (head == null) {
			head = newNode;
		} else {
			SupportStaffNode current = head;

			while (current.next != null) {
				current = current.next;
			}

			current.next = newNode;
		}

		size++;
	}

	public SupportStaff findStaffById(String staffId) {
		if (staffId == null || staffId.isBlank()) {
			return null;
		}

		SupportStaffNode current = head;

		while (current != null) {
			if (current.staff != null && current.staff.getAccountId().equalsIgnoreCase(staffId.trim())) {
				return current.staff;
			}

			current = current.next;
		}

		return null;
	}

	public void displayStaff() {
		if (head == null) {
			System.out.println("  [No staff members assigned]");
			return;
		}

		SupportStaffNode current = head;

		while (current != null) {
			if (current.staff != null) {
				String status = current.staff.isArchived()
						? "Archived"
						: (current.staff.isAvailable() ? "Available" : "Busy/Offline");
				System.out.printf("  - %s (%s) | %s | Status: %s\n", current.staff.getName(), current.staff.getAccountId(), current.staff.getSpecialization(), status);
			}

			current = current.next;
		}
	}

	public int getSize() {
		return size;
	}

	public boolean isEmpty() {
		return head == null;
	}
}
