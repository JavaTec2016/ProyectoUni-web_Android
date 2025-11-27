package com.example.proyestoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyestoandroid.abcc.AbccClase;
import com.example.proyestoandroid.abcc.AbccCorporacion;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void irA(View v){
        Intent i = null;
        if(v.getId() == R.id.btnClases){
            i = new Intent(this, AbccClase.class);
        }else if(v.getId() == R.id.btnCorporaciones){
            i = new Intent(this, AbccCorporacion.class);
        }else if(v.getId() == R.id.btnDonadores){
            return;
            //i = new Intent(this, AbccClase.class);
        }
        startActivity(i);
    }
}