package com.example.parcial2;

import android.content.Intent;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {

    private List<Mensaje> mensajes;
    private MensajeAdapter mensajeAdapter;
    private ListView listView;
    private EditText inputMensaje;
    private ImageButton enviarMensaje;
    private int usuarioId;

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

        profileImage.setImageResource(imagenId);
        nombreTextView.setText(nombre);
        apellidoTextView.setText(apellido);

        mensajes = new ArrayList<>();
        mensajeAdapter = new MensajeAdapter(this, mensajes, usuarioId);
        listView.setAdapter(mensajeAdapter);

        cargarMensajes();  // Cargar mensajes al iniciar la actividad

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
                    guardarMensajeEnHistorial(texto, usuarioId); // Guardar el mensaje en el archivo
                }
            }
        });
    }

    private void cargarMensajes() {
        File file = new File(getFilesDir(), "historialChat.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 2) {
                        int id = Integer.parseInt(parts[0]);
                        String mensaje = parts[1];
                        boolean esEnviado = id == usuarioId;
                        mensajes.add(new Mensaje(mensaje, "Hora", esEnviado, id));
                    }
                }
                mensajeAdapter.notifyDataSetChanged();
            } catch (IOException e) {
                Toast.makeText(this, "Error al cargar mensajes: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void guardarMensajeEnHistorial(String mensaje, int usuarioId) {
        File file = new File(getFilesDir(), "historialChat.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(usuarioId + "|" + mensaje + "\n");
        } catch (IOException e) {
            Toast.makeText(this, "Error al guardar el mensaje: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void VolverChatAmensaje(View view) {
        Toast.makeText(this, "Volver a MainActivity", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("usuarioId", usuarioId); // Pasar el ID del usuario actual
        startActivity(intent);
    }
}
