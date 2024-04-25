package com.back.chatbot.persistance.repository;

import com.back.chatbot.persistance.entity.AppointmentEntity;
import com.back.chatbot.persistance.entity.TurnoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAppointmentRepository extends JpaRepository<AppointmentEntity, String> {

    @Query(value = "SELECT * FROM appointment_entity WHERE appointment_state = 'LIBRE'", nativeQuery = true)
    List<AppointmentEntity> findAllByState();




    //@Query(value = "SELECT * FROM comment_entity WHERE id_channel = :id ORDER BY local_date_time", nativeQuery = true)
    //List<CommentEntity> findAllComments(@Param("id") String idChannel);


}
