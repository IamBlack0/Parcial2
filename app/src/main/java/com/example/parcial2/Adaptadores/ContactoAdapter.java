package com.example.parcial2.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.parcial2.Chat;
import com.example.parcial2.Entidades.Usuario;
import com.example.parcial2.R;

import java.util.List;

public class ContactoAdapter extends ArrayAdapter<Usuario> {

    List<Usuario> opciones;

    public ContactoAdapter(Context context, List<Usuario> datos) {
        super(context, R.layout.listview_contactos, datos);
        this.opciones = datos;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Usuario usuario = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_contactos, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.profile_image);
        TextView nombreTextView = convertView.findViewById(R.id.nombreEnChat);
        TextView apellidoTextView = convertView.findViewById(R.id.apellidoEnChat);

        imageView.setImageResource(usuario.getImagenId());
        nombreTextView.setText(usuario.getNombre());
        apellidoTextView.setText(usuario.getApellido());

        // Agregar OnClickListener para manejar el clic en cada item
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Chat.class);
                intent.putExtra("nombre", usuario.getNombre());
                intent.putExtra("apellido", usuario.getApellido());
                intent.putExtra("imagenId", usuario.getImagenId());
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}
