package bsit2e_g1.components;

public class Category implements Displayable {
	private String categoryId;
	private String categoryName;
	private String categoryDescription;

	// Aggregation: 1 Department ◇— 1..* Category
	private Department department;

	public Category() {
	}

	public Category(String categoryId, String categoryName, String categoryDescription) {
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.categoryDescription = categoryDescription;
	}

	public Category(String categoryId, String categoryName, String categoryDescription, Department department) {
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.categoryDescription = categoryDescription;
		this.department = department;
	}

	public void updateCategory(String categoryName, String categoryDescription) {
		if (categoryName != null && !categoryName.isBlank()) {
			this.categoryName = categoryName.trim();
		}
		if (categoryDescription != null && !categoryDescription.isBlank()) {
			this.categoryDescription = categoryDescription.trim();
		}
	}

	@Override
	public void displayDetails() {
		displayCategoryInfo();
	}

	public void displayCategoryInfo() {
		System.out.printf("[%s] %s - %s (Dept: %s)\n",
				categoryId != null ? categoryId : "N/A",
				categoryName != null ? categoryName : "N/A",
				categoryDescription != null ? categoryDescription : "N/A",
				department != null ? department.getDepartmentId() : "Unassigned");
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
}
