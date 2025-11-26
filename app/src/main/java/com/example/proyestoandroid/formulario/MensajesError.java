package com.example.proyestoandroid.formulario;

import java.util.HashMap;

public class MensajesError {
    public static final int OK = 0;
    public static final int WRONG_TYPE = 1;
    public static final int DATA_TOO_SMALL = 2;
    public static final int DATA_TOO_BIG = 3;
    public static final int NULL_DATA = 4;
    public static final int WRONG_DATE = 5;
    public static final int REGEX_FAIL = 6;
    public static final int DATE_NEGATIVE = 7;

    public HashMap<Integer, String> mensajes = new HashMap<>();

    public MensajesError(HashMap<Integer, String> preset){
        if(preset == null) return;
        mensajes.putAll(preset);
    }
    public MensajesError(Object[] preset){
        if(preset == null) return;
        presetSerial(preset);
    }
    public MensajesError(){}
    public void addMensaje(Integer codigo, String msj){
        mensajes.put(codigo, msj);
    }
    public String getMensaje(Integer codigo){
        return mensajes.get(codigo);
    }
    public void presetSerial(Object[] data){
        for (int i = 0; i < data.length; i+=2) {
            mensajes.put((Integer)data[i], String.valueOf(data[i+1]));
        }
    }
}
