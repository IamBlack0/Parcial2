package com.example.parcial2.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.parcial2.Entidades.Mensaje;
import com.example.parcial2.R;

import java.util.List;

public class MensajeAdapter extends BaseAdapter {

    private List<Mensaje> mensajes;
    private LayoutInflater inflater;

    public MensajeAdapter(Context context, List<Mensaje> mensajes) {
        this.mensajes = mensajes;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mensajes.size();
    }

    @Override
    public Object getItem(int position) {
        return mensajes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2; // Dos tipos de vistas: mensajes enviados y recibidos
    }

    @Override
    public int getItemViewType(int position) {
        Mensaje mensaje = mensajes.get(position);
        return mensaje.esEnviado() ? 0 : 1; // 0 para enviado, 1 para recibido
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Mensaje mensaje = mensajes.get(position);
        if (convertView == null) {
            if (getItemViewType(position) == 0) {
                convertView = inflater.inflate(R.layout.listview_chat_derecha, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.listview_chat_izquierda, parent, false);
            }
        }

        TextView texto = convertView.findViewById(R.id.message_text);
        TextView hora = convertView.findViewById(R.id.message_time);

        texto.setText(mensaje.getTexto());
        hora.setText(mensaje.getHora());

        return convertView;
    }
}
