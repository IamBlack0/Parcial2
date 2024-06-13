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
import com.squareup.picasso.Picasso;

import java.util.List;

public class ContactoAdapter extends ArrayAdapter<Contacto> {

    List<Contacto> contactos;
    int usuarioId;

    public ContactoAdapter(Context context, List<Contacto> datos, int usuarioId) {
        super(context, R.layout.listview_contactos, datos);
        this.contactos = datos;
        this.usuarioId = usuarioId;
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
            Picasso.get().load(contacto.getImagenId()).into(imageView);
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
                    intent.putExtra("usuarioId", usuarioId); // Pasar el ID del usuario actual

                    if (usuarioId == 1) {
                        intent.putExtra("contactoId", 1); // Usuario 1 siempre envía como contacto 1
                        intent.putExtra("destinatarioContactoId", contacto.getId()); // Pasar el ID del contacto destinatario
                    } else {
                        if (contacto.getId() == 1) {
                            intent.putExtra("contactoId", 2); // Usuario 2 envía como contacto 2 al contacto 1
                            intent.putExtra("destinatarioContactoId", 1); // Siempre envía al contacto 1
                        } else {
                            intent.putExtra("contactoId", contacto.getId()); // Usuario 2 envía como el contacto seleccionado
                            intent.putExtra("destinatarioContactoId", 1); // Siempre envía al contacto 1
                        }
                    }
                    getContext().startActivity(intent);
                }
            });
        }

        return convertView;
    }
}
