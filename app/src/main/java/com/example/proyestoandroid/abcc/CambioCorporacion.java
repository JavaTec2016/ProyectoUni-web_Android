package com.example.proyestoandroid.abcc;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyestoandroid.R;
import com.example.proyestoandroid.formulario.MensajesError;

import model.Clase;
import model.Corporacion;

public class CambioCorporacion extends CambioActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabla = Corporacion.class.getSimpleName().toLowerCase();
        form.makeNewField(Corporacion.ID, "number", "", "ID").setReadOnly(true);
        form.makeNewField(Corporacion.NOMBRE, "text", "Nombre", "Nombre de la corporación");
        form.makeNewField(Corporacion.DIRECCION, "text", "Direccion", "Dirección");
        form.makeNewField(Corporacion.TELEFONO, "phone", "Numero de telefono", "Teléfono");

        autoFill();
    }
    @Override
    public void setMensajesError() {
        setMensajesFor(Corporacion.NOMBRE, new Object[]{
                MensajesError.WRONG_TYPE, "",
                MensajesError.DATA_TOO_BIG, "No debe exceder 100 caracteres",
                MensajesError.REGEX_FAIL, "Solo se permiten letras, numeros y espacios",
        });
        setMensajesFor(Corporacion.DIRECCION, new Object[]{
                MensajesError.WRONG_TYPE, "",
                MensajesError.DATA_TOO_BIG, "No debe exceder 200 caracteres",
        });
        setMensajesFor(Corporacion.TELEFONO, new Object[]{
                MensajesError.WRONG_TYPE, "",
                MensajesError.DATA_TOO_BIG, "No debe exceder 10 caracteres",
                MensajesError.REGEX_FAIL, "Debe ser un número telefónico válido",
        });
    }
}