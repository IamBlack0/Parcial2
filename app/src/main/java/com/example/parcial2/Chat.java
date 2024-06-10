package com.example.parcial2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parcial2.Adaptadores.MensajeAdapter;
import com.example.parcial2.Entidades.Mensaje;

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {

    private List<Mensaje> mensajes;
    private MensajeAdapter mensajeAdapter;
    private ListView listView;
    private EditText inputMensaje;
    private ImageButton enviarMensaje;
    private int usuarioId;
    private int contactoId; // ID del contacto actual
    private int destinatarioContactoId; // ID del contacto destinatario actual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ImageView profileImage = findViewById(R.id.ImagenPerfil);
        TextView nombreTextView = findViewById(R.id.nombreEnChat);
        TextView apellidoTextView = findViewById(R.id.apellidoEnChat);
        inputMensaje = findViewById(R.id.inputMensaje);
        enviarMensaje = findViewById(R.id.EnviarMensaje);
        listView = findViewById(R.id.messages_list);

        Intent intent = getIntent();
        String nombre = intent.getStringExtra("nombre");
        String apellido = intent.getStringExtra("apellido");
        int imagenId = intent.getIntExtra("imagenId", R.drawable.baseline_person_24);
        usuarioId = intent.getIntExtra("usuarioId", -1); // Obtener el ID del usuario actual
        contactoId = intent.getIntExtra("contactoId", -1); // Obtener el ID del contacto actual
        destinatarioContactoId = intent.getIntExtra("destinatarioContactoId", -1); // Obtener el ID del contacto destinatario actual

        profileImage.setImageResource(imagenId);
        nombreTextView.setText(nombre);
        apellidoTextView.setText(apellido);

        // Mostrar Toast con los IDs de usuario y contacto
        Toast.makeText(this, "Usuario ID: " + usuarioId + ", Contacto ID: " + contactoId + ", Destinatario Contacto ID: " + destinatarioContactoId, Toast.LENGTH_LONG).show();

        mensajes = new ArrayList<>();
        mensajeAdapter = new MensajeAdapter(this, mensajes, usuarioId);
        listView.setAdapter(mensajeAdapter);

        cargarMensajes(usuarioId, contactoId, destinatarioContactoId); // Cargar mensajes al iniciar la actividad

        enviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = inputMensaje.getText().toString().trim();
                if (!texto.isEmpty()) {
                    Mensaje nuevoMensaje = new Mensaje(texto, "15:18 PM", true, usuarioId);
                    mensajes.add(nuevoMensaje);
                    mensajeAdapter.notifyDataSetChanged();
                    inputMensaje.setText(""); // Limpiar el campo de texto
                    listView.setSelection(mensajes.size() - 1); // Scroll to the bottom
                    guardarMensajeEnHistorial(texto, usuarioId, contactoId, destinatarioContactoId); // Guardar el mensaje en SharedPreferences
                }
            }
        });
    }

    private void cargarMensajes(int remitenteId, int remitenteContactoId, int destinatarioContactoId) {
        SharedPreferences prefs = getSharedPreferences("Chat_" + remitenteId + "_" + remitenteContactoId + "_" + destinatarioContactoId, MODE_PRIVATE);
        String mensajesData = prefs.getString("mensajes", "");
        if (!mensajesData.isEmpty()) {
            String[] mensajesArray = mensajesData.split("\n");
            for (String mensajeData : mensajesArray) {
                String[] parts = mensajeData.split("\\|");
                if (parts.length == 2) {
                    int id = Integer.parseInt(parts[0]);
                    String texto = parts[1];
                    boolean esEnviado = id == remitenteId;
                    mensajes.add(new Mensaje(texto, "Hora", esEnviado, id));
                }
            }
            mensajeAdapter.notifyDataSetChanged();
        }
    }

    private void guardarMensajeEnHistorial(String mensaje, int remitenteId, int remitenteContactoId, int destinatarioContactoId) {
        SharedPreferences prefs = getSharedPreferences("Chat_" + remitenteId + "_" + remitenteContactoId + "_" + destinatarioContactoId, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("mensajes", prefs.getString("mensajes", "") + remitenteId + "|" + mensaje + "\n");
        editor.apply();
    }

    public void VolverChatAmensaje(View view) {
        Toast.makeText(this, "Volver a MainActivity", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("usuarioId", usuarioId); // Pasar el ID del usuario actual
        intent.putExtra("contactoId", contactoId); // Pasar el ID del contacto actual
        intent.putExtra("destinatarioContactoId", destinatarioContactoId); // Pasar el ID del contacto destinatario actual
        startActivity(intent);
    }
}
