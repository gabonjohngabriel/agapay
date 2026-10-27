package bsit2e_g1.nodes;

import bsit2e_g1.components.Attachment;

public class AttachmentNode {
	public Attachment attachment;
	public AttachmentNode next;

	public AttachmentNode(Attachment attachment) {
		this.attachment = attachment;
		this.next = null;
	}
}
