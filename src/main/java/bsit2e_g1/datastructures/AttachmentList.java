package bsit2e_g1.datastructures;

import bsit2e_g1.components.Attachment;
import bsit2e_g1.nodes.AttachmentNode;

public class AttachmentList {
	private AttachmentNode head;
	private int size;

	public AttachmentList() {
		this.head = null;
		this.size = 0;
	}

	public void addAttachment(Attachment attachment) {
		if (attachment == null) return;
		AttachmentNode newNode = new AttachmentNode(attachment);

		if (head == null) {
			head = newNode;
		} else {
			AttachmentNode current = head;

			while (current.next != null) {
				current = current.next;
			}

			current.next = newNode;
		}

		size++;
	}

	public void displayAllAttachments() {
		if (head == null) {
			System.out.println("[AGAPAY] No attachments uploaded for this ticket.");
			return;
		}

		AttachmentNode current = head;
		int count = 1;

		while (current != null) {
			System.out.printf("Attachment %d: %s\n", count, current.attachment.getFileInfo());

			current = current.next;

			count++;
		}
	}

	public int getSize() {
		return size;
	}

	public boolean isEmpty() {
		return head == null;
	}
}
