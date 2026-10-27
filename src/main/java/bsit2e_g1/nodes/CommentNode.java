package bsit2e_g1.nodes;

import bsit2e_g1.components.Comment;

public class CommentNode {
	public Comment comment;
	public CommentNode next;

	public CommentNode(Comment comment) {
		this.comment = comment;
		this.next = null;
	}
}
