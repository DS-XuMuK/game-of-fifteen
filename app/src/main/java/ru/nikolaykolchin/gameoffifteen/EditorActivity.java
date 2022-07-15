package ru.nikolaykolchin.gameoffifteen;

import static ru.nikolaykolchin.gameoffifteen.UserActivity.imgMatrix;
import static ru.nikolaykolchin.gameoffifteen.UserActivity.isEdit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {
    private Bitmap bitmap;
    private ImageView imageView;
    private Matrix matrix;

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
        ImageButton buttonSpin = findViewById(R.id.buttonSpin);
        buttonSpin.setOnClickListener(this);
        ImageButton buttonMirrorHor = findViewById(R.id.buttonMirrorHor);
        buttonMirrorHor.setOnClickListener(this);
        ImageButton buttonMirrorVer = findViewById(R.id.buttonMirrorVer);
        buttonMirrorVer.setOnClickListener(this);
        ImageButton buttonOK = findViewById(R.id.buttonOK);
        buttonOK.setOnClickListener(this);

        isEdit = true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonOK) {
            finish();
            return;
        }
        if (view.getId() == R.id.buttonSpin) {
            matrix.postRotate(90);
        }
        if (view.getId() == R.id.buttonMirrorVer) {
            float[] mirror = {-1, 0, 0, 0, 1, 0, 0, 0, 1};
            Matrix mirrorMatrix = new Matrix();
            mirrorMatrix.setValues(mirror);
            matrix.postConcat(mirrorMatrix);
        }
        if (view.getId() == R.id.buttonMirrorHor) {
            float[] mirror = {1, 0, 0, 0, -1, 0, 0, 0, 1};
            Matrix mirrorMatrix = new Matrix();
            mirrorMatrix.setValues(mirror);
            matrix.postConcat(mirrorMatrix);
        }
        Bitmap editBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        imageView.setImageBitmap(editBitmap);
        imgMatrix = matrix;
    }
}