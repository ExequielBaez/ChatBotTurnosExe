package com.back.chatbot.persistance.entity;

import com.back.chatbot.enums.AppointmentDay;
import com.back.chatbot.enums.AppointmentState;
import com.back.chatbot.enums.AppointmentTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@Entity
@Data
public class AppointmentEntity {

    @Id
    @UuidGenerator
    private String idTurno;

    private LocalDate fechaTurno;

    //@Enumerated(value = EnumType.STRING)
    //private AppointmentDay appointmentDay;

    private String day;

    @Enumerated(value = EnumType.STRING)
    private AppointmentTime appointmentTime;

    @Enumerated(value = EnumType.STRING)
    private AppointmentState appointmentState;

    public AppointmentEntity() {
       this.fechaTurno = LocalDate.now();
       this.day = getDayOfWeek(fechaTurno);

    }

    private String getDayOfWeek(LocalDate fechaTurno) {

        DayOfWeek dayOfWeek = fechaTurno.getDayOfWeek();

        String nameOfTheDay = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());

        return nameOfTheDay;
    }
}
