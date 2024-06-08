package com.example.parcial2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parcial2.Adaptadores.ContactoAdapter;
import com.example.parcial2.Entidades.Contacto;
import com.example.parcial2.Entidades.Usuario;

import java.util.ArrayList;
import java.util.List;

public class Contactos extends AppCompatActivity {

    ListView ContactosList;
    private Usuario currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

        int usuarioId = getIntent().getIntExtra("usuarioId", -1);
        int contactoId = getIntent().getIntExtra("contactoId", -1); // Recibe el ID del contacto
        Toast.makeText(this, "Usuario ID: " + usuarioId + ", Contacto ID: " + contactoId, Toast.LENGTH_SHORT).show(); // Muestra ambos IDs
        cargarUsuarioActual(usuarioId);

        this.InicializarControles();
    }


    private void InicializarControles() {
        ContactosList = findViewById(R.id.contactos_list);
        this.LlenarListViews();
    }

    private void LlenarListViews() {
        List<Contacto> contactos = this.ObtenerContactos();
        if (contactos.isEmpty()) {
            Toast.makeText(this, "No se encontraron contactos", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Contactos encontrados: " + contactos.size(), Toast.LENGTH_SHORT).show();
        }
        ContactoAdapter contactoAdapter = new ContactoAdapter(ContactosList.getContext(), contactos);
        ContactosList.setAdapter(contactoAdapter);
    }

    private List<Contacto> ObtenerContactos() {
        SharedPreferences prefs = getSharedPreferences("ContactosPrefs", MODE_PRIVATE);
        String contactosData = prefs.getString("contactos", "");
        List<Contacto> contactos = new ArrayList<>();
        if (contactosData.isEmpty()) {
            // Aquí se inicializan los contactos por primera vez y se guardan
            contactos = inicializarContactos();
            guardarContactos(contactos);
            Toast.makeText(this, "Contactos inicializados y guardados", Toast.LENGTH_SHORT).show();
        } else {
            for (String contactoData : contactosData.split(";")) {
                String[] parts = contactoData.split("\\|");
                if (parts.length == 5) {  // Asegúrate de que ahora esperamos 5 partes por contacto
                    int id = Integer.parseInt(parts[0]);
                    String nombre = parts[1];
                    String apellido = parts[2];
                    String telefono = parts[3];
                    int imagenId = Integer.parseInt(parts[4]);  // Nuevo campo para ID de imagen
                    contactos.add(new Contacto(id, nombre, apellido, telefono, imagenId));
                }
            }
            Toast.makeText(this, "Contactos cargados desde SharedPreferences", Toast.LENGTH_SHORT).show();
        }

        // Filtrar contactos según el usuario actual
        List<Contacto> contactosFiltrados = new ArrayList<>();
        for (Contacto contacto : contactos) {
            if (currentUser.getId() == 1 && contacto.getId() != 1) {
                contactosFiltrados.add(contacto);
            } else if (currentUser.getId() == 2 && contacto.getId() != 2) {
                contactosFiltrados.add(contacto);
            }
        }
        Toast.makeText(this, "Contactos filtrados: " + contactosFiltrados.size(), Toast.LENGTH_SHORT).show();
        return contactosFiltrados;
    }

    private List<Contacto> inicializarContactos() {
        List<Contacto> contactos = new ArrayList<>();
        // Asumiendo que tienes imágenes predeterminadas para cada contacto en res/drawable
        contactos.add(new Contacto(1, "Contacto1", "Apellido1", "+507 0000-0001", R.drawable.yo_xx));
        contactos.add(new Contacto(2, "Contacto2", "Apellido2", "+507 0000-0002", R.drawable.yo_xx));
        contactos.add(new Contacto(3, "Contacto3", "Apellido3", "+507 0000-0003", R.drawable.yo_xx));
        contactos.add(new Contacto(4, "Contacto4", "Apellido4", "+507 0000-0004", R.drawable.yo_xx));
        contactos.add(new Contacto(5, "Contacto5", "Apellido5", "+507 0000-0005", R.drawable.yo_xx));
        contactos.add(new Contacto(6, "Contacto6", "Apellido6", "+507 0000-0006", R.drawable.yo_xx));
        return contactos;
    }

    private void guardarContactos(List<Contacto> contactos) {
        SharedPreferences prefs = getSharedPreferences("ContactosPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        StringBuilder data = new StringBuilder();
        for (Contacto contacto : contactos) {
            data.append(contacto.getId()).append("|")
                    .append(contacto.getNombre()).append("|")
                    .append(contacto.getApellido()).append("|")
                    .append(contacto.getTelefono()).append("|")
                    .append(contacto.getImagenId()).append(";");  // Asegúrate de incluir el ID de imagen
        }
        editor.putString("contactos", data.toString());
        editor.apply();
    }

    private void cargarUsuarioActual(int usuarioId) {
        SharedPreferences prefs = getSharedPreferences("UsuariosPrefs", MODE_PRIVATE);
        String usuariosData = prefs.getString("usuarios", "");
        List<Usuario> usuarios = new ArrayList<>();
        if (!usuariosData.isEmpty()) {
            for (String userData : usuariosData.split(";")) {
                String[] parts = userData.split("\\|");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0]);
                    String nombre = parts[1];
                    String apellido = parts[2];
                    String telefono = parts[3];
                    usuarios.add(new Usuario(id, nombre, apellido, telefono));
                }
            }
        }
        for (Usuario usuario : usuarios) {
            if (usuario.getId() == usuarioId) {
                currentUser = usuario;
                Toast.makeText(this, "Usuario cargado: " + currentUser.getNombre(), Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    public void VolverContactosAmensaje(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
