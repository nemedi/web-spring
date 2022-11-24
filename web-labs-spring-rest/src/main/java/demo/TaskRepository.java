package demo;

import static java.util.stream.Collectors.toMap;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class TaskRepository {

	private File file;
	private Map<String, TaskModel> tasks;
	
	public TaskRepository(@Value("${repository}") String repository) throws Exception {
		file = Paths.get(repository).toFile();
		tasks = file.exists()
				? Arrays.stream(new ObjectMapper()
							.readValue(file, TaskModel[].class))
						.collect(toMap(task -> task.getId(), task -> task))
				: new HashMap<String, TaskModel>();
	}
	
	public Collection<TaskModel> findAll() {
		return tasks.values();
	}
	
	public Optional<TaskModel> findById(String id) {
		return tasks.containsKey(id)
				? Optional.of(tasks.get(id))
				: Optional.empty();
	}

	public void save(TaskModel task) throws Exception {
		if (tasks.containsKey(task.getId())) {
			tasks.get(task.getId()).update(task);
		} else {
			tasks.put(task.getId(), task);
		}
		writeToFile();
	}
	
	public void deleteById(String id) throws Exception {
		if (tasks.containsKey(id)) {
			tasks.remove(id);
			writeToFile();
		} else {
			throw new IllegalArgumentException("Invalid user id: " + id);
		}
	}

	private void writeToFile() throws Exception {
		new ObjectMapper().writeValue(file, findAll());
	}
	
}
