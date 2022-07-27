package com.corp.adrisher.appcamera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ImageView fondo;
    private ImageButton btncamera, back;
    private String ruta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn();
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.
                    this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, 1000);
        }
    }

    private void btn(){
        fondo = (ImageView) findViewById(R.id.imgFondo);
        btncamera = (ImageButton) findViewById(R.id.btncamara);
        back = (ImageButton) findViewById(R.id.btnRegresar);
        Eventos_Botones();
    }

    private void Eventos_Botones(){
        btncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camara();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    String currentPhotoPath;
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;
    private void TomarPicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void camara(){
        Intent inte = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(inte.resolveActivity(getPackageManager()) != null){
            File fotoArchivo = null;
            try {
                fotoArchivo = GuardarImagen();
            }catch (IOException ex){
                Log.e("error", ex.toString());
            }
            if (fotoArchivo != null){
                Uri uri = FileProvider.getUriForFile(this, "com.ingelist.mycamera.fileprovider", fotoArchivo);
                inte.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(inte, 1);
            }

        }
    }

    static final int REQUEST_IMAGE_CAPTURE =1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            Bitmap imageBitmap = BitmapFactory.decodeFile(ruta);
            fondo.setImageBitmap(imageBitmap);
        }
    }
    private File GuardarImagen() throws IOException{
        String nombreFoto = "foto_";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File foto = File.createTempFile(nombreFoto, ".jpg", directorio);
        ruta = foto.getAbsolutePath();
        return foto;
    }
}