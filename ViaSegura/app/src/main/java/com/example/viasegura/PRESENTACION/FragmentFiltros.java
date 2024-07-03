package com.example.viasegura.PRESENTACION;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.viasegura.NEGOCIOS.CMapaFiltroFecha;
import com.example.viasegura.NEGOCIOS.CMapaTodo;
import com.example.viasegura.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentFiltros#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFiltros extends Fragment {
    int idPersona;
    // Componentes para fecha de inicio
    private DatePickerDialog datePickerDialogInicio;
    private Button dateButtonInicio;

    // Componentes para fecha de fin
    private DatePickerDialog datePickerDialogFin;
    private Button dateButtonFin;;
    Button btnFechas, btnTodo;
    CMapaTodo ObjTodo = new CMapaTodo();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentFiltros() {
        // Required empty public constructor
    }

    public static FragmentFiltros newInstance(String param1, String param2) {
        FragmentFiltros fragment = new FragmentFiltros();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            idPersona = getArguments().getInt("idPersona", -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filtros, container, false);
        // Inicialización de fecha de inicio
        initDatePickerInicio();
        dateButtonInicio = view.findViewById(R.id.datepickerInicio);
        dateButtonInicio.setText(getTodaysDate());

        // Inicialización de fecha de fin
        initDatePickerFin();
        dateButtonFin = view.findViewById(R.id.datepickerFin);
        dateButtonFin.setText(getTodaysDate());

        // Configurar listeners para abrir los DatePickers
        dateButtonInicio.setOnClickListener(v -> openDatePickerInicio());
        dateButtonFin.setOnClickListener(v -> openDatePickerFin());

        // Botón para enviar los filtros a ActivityMapaFechas
        btnFechas = view.findViewById(R.id.btnFechas);
        btnFechas.setOnClickListener(this::GoReportes);


        return view;
    }

    public void GoReportes(View view) {
        String fechaInicio = dateButtonInicio.getText().toString();
        String fechaFin = dateButtonFin.getText().toString();

        // Crear una instancia de CMapaFiltroFecha para verificar si hay datos
        CMapaFiltroFecha filtroFecha = new CMapaFiltroFecha();

        // Verificar si hay datos
        boolean hayDatos = filtroFecha.HayDatos(getContext(), idPersona, fechaInicio, fechaFin);

        if (hayDatos) {
            Intent intent = new Intent(getActivity(), ActivityMapaFechas.class);
            intent.putExtra("idPersona", idPersona);
            intent.putExtra("fechaInicio", fechaInicio);
            intent.putExtra("fechaFin", fechaFin);
            startActivity(intent);
        } else {
            // Mostrar un mensaje indicando que no se encontraron datos
            new AlertDialog.Builder(getContext())
                    .setTitle("Sin Resultados")
                    .setMessage("No se encontraron datos para los filtros seleccionados.")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }
    public void VerTodo(View view) {
        // Validar si hay datos de denuncias para el idPersona
        boolean hayDatos = ObjTodo.HayDatos(getContext(), idPersona);

        if (hayDatos) {
            Intent intent = new Intent(getActivity(), ActivityMapaTodo.class);
            intent.putExtra("idPersona", idPersona);
            startActivity(intent);
        } else {
            // Mostrar un mensaje indicando que no se encontraron datos
            new AlertDialog.Builder(getContext())
                    .setTitle("Sin Resultados")
                    .setMessage("No se encontraron datos de denuncias para el usuario.")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }

    private void initDatePickerInicio() {
        DatePickerDialog.OnDateSetListener dateSetListenerInicio = (view, year, month, dayOfMonth) -> {
            month = month + 1;
            String date = makeDateString(dayOfMonth, month, year);
            dateButtonInicio.setText(date);
        };

        Calendar calendarInicio = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getTimeZone("America/La_Paz");
        calendarInicio.setTimeZone(timeZone);

        int yearInicio = calendarInicio.get(Calendar.YEAR);
        int monthInicio = calendarInicio.get(Calendar.MONTH);
        int dayInicio = calendarInicio.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialogInicio = new DatePickerDialog(requireActivity(), style, dateSetListenerInicio, yearInicio, monthInicio, dayInicio);
    }

    private void openDatePickerInicio() {
        datePickerDialogInicio.show();
    }

    private void initDatePickerFin() {
        DatePickerDialog.OnDateSetListener dateSetListenerFin = (view, year, month, dayOfMonth) -> {
            month = month + 1;
            String date = makeDateString(dayOfMonth, month, year);
            dateButtonFin.setText(date);
        };

        Calendar calendarFin = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getTimeZone("America/La_Paz");
        calendarFin.setTimeZone(timeZone);

        int yearFin = calendarFin.get(Calendar.YEAR);
        int monthFin = calendarFin.get(Calendar.MONTH);
        int dayFin = calendarFin.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialogFin = new DatePickerDialog(requireActivity(), style, dateSetListenerFin, yearFin, monthFin, dayFin);
    }

    private void openDatePickerFin() {
        datePickerDialogFin.show();
    }

    private String getTodaysDate() {
        Calendar Cal = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getTimeZone("America/La_Paz");
        Cal.setTimeZone(timeZone);

        int year = Cal.get(Calendar.YEAR);
        int month = Cal.get(Calendar.MONTH) + 1; // Enero es 0
        int day = Cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private String makeDateString(int day, int month, int year) {
        return day + " / " + month + " / " + year;
    }
}