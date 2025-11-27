package com.example.proyestoandroid.abcc;

import android.os.Bundle;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyestoandroid.R;
import com.example.proyestoandroid.formulario.FormWrapper;
import com.example.proyestoandroid.formulario.MensajesError;

import model.Clase;
import model.Model;

public class CambioClase extends CambioActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabla = Clase.class.getSimpleName().toLowerCase();
        form.makeNewField(Clase.ID, "number", "", "ID").setReadOnly(true);
        form.makeNewField(Clase.ANIO_GRADUACION, "number", "Ej. 2020", "Año de graduación");

        autoFill();
    }
    @Override
    public void setMensajesError() {
        setMensajesFor(Clase.ANIO_GRADUACION, new Object[]{
                MensajesError.WRONG_TYPE, "Debe ser un año",
                MensajesError.REGEX_FAIL, "Debe ser un año válido",
        });
    }
}