package com.example.parcial2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parcial2.Entidades.Usuario;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EditarPerfil extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private EditText nombreEditText;
    private EditText apellidoEditText;
    private TextView telefonoTextView;
    private Usuario currentUser;
    private List<Usuario> usuarios;
    private ImageView perfilImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        nombreEditText = findViewById(R.id.nombreEditar);
        apellidoEditText = findViewById(R.id.apellidoEditar);
        telefonoTextView = findViewById(R.id.telefonoEditar);
        perfilImageView = findViewById(R.id.ImagenPerfil);

        int usuarioId = getIntent().getIntExtra("usuarioId", -1);
        if (usuarioId != -1) {
            int contactoId = obtenerContactoIdActual(usuarioId);
            String imagenId = obtenerImagenIdDelContacto(contactoId);
            if (imagenId != null && !imagenId.isEmpty()) {
                Picasso.get().load(imagenId).into(perfilImageView);
            }
            cargarUsuarioActual(usuarioId);
        }
    }

    private int obtenerContactoIdActual(int usuarioId) {
        // Aquí simplemente devolvemos el ID del usuario, asumiendo que cada usuario tiene un contacto con el mismo ID
        return usuarioId;
    }


    private String obtenerImagenIdDelContacto(int contactoId) {
        SharedPreferences prefs = getSharedPreferences("ContactosPrefs", MODE_PRIVATE);
        String contactosData = prefs.getString("contactos", "");
        if (!contactosData.isEmpty()) {
            for (String contactoData : contactosData.split(";")) {
                String[] parts = contactoData.split("\\|");
                if (parts.length == 5 && Integer.parseInt(parts[0]) == contactoId) {
                    return parts[4];  // Retorna la URI de la imagen como String
                }
            }
        }
        return ""; // Retorna una cadena vacía si no encuentra nada
    }



    public void VolverEditAMensaje(View view) {
        Intent intent = new Intent();
        intent.putExtra("usuarioId", currentUser.getId());
        setResult(RESULT_OK, intent);
        finish();
    }

    public void GuardarCambios(View view) {
        String nuevoNombre = nombreEditText.getText().toString();
        String nuevoApellido = apellidoEditText.getText().toString();

        if (!nuevoNombre.isEmpty() && !nuevoApellido.isEmpty()) {
            currentUser.setNombre(nuevoNombre);
            currentUser.setApellido(nuevoApellido);
            actualizarListaUsuarios();
            guardarUsuarios();
            // Recargar la imagen
            String imagenId = obtenerImagenIdDelContacto(currentUser.getId());
            if (imagenId != null && !imagenId.isEmpty()) {
                Picasso.get().load(imagenId).into(perfilImageView);
            }
            Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show();
            VolverEditAMensaje(view);
        } else {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
        }
    }


    private void actualizarListaUsuarios() {
        usuarios = obtenerUsuarios();
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == currentUser.getId()) {
                usuarios.set(i, currentUser);
                return;
            }
        }
        usuarios.add(currentUser);
    }

    private void guardarUsuarios() {
        SharedPreferences prefs = getSharedPreferences("UsuariosPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        StringBuilder data = new StringBuilder();
        for (Usuario usuario : usuarios) {
            data.append(usuario.getId()).append("|")
                    .append(usuario.getNombre()).append("|")
                    .append(usuario.getApellido()).append("|")
                    .append(usuario.getTelefono()).append(";");
        }
        editor.putString("usuarios", data.toString());
        editor.apply();
    }

    private List<Usuario> obtenerUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences("UsuariosPrefs", MODE_PRIVATE);
        String usuariosData = prefs.getString("usuarios", "");
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
        return usuarios;
    }

    private void cargarUsuarioActual(int usuarioId) {
        List<Usuario> usuarios = obtenerUsuarios();
        for (Usuario usuario : usuarios) {
            if (usuario.getId() == usuarioId) {
                currentUser = usuario;
                nombreEditText.setText(currentUser.getNombre());
                apellidoEditText.setText(currentUser.getApellido());
                telefonoTextView.setText(currentUser.getTelefono());
                break;
            }
        }
    }
    public void cambiarImagen(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            Picasso.get().load(selectedImage).into(perfilImageView);

            // Guardar la nueva URI de la imagen en SharedPreferences
           // guardarImagenUri(selectedImage.toString());
        }
    }










}
