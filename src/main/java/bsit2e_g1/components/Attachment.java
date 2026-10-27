package bsit2e_g1.components;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Attachment {
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static int attachmentCounter = 0;

	private String attachmentId;
	private String fileName;
	private String filePath;
	private String fileType;

	private long fileSize;
	private String dateUploaded;

	public Attachment() {
	}

	public Attachment(String filePath) {
		attachmentCounter++;
		setAttachmentId(String.format("ATT-%03d", attachmentCounter));
		setDateUploaded(LocalDateTime.now().format(DATE_FORMATTER));
		uploadFile(filePath);
	}

	public Attachment(String attachmentId, String filePath) {
		setAttachmentId(attachmentId);
		setDateUploaded(LocalDateTime.now().format(DATE_FORMATTER));
		uploadFile(filePath);
	}

	public Attachment(String attachmentId, String fileName, String filePath, String fileType, long fileSize) {
		setAttachmentId(attachmentId);
		setFileName(fileName);

		if (filePath != null && !filePath.isBlank()) {
			File file = new File(filePath.trim());
			setFileSize(file.exists() ? file.length() : 0L);
		}

		setFileType(fileType);
		setFileSize(fileSize);
		setDateUploaded(LocalDateTime.now().format(DATE_FORMATTER));
	}

	public void uploadFile(String filePath) {
		if (filePath == null || filePath.trim().isBlank()) {
			System.out.println("[AGAPAY] Upload failed: File path cannot be empty.");
			return;
		}

		setFilePath(filePath.trim());

		File file = new File(this.filePath);
		setFileName(file.getName());

		// Extract file extension automatically
		String name = file.getName();
		int dotIdx = name.lastIndexOf('.');
		if (dotIdx > 0 && dotIdx < name.length() - 1) {
			setFileType(name.substring(dotIdx + 1).toUpperCase() + " File");
		} else {
			setFileType("Document");
		}

		if (file.exists()) {
			setFileSize(file.length());
			System.out.printf("[AGAPAY] File '%s' (%s, %d bytes) successfully attached to ID %s.\n", this.fileName, this.fileType, this.fileSize, this.attachmentId);
		} else {
			setFileSize(0L);
			System.out.printf("[AGAPAY] File metadata registered: '%s' (%s) [Path: %s].\n", this.fileName, this.fileType, this.filePath);
		}

		this.dateUploaded = LocalDateTime.now().format(DATE_FORMATTER);
	}

	public void removeFile() {
		if (this.filePath == null && this.fileName == null) {
			System.out.println("[AGAPAY] No file is currently attached to ID " + (attachmentId != null ? attachmentId : ""));
			return;
		}

		System.out.printf("[AGAPAY] Attachment '%s' (ID: %s) has been detached from ticket.\n", this.fileName, this.attachmentId);
		setFileName(null);
		setFilePath(null);
		setFileType(null);
		setFileSize(0L);
	}

	public String getFileInfo() {
		if (this.fileName == null && this.filePath == null) {
			return String.format("[AGAPAY] Attachment ID: %s | Status: [Empty / Detached]", attachmentId);
		}
		return String.format("ID: %s | File: %s | Type: %s | Size: %d bytes | Path: %s | Uploaded: %s",
				attachmentId != null ? attachmentId : "N/A",
				fileName != null ? fileName : "N/A",
				fileType != null ? fileType : "N/A",
				fileSize,
				filePath != null ? filePath : "N/A",
				dateUploaded != null ? dateUploaded : "N/A");
	}

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getDateUploaded() {
		return dateUploaded;
	}

	public void setDateUploaded(String dateUploaded) {
		this.dateUploaded = dateUploaded;
	}
}
