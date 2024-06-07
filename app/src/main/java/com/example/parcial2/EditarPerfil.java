package com.example.parcial2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parcial2.Entidades.Usuario;

import java.util.ArrayList;
import java.util.List;

public class EditarPerfil extends AppCompatActivity {

    private EditText nombreEditText;
    private EditText apellidoEditText;
    private TextView telefonoTextView;
    private Usuario currentUser;
    private List<Usuario> usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        nombreEditText = findViewById(R.id.nombreEditar);
        apellidoEditText = findViewById(R.id.apellidoEditar);
        telefonoTextView = findViewById(R.id.telefonoEditar);

        int usuarioId = getIntent().getIntExtra("usuarioId", -1);
        cargarUsuarioActual(usuarioId);
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
        // Iniciar una actividad para seleccionar una imagen de la galería o capturar una foto
    }

}
