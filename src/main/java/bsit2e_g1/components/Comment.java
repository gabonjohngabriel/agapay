package bsit2e_g1.components;

public class Comment implements Displayable {
	private static int commentCounter = 0;

	private String commentId;
	private String dateCreated;
	private String content;
	private boolean isEdited = false;

	// Association: 1 Account — 0..* Comment (Author)
	private Account author;

	public Comment() {
	}

	public Comment(String dateCreated, String content, Account author) {
		commentCounter++;
		this.commentId = String.format("CMT-%03d", commentCounter);
		this.dateCreated = dateCreated;
		this.content = content;
		this.author = author;
	}

	public Comment(String commentId, String dateCreated, String content) {
		this(commentId, dateCreated, content, null);
	}

	public Comment(String commentId, String dateCreated, String content, Account author) {
		this.commentId = commentId;
		this.dateCreated = dateCreated;
		this.content = content;
		this.author = author;
	}

	public void editComment(String newContent) {
		if (newContent == null || newContent.isBlank()) {
			System.err.println("[AGAPAY] Failed to edit comment " + (commentId != null ? commentId : "") + ": Content cannot be null or empty.");
			return;
		}

		if (newContent.equals(this.content)) {
			System.out.println("[AGAPAY] Comment " + (commentId != null ? commentId : "") + " is unchanged.");
			return;
		}

		this.content = newContent.trim();
		this.isEdited = true;
		System.out.println("[AGAPAY] Comment " + (commentId != null ? commentId : "") + " updated successfully.");
	}

	@Override
	public void displayDetails() {
		displayComment();
	}

	public void displayComment() {
		System.out.printf("Comment ID   : %s\n", (commentId != null ? commentId : "N/A"));

		if (author != null) {
			System.out.printf("Author       : %s [%s]\n", author.getName(), author.getRoleTitle());
		} else {
			System.out.println("Author       : Anonymous/System");
		}

		System.out.printf("Date Created : %s %s\n", dateCreated, (isEdited ? "(edited)" : ""));
		System.out.printf("Content      : %s\n", (content != null && !content.isEmpty() ? content : "[No content]"));
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = (commentId != null) ? commentId.trim() : null;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = (content != null) ? content.trim() : "";
	}

	public Account getAuthor() {
		return author;
	}

	public void setAuthor(Account author) {
		this.author = author;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean edited) {
		this.isEdited = edited;
	}
}
