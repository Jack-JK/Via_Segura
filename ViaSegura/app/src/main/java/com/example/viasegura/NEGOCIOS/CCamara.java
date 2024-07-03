package com.example.viasegura.NEGOCIOS;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.viasegura.PRESENTACION.FragmentDenunciar;

public class CCamara {

    private ActivityResultLauncher<Intent> cameraLauncher;
    private Fragment fragment;

    public CCamara(Fragment fragment) {
        this.fragment = fragment;
        cameraLauncher = fragment.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getExtras() != null) {
                            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                            ((FragmentDenunciar) fragment).mostrarFotoCapturada(imageBitmap);
                        }
                    }
                });
    }

    public void tomarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(intent);
    }
}
