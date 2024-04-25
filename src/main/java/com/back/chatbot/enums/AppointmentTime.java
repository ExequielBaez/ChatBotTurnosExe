package com.back.chatbot.enums;

public enum AppointmentTime {
    OCHO("8:00"),
    NUEVE("9:00"),
    DIEZ("10:00"),
    ONCE("11:00"),
    DOCE("12:00");

    private final String value;

    AppointmentTime(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
