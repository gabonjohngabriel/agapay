package bsit2e_g1.nodes;

import bsit2e_g1.components.Category;

public class CategoryNode {
	public Category category;
	public CategoryNode next;

	public CategoryNode(Category category) {
		this.category = category;
		this.next = null;
	}
}
