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
import com.example.parcial2.Entidades.Contacto;
import com.example.parcial2.R;

import java.util.List;

public class ContactoAdapter extends ArrayAdapter<Contacto> {

    List<Contacto> contactos;

    public ContactoAdapter(Context context, List<Contacto> datos) {
        super(context, R.layout.listview_contactos, datos);
        this.contactos = datos;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Contacto contacto = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_contactos, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.ImagenPerfil);
        TextView nombreTextView = convertView.findViewById(R.id.nombreEnChat);
        TextView apellidoTextView = convertView.findViewById(R.id.apellidoEnChat);

        if (contacto != null) {
            imageView.setImageResource(contacto.getImagenId());
            nombreTextView.setText(contacto.getNombre());
            apellidoTextView.setText(contacto.getApellido());

            // Agregar OnClickListener para manejar el clic en cada item
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), Chat.class);
                    intent.putExtra("nombre", contacto.getNombre());
                    intent.putExtra("apellido", contacto.getApellido());
                    intent.putExtra("imagenId", contacto.getImagenId());
                    intent.putExtra("contactoId", contacto.getId()); // Agrega el ID del contacto
                    getContext().startActivity(intent);
                }
            });

        }

        return convertView;
    }
    //xd
}
