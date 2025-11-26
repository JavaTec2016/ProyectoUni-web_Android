package model;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.BiPredicate;

public class Model {
    protected LinkedHashMap<String, Object> valores = new LinkedHashMap<>();
    protected LinkedHashMap<String, DataRow> rules = new LinkedHashMap<>();

    public String getName(){
        return this.getClass().getSimpleName().toLowerCase();
    }
    protected Model(){};
    public Model(LinkedHashMap<String, Object> datos){
        if(datos == null) return;
        fill(datos);
    }
    public Model(ArrayList<Object> datos){
        if(datos == null) return;
        fill(datos);
    }
    public Model(JSONObject datos){
        if(datos == null) return;
        fill(datos);
    }
    public void fill(LinkedHashMap<String, Object> datos){
        valores.putAll(datos);
    }
    public void fill(JSONObject json){
        LinkedHashMap<String, Object> vlas = new LinkedHashMap<>();
        json.keys().forEachRemaining(key ->{
            try {
                vlas.put(key, json.get(key));
            } catch (JSONException e) {
                Log.i("Model_Construct", "Error al leer JSON: ", e);
            }
        });
        fill(vlas);
    }
    public Model getProto(){
        return Models.getModels().get(getName());
    }
    public void populate(LinkedHashMap<String, Object> datos){
        Model proto = getProto();
        valores.clear();
        datos.forEach((key, val) -> {
            if(!proto.rules.containsKey(key)) return;
            valores.put(key, val);
        });
    }

    /**
     * rellena el modelo con datos de un arrayList, el modelo debe estar registrado en Models
     * @param datos arrayList con los datos
     */
    public void fill(ArrayList<Object> datos){
        int[] i = {0};
        Model proto = getProto();
        proto.rules.keySet().forEach(key -> {
            valores.put(key, datos.get(i[0]));
            i[0]++;
        });
    }
    public LinkedHashMap<String, Object> getDataMap(){
        LinkedHashMap<String, Object> out = new LinkedHashMap<>();
        out.put("tabla", getName());
        out.putAll(valores);
        return out;
    }
    public Object read(String campo){
        return valores.get(campo);
    }
    /**
     * Filtra los valores del modelo, retorna los valores cuya regla sea igual al valor dado
     * @param ruleNombre nombre de la regla a probar
     * @param ruleValor valor de la regla a probar
     * @return valores filtrados
     */
    public HashMap<String, Object> filterValues(String ruleNombre, Object ruleValor){
        LinkedHashMap<String, Object> out = new LinkedHashMap<>();
        LinkedHashMap<String, Object> ruleset = proto_aggregateRules(ruleNombre, null);
        valores.forEach((key, val)->{
            Object rule = ruleset.get(key);
            if(rule == null || rule.equals(ruleValor)){
                out.put(key, val);
            }
        });
        return out;
    }

    ///metodos del prototipo

    public void addRule(String nombre, DataRow rule){
        rules.put(nombre, rule);
    }
    public DataRow getRule(String nombre){
        return rules.get(nombre);
    }

    /**
     * Recopila los valores de una regla para todos los campos del modelo
     * @param nombre nombre de la regla
     * @param filtro funcion opcional para filtrar reglas
     * @return mapa con los campos y el valor de su respectiva regla
     */
    public LinkedHashMap<String, Object> proto_aggregateRules(String nombre, BiPredicate<String, Object> filtro){
        LinkedHashMap<String, Object> out = new LinkedHashMap<>();
        if(filtro == null){
            Log.i("Model Prototype", "agregando regla :" + nombre + " sin filtro");
            rules.forEach((key, row)->{
                Object dato = row.get(nombre);
                out.put(key, dato);
            });
        }else{
            rules.forEach((key, val)->{
                Object dato = val.get(nombre);
                if(filtro.test(key, dato)) out.put(key, dato);
            });
        }
        return out;
    }

    public String proto_getCampoPrimario(){
        LinkedHashMap<String, Object> prim = proto_aggregateRules(DataRow.PRIMARIA, (key, val)->{
            return val.equals(true);
        });
        if(prim.size() == 1) return prim.keySet().iterator().next();
        return null;
    }
    public void setRules(){};
    public void buildRules(LinkedHashMap<String, LinkedHashMap<String, Object>> ruleMap){
        rules.clear();
        ruleMap.forEach((ruleNombre, ruleData)->{
            addRule(ruleNombre, new DataRow(ruleData));
        });
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
