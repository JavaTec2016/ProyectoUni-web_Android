package model;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Evento extends Model {
    public static final String ID = "id";
    public static final String NOMBRE = "nombre";
    public static final String FECHA_INICIO = "fecha_inicio";
    public static final String FECHA_FIN = "fecha_fin";
    public static final String TIPO = "tipo";
    public static final String DESCRIPCION ="descripcion";

    public static final HashMap<String, String> TIPOS = new HashMap<>();


    public Evento(LinkedHashMap<String, Object> datos){super(datos);}

    public Evento(ArrayList<Object> datos) {
        super(datos);
    }

    public Evento(JSONObject datos) {
        super(datos);
    }
    protected Evento(){}
    @Override
    public void setRules() {
        addRule(ID, new DataRow("int", "INT", true, true, false, -1, -1, true, ""));
        addRule(NOMBRE, new DataRow("string", "VARCHAR", true, false, false, 0, 100, false, ""));
        addRule(FECHA_INICIO, new DataRow("date", "DATE", true, false, false, -1, -1, false, ""));
        addRule(FECHA_FIN, new DataRow("date", "DATE", false, false, false, -1, -1, false, ""));
        addRule(TIPO, new DataRow("string", "VARCHAR", true, false, false, 0, 50, false, ""));
        addRule(DESCRIPCION, new DataRow("string", "TEXT", true, false, false, -1, -1, false, ""));

        TIPOS.putIfAbsent("Fonoton", "Fonoton");
        TIPOS.putIfAbsent("Graduacion", "Graduacion");
        TIPOS.putIfAbsent("Festival", "Festival");
    }
    @NonNull
    @Override
    public String toString() {
        return read(TIPO)+","+read(NOMBRE);
    }
}
