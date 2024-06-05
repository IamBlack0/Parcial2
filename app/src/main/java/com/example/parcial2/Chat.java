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

import java.io.BufferedWriter;
import java.io.File;
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

        // Mostrar un Toast con el ID del usuario recibido
        Toast.makeText(this, "ID del usuario recibido: " + usuarioId, Toast.LENGTH_LONG).show();

        profileImage.setImageResource(imagenId);
        nombreTextView.setText(nombre);
        apellidoTextView.setText(apellido);

        mensajes = new ArrayList<>();
        mensajeAdapter = new MensajeAdapter(this, mensajes);
        listView.setAdapter(mensajeAdapter);

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
                    guardarMensajeEnHistorial(nuevoMensaje); // Guardar el mensaje en el archivo
                }
            }
        });
    }

    private void guardarMensajeEnHistorial(Mensaje mensaje) {
        File file = new File(getFilesDir(), "historialChat.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(mensaje.getUsuarioId() + "|" + mensaje.getTexto() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void VolverChatAmensaje(View view) {
        Toast.makeText(this, "Volver a MainActivity", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("usuarioId", usuarioId); // Pasar el ID del usuario actual
        startActivity(intent);
    }
}
