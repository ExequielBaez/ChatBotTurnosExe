package com.back.chatbot.utils;

import com.back.chatbot.controller.dto.response.TurnoResponseDTO;
import com.back.chatbot.persistance.entity.TurnoEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public final class MyFormatter {

    public static TurnoResponseDTO formatTurno(TurnoEntity t){
        TurnoResponseDTO turnoDTO = new TurnoResponseDTO();
        LocalDateTime data = t.getFechaTurno();


        String fecha = data.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String dia = data.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        String hora = data.format(DateTimeFormatter.ofPattern("HH"));

        turnoDTO.setFecha(fecha);
        turnoDTO.setDia(dia);
        turnoDTO.setHora(hora);
        //listaHorariosReservados.add(hora);
        turnoDTO.setEstado("Reservado");

        return turnoDTO;
    }

}
