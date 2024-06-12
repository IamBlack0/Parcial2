package com.example.parcial2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parcial2.Adaptadores.ContactoAdapter;
import com.example.parcial2.Adaptadores.ConversacionAdapter;
import com.example.parcial2.Entidades.Contacto;
import com.example.parcial2.Entidades.Conversacion;
import com.example.parcial2.Entidades.Usuario;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Usuario currentUser;
    private TextView nombreTextView;
    private TextView apellidoTextView;
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

        if (esPrimeraEjecucion()) {
            inicializarUsuarios();
        }

        cargarUsuarioActual();
        if (currentUser != null) {
            cargarConversacionesRecientes();
        } else {
            Toast.makeText(this, "No se pudo cargar el usuario actual", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarConversacionesRecientes();  // Recargar conversaciones cada vez que la actividad se reanuda
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
        if (currentUserId == -1 && !usuarios.isEmpty()) {
            // Si no hay usuario guardado, inicializar con el usuario1
            currentUser = usuarios.get(0);
            guardarUsuarioActual();
        } else {
            for (Usuario usuario : usuarios) {
                if (usuario.getId() == currentUserId) {
                    currentUser = usuario;
                    break;
                }
            }
        }
        if (currentUser != null) {
            nombreTextView.setText(currentUser.getNombre());
            apellidoTextView.setText(currentUser.getApellido());
        }
    }

    private void actualizarVista() {
        // Limpiar el adaptador de la lista
        conversationsListView.setAdapter(null);

        // Recargar las conversaciones o contactos según el usuario actual
        cargarConversacionesRecientes();
    }

    private void cargarConversacionesRecientes() {
        if (currentUser.getId() == 1) {
            cargarConversacionesParaUsuario1();
        } else if (currentUser.getId() == 2) {
            cargarContactosParaUsuario2();
        }
    }

    private void cargarConversacionesParaUsuario1() {
        Map<Integer, Conversacion> ultimaConversacionPorContacto = new HashMap<>();
        List<Contacto> contactos = obtenerContactos(); // Obtener la lista de contactos

        for (Contacto contacto : contactos) {
            if (contacto.getId() != currentUser.getId()) { // Asegúrate de no incluir el usuario actual como contacto
                String chatKey = getChatKey(1, contacto.getId());
                SharedPreferences prefs = getSharedPreferences(chatKey, MODE_PRIVATE);
                String mensajesData = prefs.getString("mensajes", "");

                if (!mensajesData.isEmpty()) {
                    String[] mensajesArray = mensajesData.split("\n");
                    for (String mensajeData : mensajesArray) {
                        String[] messageParts = mensajeData.split("\\|");
                        if (messageParts.length == 3) {
                            String ultimoMensaje = messageParts[1];
                            String timestamp = messageParts[2];
                            Conversacion conversacion = new Conversacion(1, contacto.getId(), contacto.getId(), contacto.getNombre(), contacto.getApellido(), contacto.getImagenId(), ultimoMensaje, timestamp);
                            ultimaConversacionPorContacto.put(contacto.getId(), conversacion);
                        }
                    }
                }
            }
        }

        List<Conversacion> conversaciones = new ArrayList<>(ultimaConversacionPorContacto.values());
        if (!conversaciones.isEmpty()) {
            conversacionAdapter = new ConversacionAdapter(this, conversaciones);
            conversationsListView.setAdapter(conversacionAdapter);
        } else {
            Toast.makeText(this, "No hay conversaciones para cargar", Toast.LENGTH_SHORT).show();
        }
    }





    private void cargarContactosParaUsuario2() {
        List<Contacto> contactos = this.ObtenerContactos();
        ContactoAdapter contactoAdapter = new ContactoAdapter(conversationsListView.getContext(), contactos, currentUser.getId());
        conversationsListView.setAdapter(contactoAdapter);
    }

    private List<Contacto> ObtenerContactos() {
        SharedPreferences prefs = getSharedPreferences("ContactosPrefs", MODE_PRIVATE);
        String contactosData = prefs.getString("contactos", "");
        List<Contacto> contactos = new ArrayList<>();
        if (!contactosData.isEmpty()) {
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
        return contactosFiltrados;
    }

    private void cargarConversacion(String chatKey, int remitenteId, int destinatarioId, String nombre, String apellido, int imagenId, List<Conversacion> conversaciones) {
        SharedPreferences prefs = getSharedPreferences(chatKey, MODE_PRIVATE);
        String mensajesData = prefs.getString("mensajes", "");

        if (!mensajesData.isEmpty()) {
            String[] mensajesArray = mensajesData.split("\n");
            for (String mensajeData : mensajesArray) {
                String[] messageParts = mensajeData.split("\\|");
                if (messageParts.length == 3) {
                    String ultimoMensaje = messageParts[1];
                    String timestamp = messageParts[2]; // Asegúrate de que estás guardando y cargando el timestamp
                    conversaciones.add(new Conversacion(remitenteId, destinatarioId, destinatarioId, nombre, apellido, imagenId, ultimoMensaje, timestamp));
                }
            }
        }
        if (!conversaciones.isEmpty()) {
            conversacionAdapter = new ConversacionAdapter(this, conversaciones);
            conversationsListView.setAdapter(conversacionAdapter);
        } else {
            Toast.makeText(this, "No hay conversaciones para cargar", Toast.LENGTH_SHORT).show();
        }
    }



    private String getChatKey(int id1, int id2) {
        return "Chat_" + Math.min(id1, id2) + "_" + Math.max(id1, id2);
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_contextual, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.EditarPerfil) {
                    int contactoId = obtenerIdDelContactoActual(); // Implementa este método según tu lógica de aplicación
                    editarPerfil(contactoId);
                    return true;
                } else if (id == R.id.CambiarUsuario) {
                    cambiarUsuario();
                    return true;
                } else if (id == R.id.AgregarContacto) {
                    mostrarDialogoAgregarContacto();
                    return true;
                } else {
                    return false;
                }
            }
        });
        popupMenu.show();
    }

    private List<Contacto> obtenerContactos() {
        SharedPreferences prefs = getSharedPreferences("ContactosPrefs", MODE_PRIVATE);
        String contactosData = prefs.getString("contactos", "");
        List<Contacto> contactos = new ArrayList<>();
        if (!contactosData.isEmpty()) {
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
        }
        return contactos;
    }


    private void agregarContacto(int id, String nombre, String apellido, String telefono, int imagenId) {
        List<Contacto> contactos = obtenerContactos();
        contactos.add(new Contacto(id, nombre, apellido, telefono, imagenId));
        guardarContactos(contactos);
        Toast.makeText(this, "Contacto agregado exitosamente", Toast.LENGTH_SHORT).show();
    }

    private int obtenerIdDelContactoActual() {
        // Este es un ejemplo, necesitas adaptarlo a tu lógica de aplicación
        return currentUser.getId(); // Suponiendo que el ID del usuario es el mismo que el del contacto
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
                    .append(contacto.getImagenId()).append(";");
        }
        editor.putString("contactos", data.toString());
        editor.apply();
    }

    @SuppressLint("ResourceType")
    private void mostrarDialogoAgregarContacto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar Contacto");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText nombreEditText = new EditText(this);
        EditText apellidoEditText = new EditText(this);
        EditText telefonoEditText = new EditText(this);

        nombreEditText.setHint("Nombre");
        apellidoEditText.setHint("Apellido");
        telefonoEditText.setHint("Teléfono");

        layout.addView(nombreEditText);
        layout.addView(apellidoEditText);
        layout.addView(telefonoEditText);

        // Crear RadioGroup para las imágenes
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);

        RadioButton radioButton1 = new RadioButton(this);
        radioButton1.setText("Imagen 1");
        radioButton1.setId(R.drawable.screenshot_20240611_162322); // Usa el ID del recurso de la imagen

        RadioButton radioButton2 = new RadioButton(this);
        radioButton2.setText("Imagen 2");
        radioButton2.setId(R.drawable.gpgcrfaagaa95xc); // Usa el ID del recurso de la imagen

        RadioButton radioButton3 = new RadioButton(this);
        radioButton3.setText("Imagen 3");
        radioButton3.setId(R.drawable.fb_img_1714678110428); // Usa el ID del recurso de la imagen

        radioGroup.addView(radioButton1);
        radioGroup.addView(radioButton2);
        radioGroup.addView(radioButton3);

        layout.addView(radioGroup);

        builder.setView(layout)
                .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String nombre = nombreEditText.getText().toString();
                        String apellido = apellidoEditText.getText().toString();
                        String telefono = telefonoEditText.getText().toString();
                        int imagenId = radioGroup.getCheckedRadioButtonId(); // Obtener el ID de la imagen seleccionada

                        // Generar un nuevo ID para el contacto
                        int nuevoId = generarNuevoIdContacto();

                        agregarContacto(nuevoId, nombre, apellido, telefono, imagenId);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //prueba
    private int generarNuevoIdContacto() {
        List<Contacto> contactos = obtenerContactos();
        int maxId = 0;
        for (Contacto contacto : contactos) {
            if (contacto.getId() > maxId) {
                maxId = contacto.getId();
            }
        }
        return maxId + 1;
    }




    public void editarPerfil(int contactoId) {
        Intent i = new Intent(this, EditarPerfil.class);
        i.putExtra("usuarioId", currentUser.getId());
        int imagenId = obtenerImagenIdDelContacto(contactoId); // Obtiene la ID de la imagen del contacto
        i.putExtra("imagenId", imagenId);
        startActivityForResult(i, 1);
    }


    private int obtenerImagenIdDelContacto(int contactoId) {
        List<Contacto> contactos = obtenerContactos(); // Asegúrate de que este método devuelve todos los contactos
        for (Contacto contacto : contactos) {
            if (contacto.getId() == contactoId) {
                return contacto.getImagenId();
            }
        }
        return 0; // Retorna 0 si no encuentra nada, ajusta según necesidad
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
                        actualizarVista(); // Actualizar la vista para el nuevo usuario
                    }
                });
        builder.create().show();
    }


    private void actualizarUsuarioActual() {
        if (currentUser!= null) {
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
}
