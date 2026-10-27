package bsit2e_g1.ui;

import bsit2e_g1.datastructures.AccountList;
import bsit2e_g1.datastructures.DepartmentList;
import bsit2e_g1.datastructures.TicketList;

public interface Operations {
	void run(AccountList accountList, TicketList ticketList, DepartmentList departmentList); // public, abstract by default
}
