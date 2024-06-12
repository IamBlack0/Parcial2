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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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


        mensajes = new ArrayList<>();
        mensajeAdapter = new MensajeAdapter(this, mensajes, usuarioId);
        listView.setAdapter(mensajeAdapter);

        cargarMensajes(contactoId, destinatarioContactoId); // Cargar mensajes al iniciar la actividad

        enviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = inputMensaje.getText().toString().trim();
                if (!texto.isEmpty()) {
                    Mensaje nuevoMensaje = new Mensaje(texto, "HOY", true, usuarioId);
                    mensajes.add(nuevoMensaje);
                    mensajeAdapter.notifyDataSetChanged();
                    inputMensaje.setText(""); // Limpiar el campo de texto
                    listView.setSelection(mensajes.size() - 1); // Scroll to the bottom
                    guardarMensajeEnHistorial(texto, contactoId, destinatarioContactoId); // Guardar el mensaje en SharedPreferences
                }
            }
        });
    }

    private void cargarMensajes(int contactoId, int destinatarioContactoId) {
        String chatKey = getChatKey(contactoId, destinatarioContactoId);
        SharedPreferences prefs = getSharedPreferences(chatKey, MODE_PRIVATE);
        String mensajesData = prefs.getString("mensajes", "");
        if (!mensajesData.isEmpty()) {
            String[] mensajesArray = mensajesData.split("\n");
            for (String mensajeData : mensajesArray) {
                String[] parts = mensajeData.split("\\|");
                if (parts.length == 3) {
                    int id = Integer.parseInt(parts[0]);
                    String texto = parts[1];
                    String timestamp = parts[2]; // Asumiendo que el timestamp está siendo guardado
                    boolean esEnviado = id == usuarioId;
                    mensajes.add(new Mensaje(texto, timestamp, esEnviado, id));
                }
            }
            mensajeAdapter.notifyDataSetChanged();
        }
    }


    private void guardarMensajeEnHistorial(String mensaje, int contactoId, int destinatarioContactoId) {
        String chatKey = getChatKey(contactoId, destinatarioContactoId);
        SharedPreferences prefs = getSharedPreferences(chatKey, MODE_PRIVATE);
        String existingMessages = prefs.getString("mensajes", "");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String timestamp = sdf.format(new Date());
        String newMessageRecord = usuarioId + "|" + mensaje + "|" + timestamp + "\n";
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("mensajes", existingMessages + newMessageRecord);
        editor.apply();
    }


    private String getChatKey(int id1, int id2) {
        return "Chat_" + Math.min(id1, id2) + "_" + Math.max(id1, id2);
    }

    public void VolverChatAmensaje(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("usuarioId", usuarioId); // Pasar el ID del usuario actual
        intent.putExtra("contactoId", contactoId); // Pasar el ID del contacto actual
        intent.putExtra("destinatarioContactoId", destinatarioContactoId); // Pasar el ID del contacto destinatario actual
        startActivity(intent);
    }
}
