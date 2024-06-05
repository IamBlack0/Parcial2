package com.example.parcial2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parcial2.Entidades.Usuario;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
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

        // Obtener el ID del usuario desde el Intent
        int usuarioId = getIntent().getIntExtra("usuarioId", -1);

        // Cargar el usuario actual desde el archivo
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

    private List<Usuario> obtenerUsuarios() {
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
                        usuarios.add(new Usuario(id, nombre, apellido, telefono));
                    }
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return usuarios;
    }

    private void guardarUsuarios() {
        try {
            FileOutputStream fos = openFileOutput("usuario.txt", MODE_PRIVATE); // Sobrescribir el archivo
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            for (Usuario usuario : usuarios) {
                osw.write(usuario.getId() + "|" + usuario.getNombre() + "|" + usuario.getApellido() + "|" + usuario.getTelefono() + "\n");
            }
            osw.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarUsuarioActual(int usuarioId) {
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
                        if (id == usuarioId) {
                            currentUser = new Usuario(id, nombre, apellido, telefono);
                            nombreEditText.setText(nombre);
                            apellidoEditText.setText(apellido);
                            telefonoTextView.setText(telefono);
                            break;
                        }
                    }
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
