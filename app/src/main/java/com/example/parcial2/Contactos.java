package com.example.parcial2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parcial2.Adaptadores.ContactoAdapter;
import com.example.parcial2.Entidades.Usuario;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Contactos extends AppCompatActivity {

    ListView ContactosList;
    private Usuario currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

        // Obtener el usuario actual desde el Intent
        int usuarioId = getIntent().getIntExtra("usuarioId", -1);
        if (usuarioId == -1) {
            Toast.makeText(this, "Error: ID de usuario no válido.", Toast.LENGTH_LONG).show();
            return;
        }
        cargarUsuarioActual(usuarioId);

        this.InicializarControles();
    }

    private void InicializarControles() {
        ContactosList = (ListView) findViewById(R.id.contactos_list);
        this.LlenarListViews();
    }

    private void LlenarListViews() {
        List<Usuario> usuarios = this.ObtenerContactos();
        ContactoAdapter contactoAdapter = new ContactoAdapter(ContactosList.getContext(), usuarios, currentUser.getId());
        ContactosList.setAdapter(contactoAdapter);
    }

    private List<Usuario> ObtenerContactos() {
        List<Usuario> usuarios = new ArrayList<>();
        File file = new File(getFilesDir(), "usuario.txt");
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 4) {
                        int id = Integer.parseInt(parts[0]);
                        String nombre = parts[1];
                        String apellido = parts[2];
                        String telefono = parts[3];
                        if (id != currentUser.getId()) { // Filtrar al usuario actual
                            usuarios.add(new Usuario(id, nombre, apellido, telefono));
                        }
                    }
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return usuarios;
    }

    private void cargarUsuarioActual(int usuarioId) {
        File file = new File(getFilesDir(), "usuario.txt");
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                boolean found = false;  // Flag para verificar si se encontró el usuario
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 4) {
                        int id = Integer.parseInt(parts[0]);
                        if (id == usuarioId) {
                            String nombre = parts[1];
                            String apellido = parts[2];
                            String telefono = parts[3];
                            currentUser = new Usuario(id, nombre, apellido, telefono);
                            found = true;  // Marcar que se encontró el usuario
                            break;
                        }
                    }
                }
                br.close();
                if (!found) {
                    Toast.makeText(this, "Usuario no encontrado.", Toast.LENGTH_SHORT).show();
                    currentUser = null;  // Asegurar que currentUser es null si no se encontró el usuario
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al cargar el usuario: " + e.getMessage(), Toast.LENGTH_LONG).show();
                currentUser = null;  // Asegurar que currentUser es null en caso de error
            }
        } else {
            Toast.makeText(this, "Archivo de usuarios no existe.", Toast.LENGTH_SHORT).show();
            currentUser = null;  // Asegurar que currentUser es null si no existe el archivo
        }
    }


    public void VolverContactosAmensaje(View view) {
        if (currentUser == null) {
            Toast.makeText(this, "No se pudo cargar el usuario actual.", Toast.LENGTH_SHORT).show();
            return;  // Salir del método si currentUser es null
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("usuarioId", currentUser.getId());
        startActivity(intent);
    }

}
