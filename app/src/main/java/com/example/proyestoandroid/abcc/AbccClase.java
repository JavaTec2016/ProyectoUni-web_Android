package com.example.proyestoandroid.abcc;
import android.os.Bundle;
import android.widget.Toast;

import com.example.proyestoandroid.formulario.MensajesError;

import model.Clase;
import model.Models;

public class AbccClase extends AbccActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Clase prototype = (Clase) Models.getModels().fromClass(Clase.class);

        tabla = prototype.getName();
        form.makeNewField(Clase.ANIO_GRADUACION, "number", "Ej. 2020", "Año de graduación");

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
        showToast("Clase registrada", Toast.LENGTH_SHORT);
    }
}