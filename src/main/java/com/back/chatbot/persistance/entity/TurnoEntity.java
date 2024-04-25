package com.back.chatbot.persistance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;


@Entity
@Data
public class TurnoEntity {
    @Id
    @UuidGenerator
    private String idTurno;

    private LocalDateTime fechaTurno;


}
