package bsit2e_g1.components;

import bsit2e_g1.datastructures.AttachmentList;
import bsit2e_g1.datastructures.CommentList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Ticket implements Displayable {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static int ticketNo = 0;
	private static int notifCounter = 0;

	public static void resetTicketCounter() {
		ticketNo = 0;
	}

	public static int getTicketCounter() {
		return ticketNo;
	}

	private String ticketId;
	private String title;
	private String description;
	private String dateSubmitted;
	private String dateResolved;
	private String priority;
	private String status;
	private String resolutionNotes;

	private boolean isArchived = false;
	private String dateArchived = "N/A";

	// Composition (1 Ticket ◆— 0..* Comment)
	private CommentList comments;

	// Composition (1 Ticket ◆— 0..* Attachment)
	private AttachmentList attachments;

	// Association (0..* Ticket — 1 Category)
	private Category category;

	// Association (1 User — 0..* Ticket)
	private User submitter;

	// Association (0..1 SupportStaff — 0..* Ticket)
	private SupportStaff assignedStaff;

	public Ticket() {
		this.comments = new CommentList();
		this.attachments = new AttachmentList();
	}

	public Ticket(String title, String description, String dateSubmitted, String priority, Category category, User submitter) {
		this();
		ticketNo++;

		this.ticketId = String.format("SA-%03d", ticketNo);
		this.title = title;
		this.description = description;
		this.dateSubmitted = dateSubmitted;
		this.dateResolved = "N/A";
		this.priority = priority;
		this.category = category;
		this.status = "Pending";
		this.submitter = submitter;
	}

	public void submit() {
		this.status = "Pending";
		if (this.submitter != null) {
			notifyUser(this.submitter, String.format("Ticket %s ('%s') was successfully submitted.", ticketId, title));
		}
	}

	public boolean accept(SupportStaff staff) {
		if (this.isArchived) {
			System.out.printf("[AGAPAY] Ticket %s is archived and read-only. Unarchive it first to modify.\n\n", ticketId);
			return false;
		}

		if ("In Progress".equalsIgnoreCase(this.status)) {
			System.out.printf("[AGAPAY] Ticket %s is already 'In Progress' by %s.\n\n", ticketId,
					assignedStaff != null ? assignedStaff.getName() : "another staff member");
			return false;
		}

		if ("Resolved".equalsIgnoreCase(this.status) || "Closed".equalsIgnoreCase(this.status) || "Cancelled".equalsIgnoreCase(this.status)) {
			System.out.printf("[AGAPAY] Cannot accept Ticket %s because it is already '%s'.\n\n", ticketId, status);
			return false;
		}

		this.assignedStaff = staff;
		this.status = "In Progress";

		if (this.submitter != null) {
			notifyUser(this.submitter, String.format("Ticket %s is now In Progress by %s.", ticketId, staff.getName()));
		}
		return true;
	}

	public boolean resolve(SupportStaff staff, String resolutionNotes) {
		if (this.isArchived) {
			System.out.printf("[AGAPAY] Ticket %s is archived and read-only. Unarchive it first to modify.\n\n", ticketId);
			return false;
		}

		if ("Pending".equalsIgnoreCase(this.status)) {
			System.out.printf("[AGAPAY] Ticket %s must be accepted first before it can be resolved.\n\n", ticketId);
			return false;
		}

		if (!"In Progress".equalsIgnoreCase(this.status)) {
			System.out.println("[AGAPAY] Ticket is already marked as " + this.status + ".\n");
			return false;
		}

		if (this.assignedStaff != null && !this.assignedStaff.getAccountId().equalsIgnoreCase(staff.getAccountId())) {
			System.out.printf("[AGAPAY] ACCESS DENIED: Only the assigned staff member (%s) can resolve this ticket.\n\n", this.assignedStaff.getName());
			return false;
		}

		if (resolutionNotes == null || resolutionNotes.isBlank()) {
			System.out.println("[AGAPAY] Resolution notes cannot be blank.\n");
			return false;
		}

		this.status = "Resolved";
		this.resolutionNotes = resolutionNotes.trim();
		this.assignedStaff = staff;
		this.dateResolved = LocalDateTime.now().format(FORMATTER);

		if (this.submitter != null) {
			notifyUser(this.submitter, String.format("Ticket %s has been RESOLVED by %s. Note: %s", ticketId, staff.getName(), this.resolutionNotes));
		}
		return true;
	}

	public boolean close() {
		return close(null);
	}

	public boolean close(Account actor) {
		if (this.isArchived) {
			System.out.printf("[AGAPAY] Ticket %s is ARCHIVED and READ-ONLY. Restore it first to modify.\n\n", ticketId);
			return false;
		}

		if (!"Resolved".equalsIgnoreCase(this.status)) {
			System.out.println("[AGAPAY] Only resolved tickets can be marked as Closed.\n");
			return false;
		}
		this.status = "Closed";
		if (actor instanceof User && this.assignedStaff != null) {
			notifyUser(this.assignedStaff, String.format("Ticket %s was verified and closed by %s.", ticketId, actor.getName()));
		} else if (this.submitter != null) {
			notifyUser(this.submitter, String.format("Ticket %s has been closed.", ticketId));
		}
		return true;
	}

	public boolean reopen(String reason) {
		if (this.isArchived) {
			System.out.printf("[AGAPAY] Ticket %s is ARCHIVED and READ-ONLY. Restore it first to modify.\n\n", ticketId);
			return false;
		}

		if (!"Resolved".equalsIgnoreCase(this.status) && !"Closed".equalsIgnoreCase(this.status)) {
			System.out.println("[AGAPAY] Only resolved or closed tickets can be reopened.\n");
			return false;
		}
		if (reason == null || reason.isBlank()) {
			System.out.println("[AGAPAY] A reason is required to reopen a ticket.\n");
			return false;
		}

		this.status = "In Progress";
		this.dateResolved = "N/A";
		String now = LocalDateTime.now().format(FORMATTER);
		Comment reopenComment = new Comment(now, "Ticket reopened. Reason: " + reason.trim(), this.submitter);
		this.comments.addComment(reopenComment);

		if (this.assignedStaff != null) {
			notifyUser(this.assignedStaff, String.format("Ticket %s has been REOPENED by submitter. Reason: %s", ticketId, reason.trim()));
		}
		return true;
	}

	public boolean canBeCancelled() {
		return !this.isArchived && "Pending".equalsIgnoreCase(this.status);
	}

	public boolean cancel(User user) {
		if (this.isArchived) {
			System.out.printf("[AGAPAY] Ticket %s is ARCHIVED and READ-ONLY. Restore it first to modify.\n\n", ticketId);
			return false;
		}

		if (!canBeCancelled()) {
			System.out.printf("[AGAPAY] Cannot cancel Ticket %s. Current status is '%s'.\n\n", ticketId, status);
			return false;
		}
		this.status = "Cancelled";
		if (this.assignedStaff != null) {
			String name = (user != null && user.getName() != null) ? user.getName() : "user";
			notifyUser(this.assignedStaff, String.format("Ticket %s has been cancelled by user %s.", ticketId, name));
		}
		return true;
	}

	public boolean canBeArchived() {
		return !this.isArchived && ("Resolved".equalsIgnoreCase(this.status)
				|| "Closed".equalsIgnoreCase(this.status)
				|| "Cancelled".equalsIgnoreCase(this.status));
	}

	public boolean archive() {
		if (this.isArchived) {
			System.out.printf("[AGAPAY] Ticket %s is already archived.\n\n", ticketId);
			return false;
		}
		if (!canBeArchived()) {
			System.out.printf("[AGAPAY] Cannot archive Ticket %s while status is '%s'. Only Resolved, Closed, or Cancelled tickets can be archived.\n\n", ticketId, status);
			return false;
		}
		this.isArchived = true;
		this.dateArchived = LocalDateTime.now().format(FORMATTER);
		return true;
	}

	public boolean unarchive() {
		if (!this.isArchived) {
			System.out.printf("[AGAPAY] Ticket %s is not archived.\n\n", ticketId);
			return false;
		}
		this.isArchived = false;
		this.dateArchived = "N/A";
		return true;
	}

	public boolean isOverdue() {
		if (this.isArchived) {
			return false;
		}
		try {
			LocalDateTime submitted = LocalDateTime.parse(this.dateSubmitted, FORMATTER);
			return "Pending".equalsIgnoreCase(this.status) && submitted.plusDays(2).isBefore(LocalDateTime.now());
		} catch (Exception e) {
			return false;
		}
	}

	public void addComment(Comment comment) {
		if (this.isArchived) {
			System.out.printf("[AGAPAY] Ticket %s is ARCHIVED and READ-ONLY. Restore it first to add comments.\n\n", ticketId);
			return;
		}
		if (comment != null) {
			this.comments.addComment(comment);
			if (comment.getAuthor() instanceof SupportStaff && submitter != null) {
				notifyUser(submitter, String.format("New comment from staff on Ticket %s: %s", ticketId, comment.getContent()));
			} else if (comment.getAuthor() instanceof User && assignedStaff != null) {
				notifyUser(assignedStaff, String.format("New comment from %s on Ticket %s: %s", comment.getAuthor().getName(), ticketId, comment.getContent()));
			}
		}
	}

	public void addAttachment(Attachment attachment) {
		if (this.isArchived) {
			System.out.printf("[AGAPAY] Ticket %s is ARCHIVED and READ-ONLY. Restore it first to attach files.\n\n", ticketId);
			return;
		}
		if (attachment != null) {
			this.attachments.addAttachment(attachment);
		}
	}

	private void notifyUser(Account recipient, String message) {
		notifCounter++;
		Notification n = new Notification(String.format("NOTIF-%03d", notifCounter), message, this, recipient);
		n.sendNotification();
	}

	public void displaySummary() {
		String submitterName = (submitter != null) ? submitter.getName() : "N/A";
		String staffName = (assignedStaff != null) ? assignedStaff.getName() : "Unassigned";
		String categoryName = (category != null) ? category.getCategoryName() : "General";
		String deptId = (category != null && category.getDepartment() != null)
				? category.getDepartment().getDepartmentId()
				: "General";

		System.out.printf("Ticket ID      : %s\n", ticketId);
		System.out.printf("Title          : %s\n", title);
		System.out.printf("Description    : %s\n", description);
		System.out.println("----------------------------------------------------");
		System.out.printf("Department     : %s\n", deptId);
		System.out.printf("Category       : %s\n", categoryName);
		System.out.printf("Priority       : %s\n", priority);
		String overdueTag = isOverdue() ? " [OVERDUE]" : "";
		System.out.printf("Status         : %s%s%s\n", status, isArchived ? " [ARCHIVED]" : "", overdueTag);
		System.out.printf("Submitted By   : %s\n", submitterName);
		System.out.printf("Assigned Staff : %s\n", staffName);
		System.out.printf("Date Submitted : %s\n", dateSubmitted);
		System.out.printf("Date Resolved  : %s\n", dateResolved);
		if (isArchived) {
			System.out.printf("Date Archived  : %s\n", dateArchived);
		}
		if (resolutionNotes != null && !resolutionNotes.isBlank()) {
			System.out.printf("Resolution Note: %s\n", resolutionNotes);
		}
	}

	@Override
	public void displayDetails() {
		System.out.println("====================================================");
		System.out.println("                 TICKET DETAILS                     ");
		System.out.println("====================================================");
		displaySummary();
		System.out.println("----------------------------------------------------");
		System.out.println("Attachments (" + attachments.getSize() + "):");
		attachments.displayAllAttachments();
		System.out.println("----------------------------------------------------");
		System.out.println("Comments Log (" + comments.getSize() + "):");
		comments.displayAllComments();
		System.out.println("====================================================\n");
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDateSubmitted() {
		return dateSubmitted;
	}

	public void setDateSubmitted(String dateSubmitted) {
		this.dateSubmitted = dateSubmitted;
	}

	public String getDateResolved() {
		return dateResolved;
	}

	public void setDateResolved(String dateResolved) {
		this.dateResolved = dateResolved;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if (this.isArchived) {
			System.out.printf("[AGAPAY] Ticket %s is ARCHIVED and READ-ONLY. Restore it first to modify.\n\n", ticketId);
			return;
		}
		this.status = status;
	}

	public boolean isArchived() {
		return isArchived;
	}

	public void setArchived(boolean isArchived) {
		this.isArchived = isArchived;
	}

	public String getDateArchived() {
		return dateArchived;
	}

	public void setDateArchived(String dateArchived) {
		this.dateArchived = dateArchived;
	}

	public String getResolutionNotes() {
		return resolutionNotes;
	}

	public void setResolutionNotes(String resolutionNotes) {
		this.resolutionNotes = resolutionNotes;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public User getSubmitter() {
		return submitter;
	}

	public void setSubmitter(User submitter) {
		this.submitter = submitter;
	}

	public SupportStaff getAssignedStaff() {
		return assignedStaff;
	}

	public void setAssignedStaff(SupportStaff assignedStaff) {
		this.assignedStaff = assignedStaff;
	}

	public CommentList getComments() {
		return comments;
	}

	public AttachmentList getAttachments() {
		return attachments;
	}
}
