package com.habitask.services;

import com.habitask.Dto.UserDTO;
import com.habitask.model.Task;
import com.habitask.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserApiService userService; // Servicio que obtiene el usuario autenticado

    public Task createTask(Task task) {
        UserDTO currentUser = userService.getAuthenticatedUser();
        task.setIdUserCreator(currentUser.getId());
        task.setCreatedAt(new Date());
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask) throws Exception{
        Task task = validateTaskOwnership(id);
        task.setTitle(updatedTask.getTitle());
        task.setIdUserAssignee(updatedTask.getIdUserAssignee());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) throws Exception{
        Task task = validateTaskOwnership(id);
        if (task.isCompleted()) {
            throw new IllegalStateException("Cannot delete completed tasks.");
        }
        taskRepository.delete(task);
    }

    public Task markAsCompleted(Long id) throws Exception {
        Task task = validateTaskAssignment(id);
        task.setCompleted(true);
        task.setCompletedAt(new Date());
        return taskRepository.save(task);
    }

    private Task validateTaskOwnership(Long id) throws Exception{
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        UserDTO currentUser = userService.getAuthenticatedUser();
        if (!task.getIdUserCreator().equals(currentUser.getId())) {
            throw new RuntimeException("You can only modify your own tasks.");
        }
        return task;
    }

    private Task validateTaskAssignment(Long id) throws Exception {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        UserDTO currentUser = userService.getAuthenticatedUser();
        if (!task.getIdUserAssignee().equals(currentUser.getId())) {
            throw new RuntimeException("You can only complete tasks assigned to you.");
        }
        return task;
    }
}
