package com.example.parcial2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView configIcon = findViewById(R.id.config_icon);
        configIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_contextual, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.EditarPerfil) {
                    editarPerfil();
                    return true;
                } else if (id == R.id.agregarContactos) {
                    agregarContactos();
                    return true;
                } else if (id == R.id.CambiarUsuario) {
                    cambiarUsuario();
                    return true;
                } else {
                    return false;
                }
            }
        });
        popupMenu.show();
    }

    public void editarPerfil() {
        Intent i = new Intent(this, EditarPerfil.class);
        startActivity(i);
    }

    public void agregarContactos() {
        Toast.makeText(this, "Agregar Contactos", Toast.LENGTH_SHORT).show();
    }

    public void cambiarUsuario() {
        Toast.makeText(this, "Cambiar Usuario", Toast.LENGTH_SHORT).show();
    }

    public void BuscarContactos(View view) {
        Intent i = new Intent(this, Contactos.class);
        startActivity(i);
    }
}
