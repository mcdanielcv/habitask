package com.habitask.services;

import com.habitask.Dto.UserDTO;
import com.habitask.Dto.UserTaskDto;
import com.habitask.model.Task;
import com.habitask.repository.UserTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class UserTaskService {

    @Autowired
    private UserTaskRepository userTaskRepository;

    @Autowired
    private UserApiService userApiService;

    public Page<UserTaskDto> findUsersTasks(Pageable pageable, String name, String email) {
        // Llamada al repositorio para obtener las tareas
        Page<Task> tasks = userTaskRepository.findByUserUserNameContainingAndUserUserEmailContaining(
                name, email, pageable);

        // Convertir las tareas a DTOs
        return tasks.map(task -> {
            // Obtener el usuario asociado a la tarea utilizando su ID
            UserDTO userDtoCreate = userApiService.getUserDtoById(task.getIdUserCreator());
            // Crear el DTO de UserTask y devolverlo
            return new UserTaskDto(
                    userDtoCreate.getName(),        // Nombre del usuario
                    userDtoCreate.getEmail(),       // Email del usuario
                    task.getTitle(),                // Título de la tarea
                    (task.isCompleted() ? "TERMINADO" :"EJECUCION"),               // Estado de la tarea
                    task.getCreatedAt(),            // Fecha de creación de la tarea
                    task.getCompletedAt()           // Fecha de completado de la tarea
            );
        });
    }

}
