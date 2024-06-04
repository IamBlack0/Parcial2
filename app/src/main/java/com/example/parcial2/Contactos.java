package com.example.parcial2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.parcial2.Adaptadores.ContactoAdapter;
import com.example.parcial2.Entidades.Usuario;

import java.util.ArrayList;
import java.util.List;

public class Contactos extends AppCompatActivity {


    ListView ContactosList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

        this.InicializarControles();

    }



    private void InicializarControles() {
        ContactosList = (ListView) findViewById(R.id.contactos_list);
        this.LlenarListViews();
    }


    private void LlenarListViews() {
        List<Usuario> usuarios = this.ObtenerContactos();
        ContactoAdapter contactoAdapter = new ContactoAdapter(ContactosList.getContext(), usuarios);
        ContactosList.setAdapter(contactoAdapter);
    }




    private List<Usuario> ObtenerContactos() {
        List<Usuario> usuarios = new ArrayList<>();

        usuarios.add(new Usuario("JOSE", "RIVERA"));
        usuarios.add(new Usuario("micaelo", "RIVERA"));
        usuarios.add(new Usuario("GUSTAVO", "RIVERA"));
        usuarios.add(new Usuario("ROSA", "RIVERA"));
        usuarios.add(new Usuario("ANA", "RIVERA"));


        return usuarios;
    }




    public void VolverContactosAmensaje(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}