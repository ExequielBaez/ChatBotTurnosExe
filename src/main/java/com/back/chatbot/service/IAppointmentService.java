package com.back.chatbot.service;

import com.back.chatbot.controller.dto.request.AppointmentRequestDTO;
import com.back.chatbot.controller.dto.response.TurnoResponseDTO;
import com.back.chatbot.persistance.entity.AppointmentEntity;
import com.back.chatbot.persistance.entity.TurnoEntity;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface IAppointmentService {
    AppointmentEntity createAppointment(AppointmentEntity appointmentEntity);
    List<AppointmentEntity> findAll();
    List<TurnoResponseDTO> getTurnos();

    List<TurnoResponseDTO> getAppByNextDay(String date, String moment);
    List<TurnoResponseDTO> getAppByDate(String date, String moment);
}
