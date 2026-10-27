package bsit2e_g1.nodes;

import bsit2e_g1.components.Notification;

public class NotificationNode {
	public Notification notification;
	public NotificationNode next;

	public NotificationNode(Notification notification) {
		this.notification = notification;
		this.next = null;
	}
}