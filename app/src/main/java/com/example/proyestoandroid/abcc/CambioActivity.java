package com.example.proyestoandroid.abcc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyestoandroid.R;
import com.example.proyestoandroid.formulario.FormWrapper;
import com.example.proyestoandroid.formulario.MensajesError;

import org.json.JSONException;

import java.util.LinkedHashMap;

import controller.ABCC;
import controller.JSONParser;
import model.Model;

public class CambioActivity extends AppCompatActivity {
    ScrollView formContainer;
    FormWrapper form;
    MensajesError errores = null;
    protected String tabla = null;
    protected String oldId = null;
    protected LinkedHashMap originalData = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cambio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        formContainer = findViewById(R.id.formScrollView);
        form = new FormWrapper(formContainer);
        errores = new MensajesError();
        setMensajesError();
        try {
            buildModel();
        } catch (JSONException e) {
            showToast("Error al autorrellenar el formulario", Toast.LENGTH_LONG);
            Log.i("REGISTRO EDIT", "Error DECODE", e);
        }

    }
    public void limpiar(View v){
        form.clearForm();
    }
    public void editar(View v){
        LinkedHashMap<String, Object> newData = form.getFormData();
        ABCC proto = new ABCC(this);
        new Thread(()->{
            try {
                LinkedHashMap<String, Object> vald = proto.makeCambio(tabla, Integer.parseInt(oldId), newData);
                displayValidacion(vald);
            } catch (Exception e) {
                showEditarrFail();
                Log.i("ABCC_Editar", "Error: ", e);
            }
        }).start();

    }
    public void autoFill(){
        form.setFormData(originalData);
    }
    public void buildModel() throws JSONException {
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        oldId = extras.getString("OLD_id");
        originalData = JSONParser.decode(extras.getSerializable("modelData", String.class));
    }
    public void displayValidacion(LinkedHashMap<String, Object> validacion){
        if(validacion==null) {
            showEditarrSuccess();
            return;
        }
        validacion.forEach((id, codigo)->{
            int numCodigo = Integer.parseInt(String.valueOf(codigo));
            if(numCodigo == MensajesError.OK) { form.clearLabelError(id); }
            else if(numCodigo == MensajesError.NULL_DATA){form.markLabelError(id, "*Campo requerido");}
            else form.markLabelError(id, errores.getMensaje(numCodigo));
        });
    }
    public void showToast(String text, int length){
        CambioActivity hook = this;
        runOnUiThread(() -> Toast.makeText(hook, text, length).show());
    }
    public void showEditarrFail(){
        showToast("Este registro no se puede eliminar", Toast.LENGTH_SHORT);
    }
    public void showEditarrSuccess(){
        showToast("Registro modificado", Toast.LENGTH_SHORT);
    }
    public void setMensajesError(){}

}