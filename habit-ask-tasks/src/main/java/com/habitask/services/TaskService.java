package com.habitask.services;

import com.habitask.Dto.UserDTO;
import com.habitask.model.Notification;
import com.habitask.model.Task;
import com.habitask.repository.TaskRepository;
import com.habitask.service.EmailNotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserApiService userService; // Servicio que obtiene el usuario autenticado

    private final EmailNotificationService notificationService;

    public TaskService(TaskRepository taskRepository,
                       UserApiService userService,
                       EmailNotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public Task createTask(Task task, String token) {
        UserDTO currentUser = userService.getAuthenticatedUser(token);
        task.setIdUserCreator(currentUser.getId());
        task.setCreatedAt(new Date());
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask , String token) throws Exception{
        Task task = validateTaskOwnership(id, token );
        task.setTitle(updatedTask.getTitle());
        task.setIdUserAssignee(updatedTask.getIdUserAssignee());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id , String token) throws Exception{
        Task task = validateTaskOwnership(id, token);
        if (task.isCompleted()) {
            throw new IllegalStateException("Cannot delete completed tasks.");
        }
        taskRepository.delete(task);
    }

    public Task markAsCompleted(Long id, String token) throws Exception {
        Task task = validateTaskAssignment(id, token);
        task.setCompleted(true);
        task.setCompletedAt(new Date());

        // Notificar al creador de la tarea
        Notification notification = new Notification();

        //obtener usuario dado el id
        UserDTO userDtoCreado= userService.getUserDtoById(task.getIdUserCreator());
        log.info("markAsCompleted::UserDTO"+userDtoCreado.getId()+"-mail->"+userDtoCreado.getEmail());

        //

        notification.setRecipient(userDtoCreado.getEmail());
        notification.setSubject("Tarea completada");
        notification.setMessage("La tarea '" + task.getTitle() + "' ha sido completada.");
        notification.setTimestamp(new Date());

        notificationService.sendNotification(notification);
        log.info("markAsCompleted::notificacion enviada");
        return taskRepository.save(task);
    }

    private Task validateTaskOwnership(Long id, String token) throws Exception{
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        UserDTO currentUser = userService.getAuthenticatedUser(token);
        if (!task.getIdUserCreator().equals(currentUser.getId())) {
            throw new RuntimeException("You can only modify your own tasks.");
        }
        return task;
    }

    private Task validateTaskAssignment(Long id, String token) throws Exception {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        UserDTO currentUser = userService.getAuthenticatedUser(token);
        if (!task.getIdUserAssignee().equals(currentUser.getId())) {
            throw new RuntimeException("You can only complete tasks assigned to you.");
        }
        return task;
    }
}
