package controller;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import model.Model;
import model.Models;

public class ABCC {
    public static final String URLConsulta = "http://10.0.2.2:80/proyesto/backend/API/api_mysql_consultas.php";
    public static final String URLAltas = "http://10.0.2.2:80/proyesto/backend/API/api_mysql_altas.php";
    public static final String URLBajas = "http://10.0.2.2:80/proyesto/backend/API/api_mysql_bajas.php";
    public static final String URLCambios = "http://10.0.2.2:80/proyesto/backend/API/api_mysql_cambios.php";

    public static final String WIFI_OFF = "Wi-Fi inactivo";
    public static final String NO_INTERNET = "Error de conexion";

    JSONParser parser = null;
    ConnectivityManager cm = null;
    Network net = null;
    Activity source = null;
    public ABCC(Activity source) {
        setSource(source);
    }

    public void setSource(Activity source) {
        this.source = source;
    }

    private void prepareConnection() throws Exception {
        parser = new JSONParser();
        cm = (ConnectivityManager) source.getSystemService(Context.CONNECTIVITY_SERVICE);
        net = cm.getActiveNetwork();

        if(net == null){
            throw new Exception(NO_INTERNET);
        }
        if(cm.getNetworkCapabilities(net)==null){
            throw new Exception(WIFI_OFF);
        }
    }

    /**
     * Realiza una consulta a la BD remota con la tabla y los datos dados
     * @param tabla tabla a consultar
     * @param data datos para filtrar busqueda
     * @return lista de resultados
     * @throws Exception si no es posible realizar la conexion
     */
    public ArrayList<Model> makeConsulta(String tabla, LinkedHashMap<String, Object> data) throws Exception {
        prepareConnection();
        String method = "GET";
        ArrayList<Model> out = new ArrayList<>();
        JSONObject elobjeto = parser.requestConsulta(tabla, URLConsulta, method, data);

        try{
            JSONArray resultSet = elobjeto.getJSONArray("resultSet");
            int i = 0;

            for (; i < resultSet.length(); i++) {
                JSONObject modelData = resultSet.getJSONObject(i);
                Model m = Models.getModels().newInstance(tabla, modelData);
                out.add(m);
            }
            return out;
        }catch (Exception e){ throw e; }
    }
    /**
     * Realiza una alta en la BD remota con la tabla y los datos dados
     * @param tabla tabla a consultar
     * @param data datos para agregar
     * @return lista de codigos de validacion para cada campo
     * @throws Exception si no es posible realizar la conexion
     */
    public LinkedHashMap<String, Object> makeAlta(String tabla, LinkedHashMap<String, Object> data) throws Exception {
        prepareConnection();
        String method = "POST";

        LinkedHashMap<String, Object> fixed = new LinkedHashMap<>();
        fixed.put("tabla", tabla);
        fixed.putAll(data);

        JSONObject elobjeto = parser.request(URLAltas, method, fixed);

        try{

            boolean status = elobjeto.getBoolean("status");
            if(!status){
                return parser.toHashMap(elobjeto.getJSONObject("validation"));
            }else{
                return null;
            }

        }catch (Exception e){ throw e; }
    }
    /**
     * Realiza un cambio en la BD remota con la tabla y los datos dados
     * @param tabla tabla a consultar
     * @param data datos para cambiar el registro
     * @param oldId ID del registro a cambiar
     * @return lista de codigos de validacion para cada campo
     * @throws Exception si no es posible realizar la conexion
     */
    public LinkedHashMap<String, Object> makeCambio(String tabla, Integer oldId, LinkedHashMap<String, Object> data) throws Exception {
        prepareConnection();
        String method = "GET";

        LinkedHashMap<String, Object> fixed = new LinkedHashMap<>();
        fixed.put("tabla", tabla);
        fixed.put("OLD_id", oldId);
        fixed.putAll(data);

        JSONObject elobjeto = parser.request(URLCambios, method, fixed);

        try{

            boolean status = elobjeto.getBoolean("status");
            if(!status){
                return parser.toHashMap(elobjeto.getJSONObject("validation"));
            }else{
                return null;
            }

        }catch (Exception e){ throw e; }
    }
    /**
     * Realiza una baja en la BD remota con la tabla y los datos dados
     * @param tabla tabla a consultar
     * @param id ID del registro a eliminar
     * @return confirmacion de validacion
     * @throws Exception si no es wposible realizar la conexion
     */
    public boolean makeBaja(String tabla, Integer id) throws Exception {
        prepareConnection();
        String method = "GET";

        LinkedHashMap<String, Object> fixed = new LinkedHashMap<>();
        fixed.put("tabla", tabla);
        fixed.put("id", id);

        JSONObject elobjeto = parser.request(URLBajas, method, fixed);

        try{

            return elobjeto.getBoolean("status");

        }catch (Exception e){ throw e; }
    }
}
