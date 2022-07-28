package ru.nikolaykolchin.gameoffifteen;

import static ru.nikolaykolchin.gameoffifteen.CommonMethods.*;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int FIELD_SIZE = 16;
    private int emptyPos;
    private ImageView emptySquare;
    private ArrayList<ImageView> imageList;
    private ArrayList<Object> tagList;
    private ArrayList<Drawable> drawableList;
    private SharedPreferences mSettings;
    private static final int GALLERY_REQUEST = 1;
    public static Matrix imgMatrix;
    private Bitmap bitmap;
    public static boolean isEdit;
    private SoundPool mSoundPool;
    private int sound;
    private AssetManager mAssetManager;
    private int mStreamID;
    private static final int REQUEST_STORAGE = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initLists();
        imgMatrix = new Matrix();
        isEdit = false;

        mSettings = getSharedPreferences("my_settings", Context.MODE_PRIVATE);
        if (!mSettings.getBoolean("hasVisitedUser", false)) {
            FragmentManager manager = getSupportFragmentManager();
            DialogUserInfo dialogUserInfo = new DialogUserInfo();
            dialogUserInfo.show(manager, "dialogUserInfo");
            dialogUserInfo.setCancelable(false);

            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean("hasVisitedUser", true);
            editor.apply();
        }
    }

    private void createSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    private int playSound(int sound) {
        if (sound > 0) {
            mStreamID = mSoundPool.play(sound, 1, 1, 1, 0, 1);
        }
        return mStreamID;
    }

    private int loadSound(String fileName) {
        AssetFileDescriptor afd;
        try {
            afd = mAssetManager.openFd(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Не могу загрузить файл " + fileName,
                    Toast.LENGTH_SHORT).show();
            return -1;
        }
        return mSoundPool.load(afd, 1);
    }

    @Override
    protected void onResume() {
        super.onResume();

        createSoundPool();
        mAssetManager = getAssets();
        sound = loadSound("soundwin.ogg");

        if (isEdit) {
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                    imgMatrix, true);
            imgMatrix.reset();
            loadBitmapToImageList(imageList, bitmap);

            int randomNum = (int) (Math.random() * 9 + 1);
            String memName = "mem" + randomNum;
            int resID = getResources().getIdentifier(memName, "drawable", getPackageName());
            imageList.get(15).setImageResource(resID);

            drawableList.clear();
            for (ImageView iv : imageList) {
                drawableList.add(iv.getDrawable());
                iv.setClickable(true);
            }
            isEdit = false;

            startNewGame();
        }
    }

    @Override
    protected void onPause() {
        mSoundPool.release();
        mSoundPool = null;

        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        bitmap = null;

        if (requestCode == GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = imageReturnedIntent.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                assert bitmap != null;
                bitmap = cropBitmap(bitmap);

                Bitmap compressBitmap = bitmap;

                try {
                    String filename = "bitmap.jpg";
                    FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
                    compressBitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);

                    Intent intent = new Intent(this, EditorActivity.class);
                    intent.putExtra("image", filename);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Bitmap cropBitmap(Bitmap bitmap) {
        int smallerSide = Math.min(bitmap.getWidth(), bitmap.getHeight());
        int x = (bitmap.getWidth() > bitmap.getHeight()) ? (bitmap.getWidth() - smallerSide) / 2 : 0;
        int y = (bitmap.getWidth() < bitmap.getHeight()) ? (bitmap.getHeight() - smallerSide) / 2 : 0;
        bitmap = Bitmap.createBitmap(bitmap, x, y, smallerSide, smallerSide);
        return bitmap;
    }

    protected void initLists() {
        imageList = new ArrayList<>();
        for (int i = 0; i < FIELD_SIZE; i++) {
            String posName = "pos" + (i + 1);
            int resID = getResources().getIdentifier(posName, "id", getPackageName());
            imageList.add(findViewById(resID));
        }

        emptyPos = 16;
        emptySquare = imageList.get(15);

        tagList = new ArrayList<>();
        drawableList = new ArrayList<>();
        for (ImageView iv : imageList) {
            tagList.add(iv.getTag());
            drawableList.add(iv.getDrawable());
            iv.setOnClickListener(this);
            iv.setClickable(false);
        }
        Button button = findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    public void swapSquares(int tapPosition) {
        Drawable drawableMoving = imageList.get(tapPosition - 1).getDrawable();
        Object tmpTag = imageList.get(tapPosition - 1).getTag();

        imageList.get(tapPosition - 1).setImageDrawable(emptySquare.getDrawable());
        emptySquare.setImageDrawable(drawableMoving);

        imageList.get(tapPosition - 1).setTag(emptySquare.getTag());
        emptySquare.setTag(tmpTag);
        tagList.set(emptyPos - 1, imageList.get(emptyPos - 1).getTag());
        tagList.set(tapPosition - 1, imageList.get(tapPosition - 1).getTag());

        emptySquare = imageList.get(tapPosition - 1);
        emptyPos = tapPosition;
    }

    public void shuffleSquares(List<Integer> randomField) {
        String checkString = "01020304050607080910111213141516";
        tagList.clear();
        for (int i = 0; i < FIELD_SIZE; i++) {
            tagList.add(checkString.substring(i * 2, i * 2 + 2));
        }
        for (int i = 0; i < FIELD_SIZE; i++) {
            imageList.get(i).setImageDrawable(drawableList.get(randomField.get(i) - 1));
            imageList.get(i).setTag(tagList.get(randomField.get(i) - 1));
            if (randomField.get(i) == 16) {
                emptySquare = imageList.get(i);
                emptyPos = i + 1;
            }
        }
        tagList.clear();
        for (ImageView iv : imageList) {
            tagList.add(iv.getTag());
        }
    }

    public void finishActivity() {
        finish();
    }

    public void startNewGame() {
        List<Integer> randomField = new ArrayList<>();
        for (int i = 0; i < FIELD_SIZE; i++) {
            randomField.add(i + 1);
        }

        Collections.shuffle(randomField);
        while (!isSolvable(randomField)) {
            Collections.shuffle(randomField);
        }

        shuffleSquares(randomField);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            requestPermission();
            return;
        }

        int tapPosition = 0;
        for (int i = 0; i < FIELD_SIZE; i++) {
            if (view.getId() == imageList.get(i).getId()) {
                tapPosition = i + 1;
            }
        }

        if (!isPossibleToMove(tapPosition, emptyPos)) {
            return;
        }
        if (mSettings.getBoolean("isVibrationOn", false)) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(10);
        }
        swapSquares(tapPosition);

        if (tapPosition == 16 && isWin(tagList)) {
            if (mSettings.getBoolean("isSoundOn", false)) playSound(sound);

            FragmentManager manager = getSupportFragmentManager();
            DialogUserWin dialogUserWin = new DialogUserWin();
            dialogUserWin.show(manager, "dialogUserWin");
            dialogUserWin.setCancelable(false);
        }
    }

    private void requestPermission() {
        String storagePermission = Manifest.permission.READ_EXTERNAL_STORAGE;
        int hasPermission = ActivityCompat.checkSelfPermission(this, storagePermission);
        String[] permissions = new String[]{storagePermission};
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_STORAGE);
        } else {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),
                        "Разрешение получено!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Вы отклонили запрос", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}