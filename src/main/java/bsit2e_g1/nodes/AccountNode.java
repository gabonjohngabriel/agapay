package bsit2e_g1.nodes;

import bsit2e_g1.components.Account;

public class AccountNode {
	public Account account;
	public AccountNode next;

	public AccountNode(Account account) {
		this.account = account;
		this.next = null;
	}
}
