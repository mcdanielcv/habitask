package com.habitask.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserTaskDto {
    private String userName;
    private String userEmail;
    private String taskTitle;
    private String taskStatus;
    private Date taskCreatedAt;
    private Date taskCompletedAt;

    // Constructor
    public UserTaskDto(String userName, String userEmail, String taskTitle, String taskStatus,
                       Date taskCreatedAt, Date taskCompletedAt) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.taskTitle = taskTitle;
        this.taskStatus = taskStatus;
        this.taskCreatedAt = taskCreatedAt;
        this.taskCompletedAt = taskCompletedAt;
    }
}
