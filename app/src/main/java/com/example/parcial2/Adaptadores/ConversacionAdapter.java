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

public class ConversacionAdapter extends ArrayAdapter<Usuario> {

    private List<Usuario> usuarios;
    private List<String> ultimosMensajes;
    private int currentUserID;

    public ConversacionAdapter(Context context, List<Usuario> usuarios, List<String> ultimosMensajes, int currentUserID) {
        super(context, R.layout.listview_conversacion, usuarios);
        this.usuarios = usuarios;
        this.ultimosMensajes = ultimosMensajes;
        this.currentUserID = currentUserID;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Usuario usuario = getItem(position);
        String ultimoMensaje = ultimosMensajes.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_conversacion, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.ImagenPerfil);
        TextView nombreTextView = convertView.findViewById(R.id.nombreEnChat);
        TextView messageTextView = convertView.findViewById(R.id.message);

        imageView.setImageResource(usuario.getImagenId());
        nombreTextView.setText(usuario.getNombre() + " " + usuario.getApellido());
        messageTextView.setText(ultimoMensaje);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Chat.class);
                intent.putExtra("usuarioId", currentUserID);
                intent.putExtra("nombre", usuario.getNombre());
                intent.putExtra("apellido", usuario.getApellido());
                intent.putExtra("imagenId", usuario.getImagenId());
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}
