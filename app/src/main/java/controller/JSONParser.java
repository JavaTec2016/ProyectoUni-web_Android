package controller;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class JSONParser {
    private InputStream is = null;
    private OutputStream os = null;
    private JSONObject jsonObject = null;
    private HttpURLConnection conexion = null;
    private URL url;

    //codigo PETICION HTTP (request)

    public void sendJSON(String targetUrl, String method, String json) throws IOException {
        url = new URL(targetUrl);
        conexion = (HttpURLConnection) url.openConnection();
        //indicar el rol de la conexion
        conexion.setDoOutput(true);
        //indicar metodo
        conexion.setRequestMethod(method);
        //indicar tamanio fijo de la cadena
        conexion.setFixedLengthStreamingMode(json.length());
        //establecer formato de comunicacion
        conexion.setRequestProperty("Content-Type", "application/x-www.form-urlencoded");
        //preparar envio del json
        os = new BufferedOutputStream(conexion.getOutputStream());
        Log.i("ENVIO", json);
        os.write(json.getBytes());
        os.flush();
        os.close();
    }
    public String mapParams(LinkedHashMap<String, Object> params){
        ArrayList<String> urlParams = new ArrayList<>();
        //convierte el body a url
        params.forEach((key, val)->{
            try {
                urlParams.add(
                        URLEncoder.encode(key, "UTF-8")+"="+URLEncoder.encode(String.valueOf(val), "UTF-8")
                );
            } catch (UnsupportedEncodingException e) {
                Log.i("URL PARAMS", "caracteres invalidos ",new RuntimeException(e));
            }
        });

        return String.join("&", urlParams);
    }
    public void sendJSONGET(String targetUrl, LinkedHashMap<String, Object> params) throws IOException {
        String urlParams = targetUrl+"?"+mapParams(params);
        url = new URL(urlParams);
        conexion = (HttpURLConnection) url.openConnection();
        //indicar el rol de la conexion
        conexion.setDoInput(true);
        //indicar metodo
        conexion.setRequestMethod("GET");
        Log.i("ENVIO GET", urlParams);
    }
    public String recieve() throws IOException {
        is = new BufferedInputStream(conexion.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder res = new StringBuilder();
        String k;
        while ((k = reader.readLine()) != null){
            res.append(k).append("\n");
        }
        Log.i("RECIBO", res.toString());

        is.close();
        return String.valueOf(res);
    }
    public JSONObject request(String targetUrl, String method, LinkedHashMap<String, Object> datos){
        //peticion para ALTAS
        String json = new JSONObject(datos).toString();
        Log.i("JSONoutput->", json);
        ///---------PETICION (request)
        try {
            if(method.equals("GET")) sendJSONGET(targetUrl, datos);
            else sendJSON(targetUrl, method, json);
        } catch (MalformedURLException e) {
            Log.i("REQUEST->", "ERROR: URL malformada: ", e);
        } catch (IOException e) {
            Log.i("REQUEST->", "ERROR: error de comunicacion", e);
        }
        //----------RESPUESTA (resposne)

        try {
            String j = recieve();
            jsonObject = new JSONObject(j);
        } catch (IOException e) {
            Log.i("RESPONSE->", "ERROR: error de comunicacion", new IOException(e));
            throw new RuntimeException(e);
        } catch (JSONException e) {
            Log.i("RESPONSE->", "ERROR: JSON invalido");
            throw new RuntimeException(e);
        }
        return jsonObject;
    }

    public JSONObject requestConsulta(String tabla, String targetUrl, String method, String llaveFiltro, Object valorFiltro){
        /**
         * tood igual
         * copia desde inicio hasta el return (absolutamente lo mismo)
         * manda el filtro como un strigngn json igual
         * un paso extra, no siempre no
         * pero manejo un jsonArray tonces no se puede
         */

        LinkedHashMap<String, Object> datos = new LinkedHashMap<>();
        datos.put("tabla", tabla);
        if(llaveFiltro!= null) datos.put(llaveFiltro, valorFiltro);
        return request(targetUrl, method, datos);
    }
    public JSONObject requestConsulta(String tabla, String targetUrl, String method, LinkedHashMap<String, Object> datos){
        /**
         * tood igual
         * copia desde inicio hasta el return (absolutamente lo mismo)
         * manda el filtro como un strigngn json igual
         * un paso extra, no siempre no
         * pero manejo un jsonArray tonces no se puede
         */
        datos.put("tabla", tabla);
        return request(targetUrl, method, datos);
    }

    /**
     * Convierte un JSONObject simple a LinkedHashMap, no contempla arreglos
     * @param obj json a convertir
     * @return hashmap con valores en orden de insercion
     */
    public LinkedHashMap<String, Object> toHashMap(JSONObject obj){
        LinkedHashMap<String, Object> vlas = new LinkedHashMap<>();
        obj.keys().forEachRemaining(key ->{
            try {
                vlas.put(key, obj.get(key));
            } catch (JSONException e) {
                Log.i("JSONParser", "Error al convertir el JSON: ", e);
            }
        });
        return vlas;
    }
}
