package com.example.viasegura.NEGOCIOS;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.viasegura.R;
import com.squareup.picasso.Picasso;

import java.io.File;

public class DenunciaAdapter extends BaseAdapter {

    private Context mContext;
    private Cursor mCursor;

    public DenunciaAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            // Inflar el layout del item de denuncia si no existe una vista reutilizable
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_denuncia, parent, false);

            // Crear un ViewHolder para mantener las referencias a las vistas internas
            holder = new ViewHolder();
            holder.tituloTextView = convertView.findViewById(R.id.text_titulo);
            holder.fechaTextView = convertView.findViewById(R.id.text_fecha);
            holder.horaTextView = convertView.findViewById(R.id.text_hora); // Añadir referencia al TextView de la hora
            holder.descripcionTextView = convertView.findViewById(R.id.text_descripcion);
            holder.latitudTextView = convertView.findViewById(R.id.text_latitud);
            holder.longitudTextView = convertView.findViewById(R.id.text_longitud);
            holder.fotoImageView = convertView.findViewById(R.id.image_foto);
            holder.categoriaTextView = convertView.findViewById(R.id.text_categoria); // Agregar referencia al TextView de la categoría

            convertView.setTag(holder);
        } else {
            // Si convertView ya existe, obtener el ViewHolder desde el tag
            holder = (ViewHolder) convertView.getTag();
        }

        // Mover el cursor a la posición correcta
        mCursor.moveToPosition(position);

        // Obtener los datos de la denuncia desde el cursor
        int idDenuncia = mCursor.getInt(mCursor.getColumnIndexOrThrow("idDenuncia"));
        String fecha = mCursor.getString(mCursor.getColumnIndexOrThrow("fecha"));
        String hora = mCursor.getString(mCursor.getColumnIndexOrThrow("hora")); // Obtener la hora desde el cursor
        String descripcion = mCursor.getString(mCursor.getColumnIndexOrThrow("descripcion"));
        double latitud = mCursor.getDouble(mCursor.getColumnIndexOrThrow("latitud"));
        double longitud = mCursor.getDouble(mCursor.getColumnIndexOrThrow("longitud"));
        String fotoPath = mCursor.getString(mCursor.getColumnIndexOrThrow("foto"));
        int idCategoriaIndex = mCursor.getColumnIndexOrThrow("nombre_categoria");
        // Obtener el nombre de la categoría desde el cursor
        String nombreCategoria = mCursor.getString(mCursor.getColumnIndexOrThrow("nombre_categoria"));


        // Obtener el nombre de la categoría

        // Mostrar los datos en las vistas correspondientes
        holder.tituloTextView.setText("Título: " + nombreCategoria); // Mostrar el nombre de la categoría como título
        holder.fechaTextView.setText("Fecha: " + fecha);
        holder.horaTextView.setText("Hora: " + hora); // Establecer la hora en el TextView
        holder.descripcionTextView.setText("Descripción: " + descripcion);
        holder.latitudTextView.setText("Latitud: " + latitud);
        holder.longitudTextView.setText("Longitud: " + longitud);

        // Cargar la imagen usando Picasso si hay una ruta válida
        if (fotoPath != null && !fotoPath.isEmpty()) {
            Picasso.get().load(new File(fotoPath)).into(holder.fotoImageView);
        }

        return convertView;
    }

    // Método para obtener el nombre de la categoría basado en el ID
    private String obtenerNombreCategoria(int idCategoria) {
        CDenuncias denuncias = new CDenuncias();
        return denuncias.obtenerNombreCategoria(mContext, idCategoria);
    }

    // Clase ViewHolder para mantener las referencias a las vistas internas del item
    static class ViewHolder {
        TextView tituloTextView;
        TextView fechaTextView;
        TextView horaTextView; // Añadir este TextView
        TextView descripcionTextView;
        TextView latitudTextView;
        TextView longitudTextView;
        ImageView fotoImageView;
        TextView categoriaTextView; // Agregar TextView para mostrar el nombre de la categoría
    }

}
