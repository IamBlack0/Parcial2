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
import com.example.parcial2.Entidades.Conversacion;
import com.example.parcial2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ConversacionAdapter extends ArrayAdapter<Conversacion> {

    public ConversacionAdapter(Context context, List<Conversacion> conversaciones) {
        super(context, R.layout.listview_conversacion, conversaciones);
    }

    static class ViewHolder {
        ImageView imageView;
        TextView nombreTextView;
        TextView timestampTextView;
        TextView messageTextView;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_conversacion, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.ImagenPerfil);
            holder.nombreTextView = convertView.findViewById(R.id.nombreEnChat);
            holder.timestampTextView = convertView.findViewById(R.id.timestamp);
            holder.messageTextView = convertView.findViewById(R.id.message);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Conversacion conversacion = getItem(position);
        if (conversacion != null) {
            // Usar Picasso para cargar la imagen desde una URL o ruta
            Picasso.get().load(conversacion.getImagenId()).into(holder.imageView);
            holder.nombreTextView.setText(conversacion.getNombre());
            holder.timestampTextView.setText(conversacion.getTimestamp());
            holder.messageTextView.setText(conversacion.getUltimoMensaje());

            // Agregar OnClickListener para manejar el clic en cada item
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), Chat.class);
                    intent.putExtra("nombre", conversacion.getNombre());
                    intent.putExtra("apellido", conversacion.getApellido());
                    intent.putExtra("imagenId", conversacion.getImagenId());
                    intent.putExtra("usuarioId", conversacion.getUsuarioId());
                    intent.putExtra("contactoId", conversacion.getContactoId());
                    intent.putExtra("destinatarioContactoId", conversacion.getDestinatarioContactoId());

                    // Ajustar la lógica para pasar los IDs correctos
                    if (conversacion.getUsuarioId() == 1) {
                        intent.putExtra("contactoId", 1); // Usuario 1 siempre envía como contacto 1
                        intent.putExtra("destinatarioContactoId", conversacion.getContactoId()); // Pasar el ID del contacto destinatario
                    } else {
                        if (conversacion.getContactoId() == 1) {
                            intent.putExtra("contactoId", 2); // Usuario 2 envía como contacto 2 al contacto 1
                            intent.putExtra("destinatarioContactoId", 1); // Siempre envía al contacto 1
                        } else {
                            intent.putExtra("contactoId", 2); // Usuario 2 envía como contacto 2
                            intent.putExtra("destinatarioContactoId", conversacion.getContactoId()); // Pasar el ID del contacto destinatario
                        }
                    }

                    getContext().startActivity(intent);
                }
            });
        }

        return convertView;
    }
}
