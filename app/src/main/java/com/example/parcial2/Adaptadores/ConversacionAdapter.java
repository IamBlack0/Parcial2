package com.example.parcial2.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parcial2.Entidades.Conversacion;
import com.example.parcial2.R;

import java.util.List;

public class ConversacionAdapter extends BaseAdapter {

    private List<Conversacion> conversaciones;
    private LayoutInflater inflater;

    public ConversacionAdapter(Context context, List<Conversacion> conversaciones) {
        this.conversaciones = conversaciones;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return conversaciones.size();
    }

    @Override
    public Object getItem(int position) {
        return conversaciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_conversacion, parent, false);
        }

        Conversacion conversacion = conversaciones.get(position);

        ImageView profileImage = convertView.findViewById(R.id.ImagenPerfil);
        TextView nombreTextView = convertView.findViewById(R.id.nombreEnChat);
        TextView apellidoTextView = convertView.findViewById(R.id.apellidoEnChat);
        TextView ultimoMensajeTextView = convertView.findViewById(R.id.message);

        // Aquí puedes ajustar la imagen de perfil según sea necesario
        profileImage.setImageResource(R.drawable.baseline_person_24);
        nombreTextView.setText(conversacion.getNombre());
        apellidoTextView.setText(conversacion.getApellido());
        ultimoMensajeTextView.setText(conversacion.getUltimoMensaje());

        return convertView;
    }
}
