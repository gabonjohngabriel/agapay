package bsit2e_g1.datastructures;

import bsit2e_g1.components.Comment;
import bsit2e_g1.nodes.CommentNode;

public class CommentList {
	private CommentNode head;
	private int size;

	public CommentList() {
		this.head = null;
		this.size = 0;
	}

	public void addComment(Comment comment) {
		if (comment == null) return;
		CommentNode newNode = new CommentNode(comment);

		if (head == null) {
			head = newNode;
		} else {
			CommentNode current = head;

			while (current.next != null) {
				current = current.next;
			}

			current.next = newNode;
		}

		size++;
	}

	public void displayAllComments() {
		if (head == null) {
			System.out.println("[AGAPAY] No comments yet for this ticket.");
			return;
		}

		CommentNode current = head;

		int count = 1;

		while (current != null) {
			System.out.printf("Comment %d\n", count);
			if (current.comment != null) {
				current.comment.displayComment();
			}

			count++;

			if (current.next != null) {
				System.out.println("----------------------------------------------------");
			}

			current = current.next;
		}
	}

	public int getSize() {
		return size;
	}

	public boolean isEmpty() {
		return head == null;
	}
}
