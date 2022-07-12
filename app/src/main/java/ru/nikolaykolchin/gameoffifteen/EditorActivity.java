package ru.nikolaykolchin.gameoffifteen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;

public class EditorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

//        ImageView imageView = findViewById(R.id.tmpImage);
//        Intent intent = getIntent();
//        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");
//        imageView.setImageBitmap(bitmap);

        Bitmap bitmap = null;
        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(filename);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageView imageView = findViewById(R.id.tmpImage);
        imageView.setImageBitmap(bitmap);

    }

    public void dismiss(View view) {
        finish();
    }
}
