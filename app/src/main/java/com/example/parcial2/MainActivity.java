package com.example.parcial2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parcial2.Entidades.Contacto;
import com.example.parcial2.Entidades.Usuario;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Usuario currentUser;
    private TextView nombreTextView;
    private TextView apellidoTextView;

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

        if (esPrimeraEjecucion()) {
            inicializarUsuarios();
        }

        cargarUsuarioActual();
    }

    private boolean esPrimeraEjecucion() {
        SharedPreferences prefs = getSharedPreferences("UsuariosPrefs", MODE_PRIVATE);
        return prefs.getBoolean("esPrimeraEjecucion", true);
    }

    private void inicializarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario(1, "Usuario1", "Apellido1", "+507 0000-0001"));
        usuarios.add(new Usuario(2, "Usuario2", "Apellido2", "+507 0000-0002"));
        guardarUsuarios(usuarios);

        SharedPreferences prefs = getSharedPreferences("UsuariosPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("esPrimeraEjecucion", false);
        editor.apply();
    }

    private void guardarUsuarios(List<Usuario> usuarios) {
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

    private void cargarUsuarioActual() {
        SharedPreferences prefs = getSharedPreferences("UsuariosPrefs", MODE_PRIVATE);
        int currentUserId = prefs.getInt("currentUserId", -1); // Asegúrate de que este valor se actualice correctamente
        List<Usuario> usuarios = obtenerUsuarios();
        for (Usuario usuario : usuarios) {
            if (usuario.getId() == currentUserId) {
                currentUser = usuario;
                nombreTextView.setText(currentUser.getNombre());
                apellidoTextView.setText(currentUser.getApellido());
                break;
            }
        }
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
                }    else if (id == R.id.AgregarContacto) {
                        agregarContacto();
                        return true;

                } else {
                    return false;
                }
            }
        });
        popupMenu.show();
    }

    private void agregarContacto() {
    }


    public void editarPerfil() {
        Intent i = new Intent(this, EditarPerfil.class);
        i.putExtra("usuarioId", currentUser.getId());
        startActivityForResult(i, 1);
    }

    public void cambiarUsuario() {
        final List<Usuario> usuarios = obtenerUsuarios(); // Cargar la lista de usuarios desde SharedPreferences
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
                        actualizarUsuarioActual(); // Asegúrate de llamar a este método
                        guardarUsuarioActual(); // Guardar el usuario actual en SharedPreferences
                    }
                });
        builder.create().show();
    }
    private void actualizarUsuarioActual() {
        if (currentUser != null) {
            nombreTextView.setText(currentUser.getNombre());
            apellidoTextView.setText(currentUser.getApellido());
            // Guardar el ID del usuario actual en SharedPreferences
            SharedPreferences prefs = getSharedPreferences("UsuariosPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("currentUserId", currentUser.getId());
            editor.apply();
        }
    }




    private List<Usuario> obtenerUsuarios() {
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
        return usuarios;
    }

    private void guardarUsuarioActual() {
        SharedPreferences prefs = getSharedPreferences("UsuariosPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("currentUserId", currentUser.getId());
        editor.apply();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            cargarUsuarioActual(); // Recargar el usuario actual y actualizar la UI
        }
    }


    public void BuscarContactos(View view) {
        Intent i = new Intent(this, Contactos.class);
        i.putExtra("usuarioId", currentUser.getId());
        startActivity(i);
    }

//    private void agregarContacto(int idUsuario, String nombre, String apellido, String telefono, int imagenId) {
//        List<Contacto> contactos = obtenerContactos();
//        contactos.add(new Contacto(idUsuario, nombre, apellido, telefono, imagenId));
//        guardarContactos(contactos);
//        Toast.makeText(this, "Contacto agregado exitosamente", Toast.LENGTH_SHORT).show();
//    }
//
//    private void mostrarDialogoAgregarContacto() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Agregar Contacto");
//
//        LinearLayout layout = new LinearLayout(this);
//        layout.setOrientation(LinearLayout.VERTICAL);
//
//        EditText nombreEditText = new EditText(this);
//        EditText apellidoEditText = new EditText(this);
//        EditText telefonoEditText = new EditText(this);
//        EditText imagenIdEditText = new EditText(this);
//
//        nombreEditText.setHint("Nombre");
//        apellidoEditText.setHint("Apellido");
//        telefonoEditText.setHint("Teléfono");
//        imagenIdEditText.setHint("ID de Imagen");
//
//        layout.addView(nombreEditText);
//        layout.addView(apellidoEditText);
//        layout.addView(telefonoEditText);
//        layout.addView(imagenIdEditText);
//
//        builder.setView(layout)
//                .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        int idUsuario = currentUser.getId();
//                        String nombre = nombreEditText.getText().toString();
//                        String apellido = apellidoEditText.getText().toString();
//                        String telefono = telefonoEditText.getText().toString();
//                        int imagenId = Integer.parseInt(imagenIdEditText.getText().toString());
//
//                        agregarContacto(idUsuario, nombre, apellido, telefono, imagenId);
//                    }
//                })
//                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.dismiss();
//                    }
//                });
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }



}
