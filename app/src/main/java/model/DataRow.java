package model;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class DataRow extends LinkedHashMap<String, Object>{
    public static final String TIPO = "tipo";
    public static final String TIPO_SQL = "tipoSQL";
    public static final String NO_NULO = "nonulo";
    public static final String PRIMARIA = "primaria";
    public static final String FORANEA = "foranea";
    public static final String UMBRAL = "umbral";
    public static final String LIMITE = "limite";
    public static final String OCULTO = "oculto";
    public static final String REGEX = "regex";

    public DataRow(){
        super();
    }
    public DataRow(LinkedHashMap<String, Object> datos){
        this.putAll(datos);
    }
    public DataRow(String tipo, String tipoSQL, boolean nonulo, boolean primaria, boolean foranea, int umbral, int limite, boolean oculto, String regex){
        super();
        put(TIPO, tipo);
        put(TIPO_SQL, tipoSQL);
        put(NO_NULO, nonulo);
        put(PRIMARIA, primaria);
        put(FORANEA, foranea);
        put(UMBRAL, umbral);
        put(LIMITE, limite);
        put(OCULTO, oculto);
        put(REGEX, regex);
    }
    public void init(){
        put(TIPO, null);
        put(TIPO_SQL, null);
        put(NO_NULO, null);
        put(PRIMARIA, null);
        put(FORANEA, null);
        put(UMBRAL, null);
        put(LIMITE, null);
        put(OCULTO, null);
        put(REGEX, null);
    }

    /**
     * copia los datos del mapa cuyas llaves correspondan a las llaves propias
     * @param datos
     */
    public void fillUnordered(HashMap<String, Object> datos){
        forEach((key, nulo)->{
            if(!containsKey(key)) return;
            put(key, datos.get(key));
        });
    }
}
