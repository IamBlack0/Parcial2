package com.example.parcial2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

    }
    public void ConfiguracionUsuario(View view) {
        // Crear el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Configuración de Usuario");

        // Opciones del AlertDialog
        String[] opciones = {"Editar Perfil", "Agregar Contactos", "Cambiar Usuario"};
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Acción para Editar Perfil
                        Toast.makeText(MainActivity.this, "Editar Perfil seleccionado", Toast.LENGTH_SHORT).show();
                        editarPerfil();
                        break;
                    case 1:
                        // Acción para Agregar Contactos
                        Toast.makeText(MainActivity.this, "Agregar Contactos seleccionado", Toast.LENGTH_SHORT).show();
                        agregarContactos();
                        break;
                    case 2:
                        // Acción para Cambiar Usuario
                        Toast.makeText(MainActivity.this, "Cambiar Usuario seleccionado", Toast.LENGTH_SHORT).show();
                        cambiarUsuario();
                        break;
                }
            }
        });

        // Mostrar el AlertDialog
        builder.show();
    }

    private void editarPerfil() {
        Intent i = new Intent(this, EditarPerfil.class);
        startActivity(i);
        // Implementa la lógica para editar el perfil
    }

    private void agregarContactos() {
        // Implementa la lógica para agregar contactos
    }

    private void cambiarUsuario() {
        // Implementa la lógica para cambiar de usuario
    }

}

