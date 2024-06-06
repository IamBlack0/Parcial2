package com.example.parcial2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parcial2.Adaptadores.ConversacionAdapter;
import com.example.parcial2.Entidades.Usuario;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Usuario currentUser;
    private TextView nombreTextView;
    private TextView apellidoTextView;
    private List<Usuario> usuarios;
    private ListView conversationsListView;
    private ConversacionAdapter conversacionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombreTextView = findViewById(R.id.headerNombre);
        apellidoTextView = findViewById(R.id.headerApellido);
        conversationsListView = findViewById(R.id.conversations_list);

        ImageView configIcon = findViewById(R.id.config_icon);
        configIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        try {
            File file = new File(getFilesDir(), "historialChat.txt");
            File file1 = new File(getFilesDir(), "historialChat1.txt");
            File file2 = new File(getFilesDir(), "historialChat2.txt");
            File file3 = new File(getFilesDir(), "historialChat3.txt");
            File file4 = new File(getFilesDir(), "historialChat4.txt");


            if (!file.exists()) {
                file.createNewFile();
            }
            if (!file1.exists()) {
                file1.createNewFile();
            }
            if (!file2.exists()) {
                file2.createNewFile();
            }
            if (!file3.exists()) {
                file3.createNewFile();
            }
            if (!file4.exists()) {
                file4.createNewFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        int usuarioId = getIntent().getIntExtra("usuarioId", -1);
        if (usuarioId != -1) {
            cargarUsuarioPorId(usuarioId);
        } else {
            cargarUsuarioActual();
        }

        actualizarListaUsuarios();
        cargarConversaciones();
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
        i.putExtra("usuarioId", currentUser.getId());
        startActivityForResult(i, 1);
    }

    public void cambiarUsuario() {
        actualizarListaUsuarios();
        String[] nombresUsuarios = new String[usuarios.size()];
        for (int i = 0; i < usuarios.size(); i++) {
            nombresUsuarios[i] = usuarios.get(i).getNombre() + " " + usuarios.get(i).getApellido();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar Usuario")
                .setItems(nombresUsuarios, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentUser = usuarios.get(which);
                        actualizarUsuarioActual();
                        guardarUsuarios();
                        cargarConversaciones(); // Recargar las conversaciones después de cambiar el usuario
                    }
                });
        builder.create().show();
    }

    private void actualizarListaUsuarios() {
        usuarios = obtenerUsuarios();
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

    private void actualizarUsuarioActual() {
        nombreTextView.setText(currentUser.getNombre());
        apellidoTextView.setText(currentUser.getApellido());
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

    private void cargarUsuarioActual() {
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
                        currentUser = new Usuario(id, nombre, apellido, telefono);
                        actualizarUsuarioActual();
                        break; // Salir del bucle después de cargar el primer usuario
                    }
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void cargarUsuarioPorId(int usuarioId) {
        File file = new File(getFilesDir(), "usuario.txt");
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 4) {
                        int id = Integer.parseInt(parts[0]);
                        if (id == usuarioId) {
                            String nombre = parts[1];
                            String apellido = parts[2];
                            String telefono = parts[3];
                            currentUser = new Usuario(id, nombre, apellido, telefono);
                            actualizarUsuarioActual();
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

    private void cargarConversaciones() {
        Map<Integer, String> ultimosMensajesMap = new HashMap<>();
        File file = new File(getFilesDir(), "historialChat.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 2) {
                        int id = Integer.parseInt(parts[0]);
                        String mensaje = parts[1];
                        if (id != currentUser.getId()) {
                            ultimosMensajesMap.put(id, mensaje);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<Usuario> usuariosConMensajes = new ArrayList<>();
        List<String> ultimosMensajes = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario.getId() != currentUser.getId() && ultimosMensajesMap.containsKey(usuario.getId())) {
                usuariosConMensajes.add(usuario);
                ultimosMensajes.add(ultimosMensajesMap.get(usuario.getId()));
            }
        }

        conversacionAdapter = new ConversacionAdapter(this, usuariosConMensajes, ultimosMensajes, currentUser.getId());
        conversationsListView.setAdapter(conversacionAdapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            int usuarioId = data.getIntExtra("usuarioId", -1);
            if (usuarioId != -1) {
                cargarUsuarioPorId(usuarioId);
            } else {
                cargarUsuarioActual();
            }
            actualizarListaUsuarios();
            cargarConversaciones();
        }
    }

    public void BuscarContactos(View view) {
        Intent i = new Intent(this, Contactos.class);
        i.putExtra("usuarioId", currentUser.getId());
        startActivity(i);
    }
}
