package bsit2e_g1.ui.user;

import bsit2e_g1.components.User;
import bsit2e_g1.utils.InputHelper;

public class UserNotificationMenu {

	// Notification
	public static void notifications(User user) {
		user.viewNotifications();

		System.out.println("[1] Mark All as Read");
		System.out.println("[0] Back\n");

		int choice = InputHelper.readInt("Choice: ", 0, 1);
		System.out.println();

		if (choice == 1) {
			user.markNotificationsAsRead();
			System.out.println("[AGAPAY] All notifications marked as read.\n");
		}
	}
}
