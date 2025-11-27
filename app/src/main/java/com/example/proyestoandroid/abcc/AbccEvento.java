package com.example.proyestoandroid.abcc;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyestoandroid.R;
import com.example.proyestoandroid.formulario.FieldAdapterItem;
import com.example.proyestoandroid.formulario.MensajesError;

import model.Evento;

public class AbccEvento extends AbccActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabla = Evento.class.getSimpleName().toLowerCase();
        cambioTarget = null;

        form.makeNewField(Evento.NOMBRE, "text", "Nombre del evento", "Nombre");
        form.makeNewField(Evento.FECHA_INICIO, "date", "", "Fecha de inicio");
        form.makeNewField(Evento.FECHA_FIN, "date", "", "Fecha de fin");
        form.makeNewField(Evento.TIPO, "spinner", "", "Tipo de evento")
                .setOptionsAsSpinner(FieldAdapterItem.fromHashMap(Evento.TIPOS));
        form.makeNewField(Evento.DESCRIPCION, "text", "Descripcion breve", "Descripcion");

        enableAutoSearch();
        makeConsultaForm();
    }
    @Override
    public void setMensajesError() {
        errores.presetSerial(new Object[]{
                MensajesError.WRONG_TYPE, "Debe ser un año",
                MensajesError.REGEX_FAIL, "Debe ser un año válido",
        });
    }

    @Override
    public void showAgregarSuccess() {
        showToast("Evento agregado", Toast.LENGTH_SHORT);
    }
}