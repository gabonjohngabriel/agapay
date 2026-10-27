package bsit2e_g1.nodes;

import bsit2e_g1.components.Department;

public class DepartmentNode {
	public Department department;
	public DepartmentNode next;

	public DepartmentNode(Department department) {
		this.department = department;
		this.next = null;
	}
}