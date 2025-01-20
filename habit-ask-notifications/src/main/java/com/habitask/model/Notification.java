package com.habitask.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Notification {
    private String recipient; // Email u otro identificador del destinatario
    private String subject;   // Asunto de la notificación
    private String message;   // Cuerpo de la notificación
    private Date timestamp; // Fecha y hora del envío
}
