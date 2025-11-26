package com.example.proyestoandroid.abcc;

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

import java.util.ArrayList;
import java.util.LinkedHashMap;

import controller.ABCC;
import model.Model;

public class AbccActivity extends AppCompatActivity {
    ScrollView formContainer;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Model> registros;
    FormWrapper form;

    protected boolean enConsulta = false;
    protected String tabla = null;
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
    }
    public void setRegistros(ArrayList<Model> mds){
        registros.clear();
        registros.addAll(mds);
    }
    public void setRecycler(ArrayList<Model> mds){
        setRegistros(mds);
        registros.forEach(reg->{
            String k = reg.getProto().proto_getCampoPrimario();
            Log.i("Model", k + " => " + reg.read(k));
        });
        runOnUiThread(() -> {
            adapter = new CustomAdapter(registros);
            recyclerView.setAdapter(adapter);
        });

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
    protected void enableAutoSearch(){
        form.onFieldsChange((wrapper, fieldId) -> {
            makeConsultaForm();
            Log.i("FIELDCHANGE", wrapper.id + " tiene " + wrapper.getValue());
        });
    }
    public boolean getEnConsulta(){return enConsulta;}

    public void showToast(String text, int length){
        AbccActivity hook = this;
        runOnUiThread(() -> Toast.makeText(hook, text, length).show());
    }
}