package com.example.parcial2.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.parcial2.Entidades.Usuario;
import com.example.parcial2.R;

import java.util.ArrayList;
import java.util.List;

public class ContactoAdapter extends ArrayAdapter<Usuario> {

    List<Usuario> opciones = new ArrayList<>();

    public ContactoAdapter(Context context, List<Usuario> datos) {
        super(context, R.layout.listview_contactos, datos);
        this.opciones = datos;
    }

    @NonNull
    public View getView(int pos, View view, @NonNull ViewGroup viewGroup){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        @SuppressLint("ViewHolder") View item = inflater.inflate(R.layout.listview_contactos,null);

        TextView nombre = item.findViewById(R.id.nombreEnChat);
        nombre.setText(opciones.get(pos).getNombre());

        TextView apellido = item.findViewById(R.id.apellidoEnChat);
        apellido.setText(opciones.get(pos).getApellido());


        return item;
    }

}
