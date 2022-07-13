package ru.nikolaykolchin.gameoffifteen;

import static ru.nikolaykolchin.gameoffifteen.UserActivity.imgMatrix;
import static ru.nikolaykolchin.gameoffifteen.UserActivity.isEdit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;

public class EditorActivity extends AppCompatActivity {
    Bitmap bitmap;
    ImageView imageView;
    Matrix matrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        matrix = new Matrix();

        bitmap = null;
        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(filename);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageView = findViewById(R.id.tmpImage);
        imageView.setImageBitmap(bitmap);
        isEdit = true;

    }

    public void dismiss(View view) {
        finish();
    }

    public void spin(View view) {
        matrix.postRotate(90);
        Bitmap spinBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        imageView.setImageBitmap(spinBitmap);
        imgMatrix = matrix;
    }

    public void mirrorVer(View view) {
        float[] mirror = {-1, 0, 0, 0, 1, 0, 0, 0, 1};
        Matrix mirrorMatrix = new Matrix();
        mirrorMatrix.setValues(mirror);
        matrix.postConcat(mirrorMatrix);
        Bitmap spinBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        imageView.setImageBitmap(spinBitmap);
        imgMatrix = matrix;
    }

    public void mirrorHor(View view) {
        float[] mirror = {1, 0, 0, 0, -1, 0, 0, 0, 1};
        Matrix mirrorMatrix = new Matrix();
        mirrorMatrix.setValues(mirror);
        matrix.postConcat(mirrorMatrix);
        Bitmap spinBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        imageView.setImageBitmap(spinBitmap);
        imgMatrix = matrix;
    }
}