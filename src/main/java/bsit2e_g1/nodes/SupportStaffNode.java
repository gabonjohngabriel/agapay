package bsit2e_g1.nodes;

import bsit2e_g1.components.SupportStaff;

public class SupportStaffNode {
	public SupportStaff staff;
	public SupportStaffNode next;

	public SupportStaffNode(SupportStaff staff) {
		this.staff = staff;
		this.next = null;
	}
}