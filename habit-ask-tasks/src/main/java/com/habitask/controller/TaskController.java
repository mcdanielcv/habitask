package com.habitask.controller;

import com.habitask.ResponseVo;
import com.habitask.model.Task;
import com.habitask.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        try {
        Task createdTask = taskService.createTask(task);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseVo(true, "Tarea Ingresada", createdTask));
        } catch (Exception e) {
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVo(false, e.getMessage()));
        }
    }
}