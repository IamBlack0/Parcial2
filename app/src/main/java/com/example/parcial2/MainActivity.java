package com.example.parcial2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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

public class MainActivity extends AppCompatActivity {

    private Usuario currentUser;
    private TextView nombreTextView;
    private TextView apellidoTextView;
    private List<Usuario> usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombreTextView = findViewById(R.id.headerNombre);
        apellidoTextView = findViewById(R.id.headerApellido);

        ImageView configIcon = findViewById(R.id.config_icon);
        configIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        try {
            File file = new File(getFilesDir(), "historialChat.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Intenta obtener el ID del usuario desde el Intent; si no existe, usa un valor predeterminado
        int usuarioId = getIntent().getIntExtra("usuarioId", -1);
        if (usuarioId != -1) {
            cargarUsuarioPorId(usuarioId);
        } else {
            cargarUsuarioActual();  // Carga el primer usuario por defecto si no se proporciona un ID
        }

        // Código para crear el archivo usuario.txt con dos usuarios iniciales
        // Descomenta este bloque para crear el archivo con los usuarios iniciales

//        try {
//            FileOutputStream fos = openFileOutput("usuario.txt", MODE_PRIVATE);
//            OutputStreamWriter osw = new OutputStreamWriter(fos);
//            osw.write("1|JOSE|RIVERA|+507 6498-5644\n");
//            osw.write("2|MICAELA|RIVERA|+507 6342-9482\n");
//            osw.close();
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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
                while ((line = br.readLine())!= null) {
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
                while ((line = br.readLine())!= null) {
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
        }
    }

    public void BuscarContactos(View view) {
        Intent i = new Intent(this, Contactos.class);
        i.putExtra("usuarioId", currentUser.getId());
        startActivity(i);
    }
}
