package com.example.viasegura.PRESENTACION;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.viasegura.DATOS.CBaseDatos;
import com.example.viasegura.NEGOCIOS.CDenuncias;
import com.example.viasegura.R;

public class FragmentMisDenuncias extends Fragment {

    private static final String ARG_ID_PERSONA = "idPersona";
    private int idPersona;

    private Spinner spinnerCategoria;
    private Spinner spinnerIdDenuncia;
    private ImageView imgFoto;
    private TextView hora;
    private TextView fecha;
    private TextView descripcion;
    private ImageButton mapaBoton;
    private double[] latLong = new double[2];

    public FragmentMisDenuncias() {
        // Required empty public constructor
    }

    public static FragmentMisDenuncias newInstance(int idPersona) {
        FragmentMisDenuncias fragment = new FragmentMisDenuncias();
        Bundle args = new Bundle();
        args.putInt(ARG_ID_PERSONA, idPersona);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idPersona = getArguments().getInt(ARG_ID_PERSONA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mis_denuncias, container, false);

        spinnerCategoria = view.findViewById(R.id.categoriaspinner);
        spinnerIdDenuncia = view.findViewById(R.id.idDenuncia);
        imgFoto = view.findViewById(R.id.imgFotoMD);
        hora = view.findViewById(R.id.hora);
        fecha = view.findViewById(R.id.FechaMD);
        descripcion = view.findViewById(R.id.DescripcionMD);
        mapaBoton = view.findViewById(R.id.mapaboton);

        CDenuncias denunciasManager = new CDenuncias();
        denunciasManager.CargarCategoria(requireContext(), spinnerCategoria);

        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoriaSeleccionada = (String) parent.getItemAtPosition(position);
                int idCategoria = obtenerIdCategoriaPorNombre(categoriaSeleccionada);

                denunciasManager.CargarDenunciasPorCategoria(requireContext(), spinnerIdDenuncia, idPersona, idCategoria);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        spinnerIdDenuncia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int idDenuncia = Integer.parseInt((String) parent.getItemAtPosition(position));
                denunciasManager.obtenerDetallesDenuncia(requireContext(), idDenuncia, imgFoto, hora, fecha, descripcion, latLong);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        mapaBoton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MapaFragmentMisDenuncias.class);
            intent.putExtra("latitud", latLong[0]);
            intent.putExtra("longitud", latLong[1]);
            startActivity(intent);
        });

        return view;
    }

    private int obtenerIdCategoriaPorNombre(String nombreCategoria) {
        CBaseDatos dbHelper = new CBaseDatos(requireContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int idCategoria = -1;

        Cursor cursor = db.rawQuery("SELECT idCategoria FROM Categoria WHERE nombre = ?", new String[]{nombreCategoria});
        if (cursor.moveToFirst()) {
            idCategoria = cursor.getInt(0);
        }
        cursor.close();
        db.close();

        return idCategoria;
    }
}
