package com.example.viasegura.PRESENTACION;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.viasegura.NEGOCIOS.CCamara;
import com.example.viasegura.NEGOCIOS.CDenuncias;
import com.example.viasegura.NEGOCIOS.CMapa;
import com.example.viasegura.NEGOCIOS.ImageUtils;
import com.example.viasegura.R;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class FragmentDenunciar extends Fragment {

    private static final int REQUEST_LOCATION = 2;
    // Para fecha
    private DatePickerDialog datePickerDialog;
    private Button dateButton, BGuardar;
    // Declaración del TimePicker y otros componentes
    private Button timeButton;
    EditText descripcionD;
    Spinner spinnerCategorias;
    ImageView ImgV;
    EditText catOtros;
    TextView latTextView, longTextView;
    CDenuncias ObjDen = new CDenuncias();
    private CCamara cCamara;
    private GridView gridView;
    int idPersona;

    private ImageView imageView;
    private Bitmap imageBitmap;
    private Bitmap fotoCapturada;

    public FragmentDenunciar() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cCamara = new CCamara(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_denunciar, container, false);
        // Fecha
        initDatePicker();
        dateButton = view.findViewById(R.id.datepicker);
        dateButton.setText(getTodaysDate());
        // Inicialización de componentes
        timeButton = view.findViewById(R.id.buttonForTimePicker);
        timeButton.setText(getCurrentTime());

        descripcionD = view.findViewById(R.id.descripcionD);
        spinnerCategorias = view.findViewById(R.id.Categoria);
        catOtros = view.findViewById(R.id.CatOtros);
        latTextView = view.findViewById(R.id.Lat);
        longTextView = view.findViewById(R.id.Long);
        ImgV = view.findViewById(R.id.imgFotoD);

        ObjDen.CargarCategoria(getActivity(), spinnerCategorias);

        dateButton.setOnClickListener(v -> openDatePicker(v));

        // Recibir el idPersona desde el Intent
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null && extras.containsKey("idPersona")) {
            idPersona = extras.getInt("idPersona");
        } else {
            // Manejar el caso donde idPersona no está presente
            Toast.makeText(getContext(), "Error: no se recibió el idPersona", Toast.LENGTH_SHORT).show();
            // Puedes decidir qué hacer en este caso, como cerrar la actividad o mostrar un mensaje de error.
            return view;
        }

        Button botonTomarFoto = view.findViewById(R.id.btnFoto);
        botonTomarFoto.setOnClickListener(v -> {
            // Llamar al método de CCamara para tomar la foto
            cCamara.tomarFoto();
        });

        Button botonGuardar = view.findViewById(R.id.guardar1);
        botonGuardar.setOnClickListener(v -> {
            if (fotoCapturada != null) {
                guardarDenuncia();
            } else {
                Toast.makeText(getContext(), "Debe capturar una foto antes de guardar la denuncia", Toast.LENGTH_SHORT).show();
            }
        });

        Button botonMostrarDenuncias = view.findViewById(R.id.boton_mostrar);
        botonMostrarDenuncias.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), MostrarDenunciasActivity.class);
            intent.putExtra("idPersona", idPersona); // Agregar el idPersona al intent
            requireActivity().startActivity(intent);
        });



        ImageButton imgButton = view.findViewById(R.id.botonMapa);
        imgButton.setOnClickListener(v -> {
            Intent mapIntent = new Intent(getActivity(), CMapa.class);
            startActivityForResult(mapIntent, REQUEST_LOCATION);
        });

        // Configurar el listener para el Spinner
        spinnerCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = (String) parent.getItemAtPosition(position);
                if ("Otro".equals(selectedCategory)) {
                    catOtros.setVisibility(View.VISIBLE); // Mostrar el campo "Otro"
                } else {
                    catOtros.setVisibility(View.GONE); // Ocultar el campo "Otro"
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                catOtros.setVisibility(View.GONE); // Ocultar el campo "Otro" si no hay nada seleccionado
            }
        });

        // Configurar el listener para el botón de hora
        timeButton.setOnClickListener(v -> openTimePicker());

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                double latitud = data.getDoubleExtra("latitud", 0.0);
                double longitud = data.getDoubleExtra("longitud", 0.0);
                latTextView.setText(String.valueOf(latitud));
                longTextView.setText(String.valueOf(longitud));
            }
        }
    }

    private void openTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (view, hourOfDay, minuteOfHour) -> {
            String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minuteOfHour);
            timeButton.setText(time); // Actualiza el texto del botón con la hora seleccionada
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getTimeZone("America/La_Paz");
        cal.setTimeZone(timeZone);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1; // Enero es 0
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            dateButton.setText(date);
        };

        Calendar cal = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getTimeZone("America/La_Paz");
        cal.setTimeZone(timeZone);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(requireActivity(), style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return day + " / " + month + " / " + year;
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    public void mostrarFotoCapturada(Bitmap imageBitmap) {
        // Aquí actualizas tu interfaz de usuario para mostrar la imagen capturada
        fotoCapturada = imageBitmap;
        ImageView imageView = getView().findViewById(R.id.imgFotoD);
        if (imageView != null) {
            imageView.setImageBitmap(imageBitmap);
        }
    }

    private int obtenerIdCategoriaSeleccionada() {
        String nombreCategoria = spinnerCategorias.getSelectedItem().toString();
        return ObjDen.getIdCategoriaPorNombre(getActivity(), nombreCategoria);
    }


    // Dentro de FragmentDenunciar.java

    private void guardarDenuncia() {
        // Obtener el título seleccionado del Spinner
        String titulo = spinnerCategorias.getSelectedItem().toString();

        // Obtener la fecha del botón de fecha
        String fecha = dateButton.getText().toString();

        // Obtener la hora del botón de hora
        String hora = timeButton.getText().toString();

        // Obtener latitud y longitud de los TextView
        String latitudStr = latTextView.getText().toString();
        String longitudStr = longTextView.getText().toString();
        double latitud = Double.parseDouble(latitudStr);
        double longitud = Double.parseDouble(longitudStr);

        // Obtener el id de la categoría seleccionada
        int idCategoria = obtenerIdCategoriaSeleccionada();

        // Guardar la imagen y obtener la ruta de la imagen guardada
        String imagePath = ImageUtils.saveImageToExternalStorage(getContext(), fotoCapturada);

        if (imagePath != null) {
            // Insertar la denuncia en la base de datos con los datos obtenidos
            CDenuncias cDenuncias = new CDenuncias();
            cDenuncias.insertarDenuncia(getContext(), imagePath, latitud, longitud, fecha, hora, descripcionD.getText().toString(), idPersona, idCategoria, 1);
            Toast.makeText(getContext(), "Se ha guardado correctamente la denuncia", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
        }
    }

}
