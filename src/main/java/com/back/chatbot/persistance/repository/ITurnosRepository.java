package com.back.chatbot.persistance.repository;

import com.back.chatbot.persistance.entity.AppointmentEntity;
import com.back.chatbot.persistance.entity.TurnoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface ITurnosRepository extends JpaRepository<TurnoEntity, String> {

    @Query(value = "SELECT * FROM turno_entity", nativeQuery = true)
    List<TurnoEntity> findAllTurnos();

    @Query(value = "SELECT * FROM turno_entity WHERE DATE(fecha_turno) >= :date", nativeQuery = true)
    List<TurnoEntity> findAllDaysByData(@Param("date") LocalDate date);

    @Query(value = "SELECT * FROM turno_entity WHERE DATE(fecha_turno) = :date", nativeQuery = true)
    List<TurnoEntity> findOneDayByData(@Param("date") LocalDate date);
}
