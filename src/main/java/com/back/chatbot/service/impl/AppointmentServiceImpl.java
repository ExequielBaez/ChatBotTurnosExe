package com.back.chatbot.service.impl;

import com.back.chatbot.controller.dto.request.AppointmentRequestDTO;
import com.back.chatbot.controller.dto.response.TurnoResponseDTO;
import com.back.chatbot.persistance.entity.AppointmentEntity;
import com.back.chatbot.persistance.entity.TurnoEntity;
import com.back.chatbot.persistance.repository.IAppointmentRepository;
import com.back.chatbot.persistance.repository.ITurnosRepository;
import com.back.chatbot.service.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements IAppointmentService {

    @Autowired
    private ITurnosRepository turnosRepository;
    @Autowired
    private IAppointmentRepository appointmentRepository;

    @Override
    public AppointmentEntity createAppointment(AppointmentEntity appointmentEntity) {
        return appointmentRepository.save(appointmentEntity);
    }

    @Override
    public List<AppointmentEntity> findAll() {
        return appointmentRepository.findAllByState();
    }


    @Override
    public List<TurnoResponseDTO> getTurnos() {
        List<TurnoEntity> turnos = turnosRepository.findAllTurnos();
        List<TurnoResponseDTO> listaTurnosEnBD = new ArrayList<>();
        List<TurnoResponseDTO> listaTurnosResponseDTO = new ArrayList<>();
        List<String> horariosPosibles = List.of("08", "09", "10", "11", "12", "13", "14", "15", "16", "17");
        //lista anidada, tiene la fecha y los turnos reservados obtenidos de la BD
        List<List<String>> listaHorariosPorFecha = new ArrayList<>();

        //recorro los turnos para obtener los horarios reservados que estan en la BD y separo por fecha
        for (TurnoEntity t : turnos) {
            TurnoResponseDTO turnoDTO = new TurnoResponseDTO();
            LocalDateTime data = t.getFechaTurno();

            String fecha = data.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String dia = data.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
            String hora = data.format(DateTimeFormatter.ofPattern("HH"));

            turnoDTO.setFecha(fecha);
            turnoDTO.setDia(dia);
            turnoDTO.setHora(hora);            
            turnoDTO.setEstado("Reservado");

            listaTurnosEnBD.add(turnoDTO);

            //ahora identifico la fecha
            int index = -1;
            for (int i = 0; i < listaHorariosPorFecha.size(); i++) {
                if (listaHorariosPorFecha.get(i).get(0).equals(fecha)) {
                    index = i;
                    break;
                }//si no se cumple el if no hace nada, si hay mas listas en la lista las analiza de nuevo
            }

            if (index == -1) {
                List<String> listaAnidada = new ArrayList<>();
                listaAnidada.add(fecha);
                listaAnidada.add(dia);
                listaAnidada.add(hora);
                listaHorariosPorFecha.add(listaAnidada);
            } else {
                listaHorariosPorFecha.get(index).add(hora);
            }
        }

            //listaTurnosResponseDTO.add(MyFormatter.formatTurno(t));

        //Recorro cada lista (fecha, hora, hora, hora) para completar los faltantes
        for (List<String> horariosPorFecha : listaHorariosPorFecha) {
            String fecha = horariosPorFecha.get(0);//la primer posicion siempre es la fecha
            List<String> listHorasReservadas = horariosPorFecha.subList(2, horariosPorFecha.size());

            for (String horario : horariosPosibles) {
                if (!listHorasReservadas.contains(horario)) {
                    TurnoResponseDTO turnoDTO = new TurnoResponseDTO();
                    turnoDTO.setEstado("Disponible");
                    turnoDTO.setFecha(fecha);
                    turnoDTO.setDia(horariosPorFecha.get(1));//en la segunda posicion esta el dia
                    turnoDTO.setHora(horario);
                    listaTurnosResponseDTO.add(turnoDTO);
                }
            }
        }

        //agrego los turnos reservados a la lista final
        listaTurnosResponseDTO.addAll(listaTurnosEnBD.stream().filter(turno -> turno.getEstado().equals("Reservado")).collect(Collectors.toList()));

        return listaTurnosResponseDTO;

//        boolean cont;
//        for (int i = 0; i < listaTurnosEnBD.size(); i++) {
//            String fecha = listaTurnosEnBD.get(i).getFecha();
//
//            //String hora = listaTurnosResponseDTO.get(i).getHora();
//            //if(fecha.equals(listaTurnosEnBD.get(i + 1).getFecha()) && cont){
//            //do {
//               for (String horario : listaHorariosPosibles) {
//                    if (!listalistaAnidadaFechaHora.contains(horario)) {
//
//                        TurnoResponseDTO turnoDTO = new TurnoResponseDTO();
//                        turnoDTO.setEstado("Disponible");
//                        turnoDTO.setFecha(fecha);
//                        turnoDTO.setDia(listaTurnosEnBD.get(i).getDia());
//                        turnoDTO.setHora(horario);
//                        listaTurnosResponseDTO.add(turnoDTO);
//                    }
//                    else {
//                        listaTurnosResponseDTO.add(listaTurnosEnBD.get(i));
//                    }
//                }
//            }
//            //while (fecha.equals(listaTurnosEnBD.get(i + 1).getFecha()));
//        //}
//
//        return listaTurnosResponseDTO;
    }


    @Override
    public List<TurnoResponseDTO> getAppByNextDay(String date, String moment) {

        LocalDate myDate = LocalDate.parse(date);
        //String fecha = myDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        //String dia = myDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());

        List<TurnoEntity> turnos = turnosRepository.findAllDaysByData(myDate);
        List<TurnoResponseDTO> listaTurnosEnBD = new ArrayList<>();
        List<TurnoResponseDTO> listaTurnosResponseDTO = new ArrayList<>();
        List<String> horariosPosibles = List.of("08", "09", "10", "11", "12", "13", "14", "15", "16", "17");
        //lista anidada, tiene la fecha y los turnos reservados obtenidos de la BD
        List<List<String>> listaHorariosPorFecha = new ArrayList<>();

        //recorro los turnos para obtener los horarios reservados que estan en la BD y separo por fecha
        for (TurnoEntity t : turnos) {
            TurnoResponseDTO turnoDTO = new TurnoResponseDTO();
            LocalDateTime data = t.getFechaTurno();

            String fecha = data.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String dia = data.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
            String hora = data.format(DateTimeFormatter.ofPattern("HH"));

            turnoDTO.setFecha(fecha);
            turnoDTO.setDia(dia);
            turnoDTO.setHora(hora);
            turnoDTO.setEstado("Reservado");

            listaTurnosEnBD.add(turnoDTO);

            //ahora identifico la fecha
            int index = -1;
            for (int i = 0; i < listaHorariosPorFecha.size(); i++) {
                if (listaHorariosPorFecha.get(i).get(0).equals(fecha)) {
                    index = i;
                    break;
                }//si no se cumple el if no hace nada, si hay mas listas en la lista las analiza de nuevo
            }

            if (index == -1) {
                List<String> listaAnidada = new ArrayList<>();
                listaAnidada.add(fecha);
                listaAnidada.add(dia);
                listaAnidada.add(hora);
                listaHorariosPorFecha.add(listaAnidada);
            } else {
                listaHorariosPorFecha.get(index).add(hora);
            }
        }

        //listaTurnosResponseDTO.add(MyFormatter.formatTurno(t));

        //Recorro cada lista (fecha, dia, hora, hora, hora) para completar los faltantes
        //Ejempo lista (19-03-2024, Lunes, 08, 09, 14, 16) para completar los faltantes
        for (List<String> horariosPorFecha : listaHorariosPorFecha) {
            String listDate = horariosPorFecha.get(0);//la primer posicion siempre es la fecha
            List<String> listHorasReservadas = horariosPorFecha.subList(2, horariosPorFecha.size());

            for (String horario : horariosPosibles) {
                if (!listHorasReservadas.contains(horario)) {
                    TurnoResponseDTO turnoDTO = new TurnoResponseDTO();
                    turnoDTO.setEstado("Disponible");
                    turnoDTO.setFecha(listDate);
                    turnoDTO.setDia(horariosPorFecha.get(1));//en la segunda posicion esta el dia
                    turnoDTO.setHora(horario);
                    listaTurnosResponseDTO.add(turnoDTO);
                }
            }
        }

        //agrego los turnos reservados a la lista final
        //listaTurnosResponseDTO.addAll(listaTurnosEnBD.stream().filter(turno -> turno.getEstado().equals("Reservado")).collect(Collectors.toList()));
//todo si paginamos la respuesta o conultamos a la lista los primeros "5" disponibles para devolver.
        return listaTurnosResponseDTO;

//        boolean cont;
//        for (int i = 0; i < listaTurnosEnBD.size(); i++) {
//            String fecha = listaTurnosEnBD.get(i).getFecha();
//
//            //String hora = listaTurnosResponseDTO.get(i).getHora();
//            //if(fecha.equals(listaTurnosEnBD.get(i + 1).getFecha()) && cont){
//            //do {
//               for (String horario : listaHorariosPosibles) {
//                    if (!listalistaAnidadaFechaHora.contains(horario)) {
//
//                        TurnoResponseDTO turnoDTO = new TurnoResponseDTO();
//                        turnoDTO.setEstado("Disponible");
//                        turnoDTO.setFecha(fecha);
//                        turnoDTO.setDia(listaTurnosEnBD.get(i).getDia());
//                        turnoDTO.setHora(horario);
//                        listaTurnosResponseDTO.add(turnoDTO);
//                    }
//                    else {
//                        listaTurnosResponseDTO.add(listaTurnosEnBD.get(i));
//                    }
//                }
//            }
//            //while (fecha.equals(listaTurnosEnBD.get(i + 1).getFecha()));
//        //}
//
//        return listaTurnosResponseDTO;
    }



    @Override
    public List<TurnoResponseDTO> getAppByDate(String date, String moment) {

        LocalDate myDate = LocalDate.parse(date);
        String fecha = myDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String dia = myDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        //String moment = ap.getMoment();

        //todo no solo que complete los que no existen (DISPONIBLES) sino aquellos que sean
        //todo mayores o menores al momento (mañana o tarde) requerido
        //todo ejemplo si es tarde deberian ser los disponibles de las 13 en adelante
        //todo devolver esa lista
        //todo lista de turnos 08 reservddo 09 disponible  10 11 12 13 14 15 16

        List<TurnoEntity> listA = turnosRepository.findOneDayByData(myDate);
        List<TurnoResponseDTO> listaTurnosEnBD = new ArrayList<>();
        List<TurnoResponseDTO> listaTurnosResponseDTO = new ArrayList<>();
        List<TurnoResponseDTO> listAppPM = new ArrayList<>();
        List<TurnoResponseDTO> listAppAM = new ArrayList<>();
        List<String> horariosReservados = new ArrayList<>();
        List<String> horariosPosibles = List.of("08", "09", "10", "11", "12", "13", "14", "15", "16", "17");

        for (TurnoEntity t : listA) {
            TurnoResponseDTO turnoDTO = new TurnoResponseDTO();
            LocalDateTime data = t.getFechaTurno();
            //
            //String fecha = data.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            //String dia = data.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
            String hora = data.format(DateTimeFormatter.ofPattern("HH"));

            turnoDTO.setFecha(fecha);
            turnoDTO.setDia(dia);
            turnoDTO.setHora(hora);
            turnoDTO.setEstado("Reservado");

            listaTurnosEnBD.add(turnoDTO);

        }

        for (int i = 0; i < listaTurnosEnBD.size(); i++) {
                String hora = listaTurnosEnBD.get(i).getHora();
                horariosReservados.add(hora);
            }

        for (String horario : horariosPosibles) {
                if (!horariosReservados.contains(horario)) {
                    //String fecha = listaTurnosEnBD.get(0).getFecha();
                    //String dia = listaTurnosEnBD.get(0).getDia();
                    TurnoResponseDTO turnoDTO = new TurnoResponseDTO();
                    turnoDTO.setEstado("Disponible");
                    turnoDTO.setFecha(fecha);
                    turnoDTO.setDia(dia);
                    turnoDTO.setHora(horario);
                    listaTurnosResponseDTO.add(turnoDTO);
                    }
            }

        if(!moment.isEmpty()) {
            for (TurnoResponseDTO turno : listaTurnosResponseDTO) {
                int time = Integer.parseInt(turno.getHora());
                if (time <= 12)
                    listAppAM.add(turno); //intente remover el turno pero me da error al tocar la lista que estoy recorriendo
                else listAppPM.add(turno);
            }
            if (moment.equals("tarde")) return listAppPM;
            else return listAppAM;
        }
    return listaTurnosResponseDTO;
    }
}
