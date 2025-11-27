package model;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Corporacion extends Model{
    public static final String ID = "id";
    public static final String NOMBRE = "nombre";
    public static final String DIRECCION = "direccion";
    public static final String TELEFONO = "telefono";
    public static final String EMAIL = "email";

    public Corporacion(LinkedHashMap<String, Object> datos){super(datos);}

    public Corporacion(ArrayList<Object> datos) {
        super(datos);
    }
    public Corporacion(JSONObject datos) {
        super(datos);
    }
    protected Corporacion(){}
    public void setRules() {
        addRule(ID, new DataRow("int", "INT", true, true, false, -1, -1, true, ""));
        addRule(NOMBRE, new DataRow("string", "VARCHAR", true, false, false, 0, 100, false, ""));
        addRule(DIRECCION, new DataRow("date", "DATE", true, false, false, -1, -1, false, ""));
        addRule(TELEFONO, new DataRow("date", "DATE", false, false, false, -1, -1, false, ""));
        addRule(EMAIL, new DataRow("string", "VARCHAR", true, false, false, 0, 50, false, ""));
    }
}
