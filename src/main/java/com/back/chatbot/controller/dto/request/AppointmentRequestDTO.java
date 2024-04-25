package com.back.chatbot.controller.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRequestDTO {
    private String date;
    private String moment;

}
