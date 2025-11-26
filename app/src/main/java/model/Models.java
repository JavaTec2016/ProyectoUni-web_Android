package model;

import android.util.Log;

import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Models extends HashMap<String, Model> {
    private static final Models models = new Models();
    private Models(){super();}

    @Nullable
    @Override
    public Model put(String key, Model value) {
        Model m = super.put(key, value);
        if(m == null) return null;
        m.setRules();
        return m;
    }

    public static Models getModels(){
        return models;
    }
    public LinkedHashMap<String, DataRow> getRulesOf(String nombre){
        return get(nombre).rules;
    }
    public LinkedHashMap<String, Object> getData(String nombre){
        return get(nombre).valores;
    }
    public LinkedHashMap<String, Object> getDataMap(String nombre){
        return get(nombre).getDataMap();
    }

    @Nullable
    @Override
    public Model putIfAbsent(String key, Model value) {
        value.setRules();
        return super.putIfAbsent(key, value);
    }

    public void addPrototype(Model m){
        putIfAbsent(m.getClass().getSimpleName(), m);
        m.setRules();
    }
    public Model fillPrototype(String nombre, LinkedHashMap<String, Object> datos){
        get(nombre).fill(datos);
        return get(nombre);
    }
    public Model fillPrototype(String nombre, ArrayList<Object> datos){
        get(nombre).fill(datos);
        return get(nombre);
    }
    public Model populatePrototype(String nombre, LinkedHashMap<String, Object> datos){
        get(nombre).populate(datos);
        return get(nombre);
    }
    public Model clearPrototype(String nombre){
        get(nombre).valores.clear();
        return get(nombre);
    }

    /**
     * Crea una instancia vacia del modelo y luego la llena con los datos dados
     * @param nombre nombre del modelo a crear
     * @param datos datos para popular el modelo
     * @return nuevo modelo
     */
    public Model newInstance(String nombre, LinkedHashMap<String, Object> datos){
        Constructor<? extends Model>[] cons = (Constructor<? extends Model>[]) get(nombre).getClass().getConstructors();
        Constructor<? extends Model> con = null;
        for (int i = 0; i < cons.length; i++) {
            if(cons[i].getParameterCount() == 0) {
                con = cons[i];
                break;
            }
        }
        if(con == null) return null;
        try {
            Model m = con.newInstance();
            m.populate(datos);
            return m;
        } catch (IllegalAccessException e) {
            Log.i("INSTANCER", "Constructor inaccesible para: '"+nombre+"'", new RuntimeException(e));
        } catch (InstantiationException e) {
            Log.i("INSTANCER", "La clase '"+nombre+"' no es instanciable", new RuntimeException(e));
        } catch (InvocationTargetException e) {
            Log.i("INSTANCER", "Error al instanciar: '"+nombre+"'", new RuntimeException(e));
        }
        return null;
    }
}
