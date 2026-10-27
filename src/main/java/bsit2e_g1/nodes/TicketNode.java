package bsit2e_g1.nodes;

import bsit2e_g1.components.Ticket;

public class TicketNode {
	public Ticket ticket;
	public TicketNode next;

	public TicketNode(Ticket ticket) {
		this.ticket = ticket;
		this.next = null;
	}
}

