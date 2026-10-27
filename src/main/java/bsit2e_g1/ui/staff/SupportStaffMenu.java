package bsit2e_g1.ui.staff;

import bsit2e_g1.components.Department;
import bsit2e_g1.components.SupportStaff;
import bsit2e_g1.datastructures.AccountList;
import bsit2e_g1.datastructures.DepartmentList;
import bsit2e_g1.datastructures.TicketList;
import bsit2e_g1.ui.Operations;
import bsit2e_g1.utils.InputHelper;

public class SupportStaffMenu implements Operations {

	private boolean filterByOwnDepartmentOnly = true;

	@Override
	public void run(AccountList accountList, TicketList ticketList, DepartmentList departmentList) {
		while (true) {
			System.out.println("====================================================");
			System.out.println("     AGAPAY | CAMPUS HELP DESK TICKETING SYSTEM");
			System.out.println("====================================================");
			System.out.println("[1] Log In");
			System.out.println("[0] Back to Main Menu");
			System.out.println("====================================================\n");

			int choice = InputHelper.readInt("Choice: ", 0, 1);
			System.out.println();

			if (choice == 0) {
				break;
			} else if (choice == 1) {
				StaffAuthMenu.login(accountList, ticketList, departmentList, staff -> {
					supportMenu(staff, accountList, ticketList, departmentList, true);
				});
			}
		}
	}

	// Support Staff Dashboard Menu
	private void supportMenu(SupportStaff supportStaff, AccountList accountList, TicketList ticketList, DepartmentList departmentList, boolean isAvailable) {
		while (true) {
			supportStaff.setAvailable(isAvailable);
			int unread = supportStaff.getNotifications().getUnreadCount();
			String notifLabel = unread > 0 ? String.format("[8] View Notifications (%d NEW)", unread) : "[8] View Notifications";
			String deptCode = (supportStaff.getDepartment() != null) ? supportStaff.getDepartment().getDepartmentId() : "ALL";
			String filterPrefLabel = filterByOwnDepartmentOnly
					? String.format("%s", deptCode)
					: "All Colleges";

			System.out.println("====================================================");
			System.out.println("     AGAPAY | CAMPUS HELP DESK TICKETING SYSTEM");
			System.out.println("====================================================");
			System.out.printf("Hello, %s!\n", supportStaff.getName());
			System.out.printf("ID: %s\n", supportStaff.getAccountId());
			System.out.printf("Active Filter Preference: [%s]\n", filterPrefLabel);
			System.out.println("-----------------------------------------------------");
			System.out.println("[1] Accept Ticket");
			System.out.println("[2] Resolve Ticket");
			System.out.println("[3] Close Resolved Ticket");
			System.out.println("[4] View Active Tickets (" + filterPrefLabel + ")");
			System.out.println("[5] View Ticket Details");
			System.out.println("[6] Search Tickets");
			System.out.println("[7] Generate Summary Report");
			System.out.println(notifLabel);
			System.out.println("[9] View Campus Departments");
			System.out.println("[10] Change Department Filtering Preference");
			System.out.println("[11] Archive/Unarchive Ticket");
			System.out.println("[12] View Archived Tickets (" + filterPrefLabel + ")");
			System.out.println("[13] Account Settings");
			System.out.println("[0] Log Out");
			System.out.println("====================================================");

			int choice = InputHelper.readInt("Choice: ", 0, 13);
			System.out.println();

			Department activeDeptFilter = filterByOwnDepartmentOnly ? supportStaff.getDepartment() : null;

			if (choice == 0) {
				System.out.println("[AGAPAY] Logging out...\n");
				supportStaff.setAvailable(false);
				break;
			} else if (choice == 1) {
				StaffTicketMenu.acceptTicket(supportStaff, ticketList);
			} else if (choice == 2) {
				StaffTicketMenu.resolveTicket(supportStaff, ticketList);
			} else if (choice == 3) {
				StaffTicketMenu.closeTicketUI(supportStaff, ticketList);
			} else if (choice == 4) {
				StaffTicketMenu.viewActiveTickets(supportStaff, ticketList, filterByOwnDepartmentOnly);
			} else if (choice == 5) {
				StaffTicketMenu.ticketDetails(supportStaff, ticketList);
			} else if (choice == 6) {
				StaffTicketMenu.searchFilter(ticketList, activeDeptFilter);
			} else if (choice == 7) {
				ticketList.generateSummaryReport(activeDeptFilter);
			} else if (choice == 8) {
				StaffNotificationMenu.notifications(supportStaff);
			} else if (choice == 9) {
				departmentList.displayAllDepartments();
			} else if (choice == 10) {
				toggleDepartmentPreference(supportStaff);
			} else if (choice == 11) {
				StaffArchiveMenu.manageArchivedTickets(supportStaff, ticketList, filterByOwnDepartmentOnly);
			} else if (choice == 12) {
				StaffArchiveMenu.viewArchivedTickets(supportStaff, ticketList, filterByOwnDepartmentOnly);
			} else if (choice == 13) {
				if (StaffAccountMenu.manageStaffAccount(supportStaff, accountList, ticketList)) {
					break;
				}
			}
		}
	}

	private void toggleDepartmentPreference(SupportStaff supportStaff) {
		String deptName = (supportStaff.getDepartment() != null)
				? supportStaff.getDepartment().getDepartmentName() + " (" + supportStaff.getDepartment().getDepartmentId() + ")"
				: "Assigned Department";

		String selected = InputHelper.choice("Select Ticket Viewing:", new String[]{
				"Department - " + deptName,
				"All Colleges (CICT, CON, CAL)"
		});
		
		System.out.println();

		filterByOwnDepartmentOnly = selected.startsWith("Department");

		System.out.printf("[AGAPAY] Filtering preference updated to: %s\n\n", selected);
	}
}
