package bsit2e_g1.ui.staff;

import bsit2e_g1.components.SupportStaff;
import bsit2e_g1.utils.InputHelper;

public class StaffNotificationMenu {

	public static void notifications(SupportStaff staff) {
		staff.viewNotifications();
		System.out.println("[1] Mark All as Read");
		System.out.println("[0] Back\n");

		int choice = InputHelper.readInt("Choice: ", 0, 1);
		System.out.println();

		if (choice == 1) {
			staff.markNotificationsAsRead();
			System.out.println("[AGAPAY] All notifications marked as read.\n");
		}
	}
}
