package bsit2e_g1.components;

import bsit2e_g1.datastructures.CategoryList;
import bsit2e_g1.datastructures.SupportStaffList;

public class Department implements Displayable {
	private final SupportStaffList supportStaffList;
	private final CategoryList categories;
	private String departmentId;
	private String departmentName;
	private String officeLocation;

	public Department() {
		this.supportStaffList = new SupportStaffList();
		this.categories = new CategoryList();
	}

	public Department(String departmentId, String departmentName, String officeLocation) {
		this();
		this.departmentId = departmentId;
		this.departmentName = departmentName;
		this.officeLocation = officeLocation;
	}

	public void updateDepartment(String departmentName, String officeLocation) {
		if (departmentName != null && !departmentName.isBlank()) {
			this.departmentName = departmentName;
		}
		if (officeLocation != null && !officeLocation.isBlank()) {
			this.officeLocation = officeLocation;
		}
	}

	public void addSupportStaff(SupportStaff staff) {
		if (staff != null) {
			this.supportStaffList.addStaff(staff);
			staff.setDepartment(this);
		}
	}

	public void addCategory(Category category) {
		if (category != null) {
			this.categories.addCategory(category);
			category.setDepartment(this);
		}
	}

	@Override
	public void displayDetails() {
		displayDepartmentInfo();
	}

	public void displayDepartmentInfo() {
		System.out.println("=================================================");
		System.out.println("            AGAPAY | DEPARTMENT INFO");
		System.out.println("=================================================");
		System.out.printf("Department ID    : %s\n", departmentId != null ? departmentId : "N/A");
		System.out.printf("Department Name  : %s\n", departmentName != null ? departmentName : "N/A");
		System.out.printf("Office Location  : %s\n", officeLocation != null ? officeLocation : "N/A");
		System.out.printf("Total Staff      : %d\n", supportStaffList.getSize());
		System.out.println("Staff Members    :");
		supportStaffList.displayStaff();
		System.out.println("-------------------------------------------------");
		System.out.printf("Total Categories : %d\n", categories.getSize());
		System.out.println("Managed Categories:");
		categories.displayCategories();
		System.out.println("=================================================\n");
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getOfficeLocation() {
		return officeLocation;
	}

	public void setOfficeLocation(String officeLocation) {
		this.officeLocation = officeLocation;
	}

	public SupportStaffList getSupportStaffList() {
		return supportStaffList;
	}

	public CategoryList getCategories() {
		return categories;
	}
}
