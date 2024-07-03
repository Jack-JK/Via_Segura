package com.example.viasegura.PRESENTACION;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.viasegura.R;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }//FIN ONCREATE
    public void GoLogin(View vista) {
        Intent obj = new Intent(this, ActivityLogin.class);
        startActivity(obj);
    }

    public void GoRegister(View vista) {
        Intent obj = new Intent(this, ActivityRegistro.class);
        startActivity(obj);
    }
}