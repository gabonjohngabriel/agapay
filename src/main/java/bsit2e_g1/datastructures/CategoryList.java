package bsit2e_g1.datastructures;

import bsit2e_g1.components.Category;
import bsit2e_g1.nodes.CategoryNode;

public class CategoryList {

	private CategoryNode head;
	private int size;

	public CategoryList() {
		this.head = null;
		this.size = 0;
	}

	public void addCategory(Category category) {
		if (category == null) return;
		CategoryNode newNode = new CategoryNode(category);

		if (head == null) {
			head = newNode;
		} else {
			CategoryNode current = head;

			while (current.next != null) {
				current = current.next;
			}

			current.next = newNode;
		}
		size++;
	}

	public Category getByIndex(int index) {
		if (index < 0 || index >= size) {
			return null;
		}

		CategoryNode current = head;

		for (int i = 0; i < index; i++) {
			current = current.next;
		}

		return current.category;
	}

	public Category findCategoryById(String categoryId) {
		if (categoryId == null || categoryId.isBlank()) {
			return null;
		}

		CategoryNode current = head;

		while (current != null) {
			if (current.category != null && current.category.getCategoryId().equalsIgnoreCase(categoryId.trim())) {
				return current.category;
			}

			current = current.next;
		}

		return null;
	}

	public void displayCategories() {
		if (head == null) {
			System.out.println("[AGAPAY] No categories registered.");
			return;
		}

		CategoryNode current = head;
		int index = 1;

		while (current != null) {
			if (current.category != null) {
				System.out.printf("[%d] %s (%s) - %s\n", index, current.category.getCategoryName(), current.category.getCategoryId(), current.category.getCategoryDescription());
			}

			current = current.next;

			index++;
		}
	}

	public int getSize() {
		return size;
	}

	public boolean isEmpty() {
		return head == null;
	}
}
