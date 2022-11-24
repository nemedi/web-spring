package demo;

import java.util.UUID;

public class TaskModel implements BaseModel {

	public enum TaskStatus {
		OPEN,
		IN_PROGRESS,
		BLOCKED,
		CLOSED
	}
	
	public enum TaskSeverity {
		LOW,
		NORMAL,
		HIGH
	}
	
	private String id;
	private String title;
	private String description;
	private String assignedTo;
	private TaskStatus status;
	private TaskSeverity severity;
	
	public TaskModel() {
		id = UUID.randomUUID().toString();
	}

	public TaskModel(String title,
			String description,
			String assignedTo,
			TaskStatus status,
			TaskSeverity severity) {
		this();
		this.title = title;
		this.description = description;
		this.assignedTo = assignedTo;
		this.status = status;
		this.severity = severity;
	}

	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getAssignedTo() {
		return assignedTo;
	}
	
	public TaskStatus getStatus() {
		return status;
	}
	
	public TaskSeverity getSeverity() {
		return severity;
	}
	
	public String getId() {
		return id;
	}
	
	public void update(TaskModel task) {
		if (task != null) {
			title = task.getTitle();
			description = task.getDescription();
			assignedTo = task.getAssignedTo();
			status = task.getStatus();
			severity = task.getSeverity();
		}
	}
	
	public void patch(TaskModel task) {
		if (task != null) {
			if (task.getTitle() != null) {
				title = task.getTitle();
			}
			if (task.getDescription() != null) {
				description = task.getDescription();
			}
			if (task.assignedTo != null) {
				assignedTo = task.getAssignedTo();
			}
			if (task.getStatus() != null) {
				status = task.getStatus();
			}
			if (task.getSeverity() != null) {
				severity = task.getSeverity();
			}
		}
	}
	
}
