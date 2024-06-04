package com.example.parcial2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Chat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ImageView profileImage = findViewById(R.id.profile_image);
        TextView nombreTextView = findViewById(R.id.nombreEnChat);
        TextView apellidoTextView = findViewById(R.id.apellidoEnChat);

        Intent intent = getIntent();
        String nombre = intent.getStringExtra("nombre");
        String apellido = intent.getStringExtra("apellido");
        int imagenId = intent.getIntExtra("imagenId", R.drawable.baseline_person_24);

        profileImage.setImageResource(imagenId);
        nombreTextView.setText(nombre);
        apellidoTextView.setText(apellido);
    }

    public void VolverChatAmensaje(View view) {
        // Mostrar un Toast en lugar de un log
        Toast.makeText(this, "Volver a MainActivity", Toast.LENGTH_SHORT).show();

        // Crear un intent para volver a MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
