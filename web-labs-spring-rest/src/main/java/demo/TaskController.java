package demo;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import demo.TaskModel.TaskSeverity;
import demo.TaskModel.TaskStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

	@Autowired
	private TaskService service;
	
	@Operation(summary = "Search tasks", operationId = "getTasks")
    @ApiResponses({ 
            @ApiResponse(responseCode = "200", description = "Found tasks", 
                content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TaskModel[].class))}),
            @ApiResponse(responseCode = "204", description = "No tasks found")})	
	@GetMapping
	public ResponseEntity<List<Object>> getTasks(
			@RequestHeader(name = "X-Fields", required = false) String fields,
			@RequestHeader(name = "X-Sort", required = false) String sort,
			@RequestParam(required = false) String title,
			@RequestParam(required = false) String description,
			@RequestParam(required = false) String assignTo,
			@RequestParam(required = false) TaskStatus status,
			@RequestParam(required = false) TaskSeverity severity) {
		List<TaskModel> tasks = service.getTasks(title, description, assignTo, status, severity);
		if (tasks.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			List<Object> items = null;
			if (fields != null && !fields.isEmpty()) {
				items = tasks.stream()
					.map(task -> task.sparseFields(fields.split(",")))
					.collect(toList());
			} else {
				items = new ArrayList<Object>(tasks);
			}
			if (sort != null && !sort.isEmpty()) {
				return ResponseEntity.ok(items.stream()
						.sorted((first, second) ->
							BaseModel.sort(sort).apply(first, second))
						.collect(toList()));
			} else {
				return ResponseEntity.ok(items);
			}
		}
		
	}
	
	@Operation(summary = "Get a task", operationId = "getTask")
    @ApiResponses({ 
            @ApiResponse(responseCode = "200", description = "Found the task",
            	content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TaskModel.class))}),
            @ApiResponse(responseCode = "404", description = "Task not found")})	
	@GetMapping("/{id}")
	public ResponseEntity<?> getTask(
			@RequestHeader(name = "X-Fields", required = false) String fields,
			@PathVariable String id) {
		Optional<TaskModel> task = service.getTask(id);
		if (!task.isPresent()) {
			return ResponseEntity.notFound().build();
		} else if (fields != null && !fields.isEmpty()) {
			return ResponseEntity.ok(task.get().sparseFields(fields.split(",")));
		} else {
			return ResponseEntity.ok(task.get());
		}
	}
	
	@Operation(summary = "Create a new task", operationId = "addTask")
    @ApiResponses({ 
            @ApiResponse(responseCode = "201", description = "Task was created",
            		headers = {@Header(name = "location", schema = @Schema(type = "string"))}),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
	@PostMapping
	public ResponseEntity<Void> addTask(@RequestHeader(name = "X-Action", required = false) String action,
			@RequestBody String payload) {
		try {
			if ("bulk".equals(action)) {
				for (TaskModel task : new ObjectMapper().readValue(payload, TaskModel[].class)) {
					service.addTask(task);
				}
				return ResponseEntity.noContent().build();
			} else {
				TaskModel task = service.addTask(new ObjectMapper().readValue(payload, TaskModel.class));
				URI uri = linkTo(getClass()).slash(task.getId()).toUri();
				return ResponseEntity.created(uri).build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@Operation(summary = "Update a task", operationId = "updateTask")
    @ApiResponses({ 
            @ApiResponse(responseCode = "204", description = "Task was update"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateTask(@PathVariable String id, @RequestBody TaskModel task) {
		try {
			Optional<TaskModel> existingTask = service.getTask(id);
			if (existingTask.isPresent()) {
				existingTask.get().update(task);
				service.updateTask(existingTask.get());
				return ResponseEntity.noContent().build();
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@Operation(summary = "Patch a task", operationId = "patchTask")
    @ApiResponses({ 
            @ApiResponse(responseCode = "204", description = "Task was patched"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
	@PatchMapping("/{id}")
	public ResponseEntity<Void> patchTask(@PathVariable String id, @RequestBody TaskModel task) {
		try {
			Optional<TaskModel> existingTask = service.getTask(id);
			if (existingTask.isPresent()) {
				existingTask.get().patch(task);
				service.updateTask(existingTask.get());
				return ResponseEntity.noContent().build();
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@Operation(summary = "Delete a task", operationId = "removeTask")
    @ApiResponses({ 
            @ApiResponse(responseCode = "204", description = "Task was deleted"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> removeNote(@PathVariable String id) {
		try {
			if (service.getTask(id).isPresent()) {
				service.removeTask(id);
				return ResponseEntity.noContent().build();
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
