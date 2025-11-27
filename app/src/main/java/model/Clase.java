package model;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Clase extends Model{
    public static final String ID = "id";
    public static final String ANIO_GRADUACION = "anio_graduacion";

    public Clase(LinkedHashMap<String, Object> datos){super(datos);}

    public Clase(ArrayList<Object> datos) {
        super(datos);
    }

    public Clase(JSONObject datos) {
        super(datos);
    }
    protected Clase(){}

    @Override
    public void setRules() {
        addRule(ID, new DataRow("int", "INT", true, true, false, -1, -1, true, ""));
        addRule(ANIO_GRADUACION, new DataRow("string", "STRING", true, false, false, -1, -1, false, ""));
    }

    @NonNull
    @Override
    public String toString() {
        return "Clase del " + read(ANIO_GRADUACION);
    }
}
