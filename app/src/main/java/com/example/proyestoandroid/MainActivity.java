package com.example.proyestoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyestoandroid.abcc.AbccActivity;
import com.example.proyestoandroid.abcc.AbccClase;

import java.util.LinkedHashMap;

import controller.ABCC;
import model.ModelLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ModelLoader.loadAll();
    }

    public void irABCC(View v){
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
    }

    public void autenticar(View v){
        LinkedHashMap<String, Object> datos = new LinkedHashMap<>();
        String user = ((EditText)findViewById(R.id.caja_usuario)).getText().toString();
        String pass = ((EditText)findViewById(R.id.caja_pass)).getText().toString();

        datos.put("usuario", user);
        datos.put("pass", pass);

        new Thread(()->{
            ABCC con = new ABCC(this);
            try {
                String rol = con.getLogin(datos);
                if(rol == null) {
                    runOnUiThread(()->{
                        Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
                    });
                    return;
                };
                runOnUiThread(()->{
                    Toast.makeText(this, "Bienvenido", Toast.LENGTH_LONG).show();
                });
                irABCC(v);
            } catch (Exception e) {
                runOnUiThread(()->{
                    Toast.makeText(this, "Error de conexion", Toast.LENGTH_SHORT).show();
                    Log.i("AUTH", "Error ", e);
                });

            }
        }).start();
    }
}