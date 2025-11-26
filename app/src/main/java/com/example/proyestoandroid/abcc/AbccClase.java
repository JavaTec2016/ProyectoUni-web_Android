package com.example.proyestoandroid.abcc;
import android.os.Bundle;

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
    }
}