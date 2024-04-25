package com.back.chatbot.controller;

import com.back.chatbot.controller.dto.request.AppointmentRequestDTO;
import com.back.chatbot.controller.dto.response.TurnoResponseDTO;
import com.back.chatbot.persistance.entity.AppointmentEntity;
import com.back.chatbot.service.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("v1/api/appointment")
@CrossOrigin(value = "http://localhost:3000")
public class AppointmentController {

    @Autowired
    private IAppointmentService appointmentService;

    /*@GetMapping
    private ResponseEntity<?> getAll(){

        List<AppointmentEntity> listA = appointmentService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(listA);

    }*/
    @PostMapping
    private ResponseEntity<?> createAppointment(@RequestBody AppointmentEntity appointmentEntity){

        AppointmentEntity appointment = appointmentService.createAppointment(appointmentEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }

    @GetMapping
    private ResponseEntity<?> getTurnos(){
        List<TurnoResponseDTO> turnos = appointmentService.getTurnos();
        return ResponseEntity.status(HttpStatus.OK).body(turnos);

    }

    @GetMapping("/appByDate")
    private ResponseEntity<?> getAppByDate(@RequestParam String date,
                                        @RequestParam String moment){

        List<TurnoResponseDTO> listA = appointmentService.getAppByDate(date, moment);
        return ResponseEntity.status(HttpStatus.OK).body(listA);

    }

    @GetMapping("/appNextDay")
    private ResponseEntity<?> getAppNextDay(@RequestParam String date,
                                        @RequestParam String moment){

        List<TurnoResponseDTO> listA = appointmentService.getAppByNextDay(date, moment);
        return ResponseEntity.status(HttpStatus.OK).body(listA);

    }
}
