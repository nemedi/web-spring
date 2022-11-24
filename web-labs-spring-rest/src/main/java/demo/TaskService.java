package demo;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.TaskModel.TaskSeverity;
import demo.TaskModel.TaskStatus;

@Service
public class TaskService {

	@Autowired
	private TaskRepository repository;
	
	public List<TaskModel> getTasks(String title,
			String description,
			String assignTo,
			TaskStatus status,
			TaskSeverity severity) {
		return repository.findAll()
				.stream()
				.filter(task -> isMatch(task, title, description, assignTo, status, severity))
				.collect(toList());
	}
	
	public Optional<TaskModel> getTask(String id) {
		return repository.findById(id);
	}
	
	public TaskModel addTask(TaskModel task) throws Exception {
		repository.save(task);
		return task;
	}

	public void updateTask(TaskModel task) throws Exception {
		repository.save(task);
	}
	
	public void removeTask(String id) throws Exception {
		repository.deleteById(id);
	}

	private boolean isMatch(TaskModel task,
			String title,
			String description,
			String assignTo,
			TaskStatus status,
			TaskSeverity severity) {
		return (title == null
				|| task.getTitle() != null
					&& task.getTitle()
					.toLowerCase()
					.startsWith(title.toLowerCase()))
				&& (description == null
						|| task.getDescription() != null
						&& task.getDescription()
						.toLowerCase()
						.startsWith(description.toLowerCase())
				&& (assignTo == null
						|| task.getAssignedTo() != null
						&& task.getAssignedTo()
						.toLowerCase()
						.startsWith(assignTo.toLowerCase()))
				&& (status == null
						|| task.getStatus() != null
						&& task.getStatus().equals(status))
				&& (severity == null
						|| task.getSeverity().equals(severity)));
	}
}
