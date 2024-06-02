package com.example.parcial2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditarPerfil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

    }








    public void CambiarUsuario(View view) {
        // Lista de usuarios
        final String[] usuarios = {"Usuario 1", "Usuario 2"};

        // Crear el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona un usuario")
                .setItems(usuarios, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Acción al seleccionar un usuario
                        Toast.makeText(EditarPerfil.this, "Seleccionaste: " + usuarios[which], Toast.LENGTH_SHORT).show();
                    }
                });


        builder.create().show();
    }
}