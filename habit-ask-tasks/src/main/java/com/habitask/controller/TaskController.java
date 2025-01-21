package com.habitask.controller;

import com.habitask.Dto.UserTaskDto;
import com.habitask.ResponseVo;
import com.habitask.model.Task;
import com.habitask.services.TaskService;
import com.habitask.services.UserTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private UserTaskService userTaskService;

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        try {
        Task createdTask = taskService.createTask(task);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseVo(true, "Tarea Ingresada", createdTask));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVo(false, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        Task task = null;
        try {
            task = taskService.updateTask(id, updatedTask);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseVo(true, "Tarea Actualizada", task));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVo(false, e.getMessage()));
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseVo(true, "Tarea Eliminada"));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVo(false, e.getMessage()));
        }
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<?> completeTask(@PathVariable Long id) {
        try {
            Task task = taskService.markAsCompleted(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseVo(true, "Tarea Completada", task));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVo(false, e.getMessage()));
        }
    }

    @GetMapping("/api/data")
    public Page<UserTaskDto> getUsersTasks(
            @RequestParam int page,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String email) {

        // Crear Pageable con la página y tamaño deseado
        Pageable pageable = PageRequest.of(page - 1, 10);  // Paginación comienza en 0

        // Llamar al servicio
        return userTaskService.findUsersTasks(pageable, name, email);
    }
}