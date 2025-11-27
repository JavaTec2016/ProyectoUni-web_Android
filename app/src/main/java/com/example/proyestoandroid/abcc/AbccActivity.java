package com.example.proyestoandroid.abcc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyestoandroid.CustomAdapter;
import com.example.proyestoandroid.R;
import com.example.proyestoandroid.formulario.FieldAdapterItem;
import com.example.proyestoandroid.formulario.FieldWrapper;
import com.example.proyestoandroid.formulario.FormWrapper;
import com.example.proyestoandroid.formulario.MensajesError;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import controller.ABCC;
import controller.JSONParser;
import model.Model;

public class AbccActivity extends AppCompatActivity {
    ScrollView formContainer;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Model> registros;
    FormWrapper form;
    MensajesError errores = null;
    HashMap<String, MensajesError> erroresCampos = new HashMap<>();
    protected boolean enConsulta = false;
    protected String tabla = null;
    protected Class<? extends CambioActivity> cambioTarget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_abcc);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        registros = new ArrayList<>();
        formContainer = findViewById(R.id.formScrollView);
        form = new FormWrapper(formContainer);
        recyclerView = findViewById(R.id.recyclerResult);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(layoutManager);

        errores = new MensajesError();
        setMensajesError();
    }
    public void setRegistros(ArrayList<Model> mds){
        registros.clear();
        registros.addAll(mds);
    }
    public void setRecycler(ArrayList<Model> mds){
        AbccActivity hook = this;
        setRegistros(mds);
        registros.forEach(reg->{
            String k = reg.getProto().proto_getCampoPrimario();
            Log.i("Model", k + " => " + reg.read(k));
        });
        runOnUiThread(() -> {
            adapter = new CustomAdapter(registros, hook);
            recyclerView.setAdapter(adapter);
        });
    }
    public void displayValidacion(LinkedHashMap<String, Object> validacion){
        if(validacion==null) {
            form.clearAllLabelErrors();
            showAgregarSuccess();
            return;
        }
        validacion.forEach((id, codigo)->{
            int numCodigo = Integer.parseInt(String.valueOf(codigo));
            if(numCodigo == MensajesError.OK) { form.clearLabelError(id); }
            else if(numCodigo == MensajesError.NULL_DATA){form.markLabelError(id, "*Campo requerido");}
            else form.markLabelError(id, getMensajeFor(id, numCodigo));
        });
    }
    public String getMensajeFor(String id, int codigo){
        return erroresCampos.get(id).getMensaje(codigo);
    }
    public void showAgregarSuccess(){
        showToast("Registro agregado", Toast.LENGTH_SHORT);
    }
    public void showEliminarFail(){
        showToast("Este registro no se puede eliminar", Toast.LENGTH_SHORT);
    }
    public void agregar(View v){
        LinkedHashMap<String, Object> datos = form.getFormData();
        ABCC proto = new ABCC(this);
        new Thread(()->{
            try {
                LinkedHashMap<String, Object> validacion = proto.makeAlta(tabla, datos);
                displayValidacion(validacion);
            } catch (Exception e) {
                showToast("Error de conexion, revise el intenet", Toast.LENGTH_LONG);
                Log.i("ABCC_alta", "Error: ", e);
            }
        }).start();
    }
    public void makeConsultaForm(){
        if(enConsulta) return;
        LinkedHashMap<String, Object> datos = form.getFormData();
        ABCC proto = new ABCC(this);
        enConsulta = true;

        new Thread(() -> {
            try {
                setRecycler(proto.makeConsulta(tabla, datos));

            } catch (Exception e) {
               showToast("Error de conexion, revise el intenet", Toast.LENGTH_LONG);
               Log.i("ABCC_consulta", "Error: ", e);
            }
            enConsulta = false;
        }).start();
    }
    public void setMensajesFor(String id, Object[] seriales){
        erroresCampos.put(id, new MensajesError(seriales));
    }
    public void limpiarForm(View v){
        form.clearForm();
    }
    protected void enableAutoSearch(){
        form.onFieldsChange((wrapper, fieldId) -> {
            makeConsultaForm();
            Log.i("FIELDCHANGE", wrapper.id + " tiene " + wrapper.getValue());
        });
    }
    public boolean getEnConsulta(){return enConsulta;}
    public void setMensajesError(){}
    public void showToast(String text, int length){
        AbccActivity hook = this;
        runOnUiThread(() -> Toast.makeText(hook, text, length).show());
    }

    public void eliminar(String id){
        ABCC proto = new ABCC(this);

        new Thread(()->{
            try {
                Log.i("REGISTRO GG2", "BAJA de" + id);
                boolean b = proto.makeBaja(tabla, Integer.parseInt(id));
                if(!b) showToast("No se pudo eliminar el registro", Toast.LENGTH_LONG);
                else showToast("Registro eliminado", Toast.LENGTH_SHORT);

                makeConsultaForm();
            } catch (Exception e) {
                showEliminarFail();
                //throw new RuntimeException(e);
            }
        }).start();

    }
    public void editarIntent(String id, Model selectedModel) throws JSONException {
        Intent i = new Intent(this, cambioTarget);
        Log.i("REGISTRO GG2", "Edicion de" + id);
        i.putExtra("OLD_id", id);
        i.putExtra("modelData", JSONParser.encode(selectedModel.getDataMap()));
        startActivity(i);
    }
    public void detallesIntent(Model model){
        Intent i = new Intent(this, null);
        Log.i("REGISTRO GG2", "Detalles de modelo");
        model.getDataMap().forEach((key, val)->{
            i.putExtra(key, String.valueOf(val));
        });
    }
}