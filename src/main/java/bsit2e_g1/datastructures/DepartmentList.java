package bsit2e_g1.datastructures;

import bsit2e_g1.components.Department;
import bsit2e_g1.nodes.DepartmentNode;

public class DepartmentList {
	private DepartmentNode head;

	public DepartmentList() {
		this.head = null;
	}

	public void addDepartment(Department department) {
		if (department == null) return;
		DepartmentNode newNode = new DepartmentNode(department);

		if (head == null) {
			head = newNode;
		} else {
			DepartmentNode current = head;
			while (current.next != null) {
				current = current.next;
			}
			current.next = newNode;
		}
	}

	public Department findDepartmentById(String departmentId) {
		if (departmentId == null || departmentId.isBlank()) {
			return null;
		}

		DepartmentNode current = head;

		while (current != null) {
			if (current.department != null && current.department.getDepartmentId().equalsIgnoreCase(departmentId.trim())) {
				return current.department;
			}
			current = current.next;
		}

		return null;
	}

	public void displayAllDepartments() {
		if (head == null) {
			System.out.println("[AGAPAY] No departments registered.\n");
			return;
		}

		DepartmentNode current = head;

		while (current != null) {
			if (current.department != null) {
				current.department.displayDepartmentInfo();
			}
			current = current.next;
		}
	}
}
