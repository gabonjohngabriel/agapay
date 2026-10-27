package bsit2e_g1.system;

import bsit2e_g1.components.Category;
import bsit2e_g1.components.Department;
import bsit2e_g1.components.SupportStaff;
import bsit2e_g1.datastructures.AccountList;
import bsit2e_g1.datastructures.DepartmentList;
import bsit2e_g1.datastructures.TicketList;
import bsit2e_g1.ui.Operations;
import bsit2e_g1.ui.staff.SupportStaffMenu;
import bsit2e_g1.ui.user.UserMenu;
import bsit2e_g1.utils.InputHelper;

public class CampusHelpDesk {
	public static void main(String[] args) throws InterruptedException {
		// Group 4

		// Initialize university departments
		Department deptCAL = new Department("CAL", "College of Arts and Letters", "Federizo Hall, 2nd Floor");
		Department deptCICT = new Department("CICT", "College of Information and Technology", "Pimentel Hall, 3rd Floor");
		Department deptCON = new Department("CON", "College of Nursing", "Pimentel Hall, 1st Floor");

		// Seed categories into departments
		deptCICT.addCategory(new Category("CAT-01", "Network & Internet Connectivity", "Wi-Fi, LAN, and campus firewall access", deptCICT));
		deptCICT.addCategory(new Category("CAT-02", "Hardware & Lab Workstations", "Desktop PCs, monitors, peripherals, and laboratory rigs", deptCICT));
		deptCICT.addCategory(new Category("CAT-03", "Software & Development Tooling", "IDE licenses, OS images, database servers, and compilers", deptCICT));
		deptCICT.addCategory(new Category("CAT-04", "Server & Cloud Infrastructure", "VM provisioning, internal hosting, and Docker/cloud environments", deptCICT));
		deptCICT.addCategory(new Category("CAT-05", "User Accounts & Authentication", "LMS access, university email, Git accounts, and password resets", deptCICT));

		deptCON.addCategory(new Category("CAT-06", "Simulation Labs & Manikins", "High-fidelity manikins, CPR monitors, and anatomy simulation gear", deptCON));
		deptCON.addCategory(new Category("CAT-07", "Medical Supplies & Consumables", "PPE, sterilization packs, syringes, IV kits, and dressing supplies", deptCON));
		deptCON.addCategory(new Category("CAT-08", "Clinical Rotations & Duty Scheduling", "RLE hospital placements, shift assignments, and affiliation clearances", deptCON));
		deptCON.addCategory(new Category("CAT-09", "Biohazard & Infection Control", "Sharps disposal units, hazardous waste handling, and lab safety audits", deptCON));
		deptCON.addCategory(new Category("CAT-10", "Skills Assessment & Licensure Prep", "OSCE station setups, clinical skill checklists, and board review modules", deptCON));

		deptCAL.addCategory(new Category("CAT-11", "Audio Visual & Production Gear", "Cameras, boom mics, field recorders, and studio lighting kits", deptCAL));
		deptCAL.addCategory(new Category("CAT-12", "Theater & Performance Spaces", "Black box theater, stage rigging, acoustic control, and prop storage", deptCAL));
		deptCAL.addCategory(new Category("CAT-13", "Fine Arts & Exhibition Studios", "Easels, sculpting stands, drafting tables, and gallery displays", deptCAL));
		deptCAL.addCategory(new Category("CAT-14", "Speech & Language Laboratories", "Acoustic booths, phonetics software, and audio translation headsets", deptCAL));
		deptCAL.addCategory(new Category("CAT-15", "Literary & Publication Archiving", "Manuscript submissions, campus journals, and literary folio reviews", deptCAL));

		// Initialize demo support staff
		SupportStaff[] staff = {
				new SupportStaff("SS-001", "Santos, Shaan Earle Huey", "shaanearlehueysantos@ms.bulsu.edu.ph", "09054054286", "Network Administrator", false, deptCICT),
				new SupportStaff("SS-002", "Jacob, Armia Jean", "armiajeanjacob@ms.bulsu.edu.ph", "09766732133", "Systems & Hardware Technician", false, deptCICT),
				new SupportStaff("SS-003", "Martos, Joshua", "joshuamartos@ms.bulsu.edu.ph", "09171234567", "Computer Lab Technician", false, deptCICT),
				new SupportStaff("SS-004", "Mendoza, Lance Maurice", "lancemauricemendoza@ms.bulsu.edu.ph", "09282345678", "Network & Systems Assistant", false, deptCICT),
				new SupportStaff("SS-005", "Bautista, John Carlo", "johncarlo.bautista@ms.bulsu.edu.ph", "09393456789", "Software & Database Support Specialist", false, deptCICT),
				new SupportStaff("SS-006", "Mendoza, Maria Clarisse", "mariaclarisse.mendoza@ms.bulsu.edu.ph", "09454567890", "Simulation Lab Coordinator", false, deptCON),
				new SupportStaff("SS-007", "Aquino, Rafael James", "rafaeljames.aquino@ms.bulsu.edu.ph", "09565678901", "Clinical Materials & Inventory Custodian", false, deptCON),
				new SupportStaff("SS-008", "Villanueva, Sarah Nicole", "sarahnicole.villanueva@ms.bulsu.edu.ph", "09676789012", "Skills Lab Technician", false, deptCON),
				new SupportStaff("SS-009", "Garcia, Christian Paul", "christianpaul.garcia@ms.bulsu.edu.ph", "09787890123", "AV & Multimedia Specialist", false, deptCAL),
				new SupportStaff("SS-010", "Navarro, Alyssa Marie", "alyssamarie.navarro@ms.bulsu.edu.ph", "09898901234", "Theater & Stage Technical Assistant", false, deptCAL),
				new SupportStaff("SS-011", "Mercado, Francis Gabriel", "francisgabriel.mercado@ms.bulsu.edu.ph", "09909012345", "Speech Lab & Production Custodian", false, deptCAL)
		};

		// Add demo support staff respective to their department
		for (int i = 0; i < staff.length; i++) {
			staff[i].getDepartment().addSupportStaff(staff[i]);
		}

		// Initialize lists
		DepartmentList departmentList = new DepartmentList();
		departmentList.addDepartment(deptCICT);
		departmentList.addDepartment(deptCON);
		departmentList.addDepartment(deptCAL);

		AccountList accountList = new AccountList(staff);
		TicketList ticketList = new TicketList();

		System.out.println("AGAPAY | BulSU Campus Help Desk Ticketing System initialized...\n");
		Thread.sleep(1000);

		System.out.println("""
				 █████╗  ██████╗  █████╗ ██████╗  █████╗ ██╗   ██╗
				██╔══██╗██╔════╝ ██╔══██╗██╔══██╗██╔══██╗╚██╗ ██╔╝
				███████║██║  ███╗███████║██████╔╝███████║ ╚████╔╝ 
				██╔══██║██║   ██║██╔══██║██╔═══╝ ██╔══██║  ╚██╔╝  
				██║  ██║╚██████╔╝██║  ██║██║     ██║  ██║   ██║   
				╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝  ╚═╝   ╚═╝
				""");

		while (true) {
			System.out.println("====================================================");
			System.out.println("     AGAPAY | CAMPUS HELP DESK TICKETING SYSTEM");
			System.out.println("====================================================\n");
			System.out.println("[1] User");
			System.out.println("[2] Support Staff");
			System.out.println("[0] Exit\n");

			int choice = InputHelper.readInt("Choice: ", 0, 2);
			System.out.println();

			if (choice == 0) {
				System.out.println("[AGAPAY] Thank you for using our service!\n");
				Thread.sleep(1000);

				System.out.println("""
						 █████╗  ██████╗  █████╗ ██████╗  █████╗ ██╗   ██╗
						██╔══██╗██╔════╝ ██╔══██╗██╔══██╗██╔══██╗╚██╗ ██╔╝
						███████║██║  ███╗███████║██████╔╝███████║ ╚████╔╝ 
						██╔══██║██║   ██║██╔══██║██╔═══╝ ██╔══██║  ╚██╔╝  
						██║  ██║╚██████╔╝██║  ██║██║     ██║  ██║   ██║   
						╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝  ╚═╝   ╚═╝
						""");

				break;
			} else {
				Operations menu = null;

				if (choice == 1) {
					menu = new UserMenu();
				} else if (choice == 2) {
					menu = new SupportStaffMenu();
				}

				if (menu != null) {
					// Providing the initialized database to the menu
					menu.run(accountList, ticketList, departmentList);
				}
			}
		}

		System.out.println();
	}
}