package ru.nikolaykolchin.gameoffifteen;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    public int emptyPos;
    public ImageView emptySquare;
    public ArrayList<ImageView> imageList;
    public ArrayList<Object> tagList;
    public ArrayList<Drawable> drawableList;
    private final String TAG = "Жизненный цикл";
    public static final String APP_PREFERENCES = "my_settings";
    public static final String CHECK_STRING = "01020304050607080910111213141516";
    SharedPreferences mSettings;
    static final int GALLERY_REQUEST = 1;
    public static Uri selectedImage;
    public static Matrix imgMatrix;
    Bitmap bitmap;
    public static boolean isEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initLists();
        imgMatrix = new Matrix();
        isEdit = false;

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean hasVisitedUser = mSettings.getBoolean("hasVisitedUser", false);

        if (!hasVisitedUser) {
            FragmentManager manager = getSupportFragmentManager();
            DialogUserInfo dialogUserInfo = new DialogUserInfo();
            dialogUserInfo.show(manager, "dialogUserInfo");
            dialogUserInfo.setCancelable(false);

            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean("hasVisitedUser", true);
            editor.apply();
        }

        Log.i(TAG, "onCreate()");
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(TAG, "onResume()");
        if (isEdit) {
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                    imgMatrix, true);
            imgMatrix.reset();

            int side = bitmap.getHeight() / 4;
            imageList.get(0).setImageBitmap(Bitmap.createBitmap(bitmap, 0, 0, side, side));
            imageList.get(1).setImageBitmap(Bitmap.createBitmap(bitmap, side, 0, side, side));
            imageList.get(2).setImageBitmap(Bitmap.createBitmap(bitmap, side * 2, 0, side, side));
            imageList.get(3).setImageBitmap(Bitmap.createBitmap(bitmap, side * 3, 0, side, side));
            imageList.get(4).setImageBitmap(Bitmap.createBitmap(bitmap, 0, side, side, side));
            imageList.get(5).setImageBitmap(Bitmap.createBitmap(bitmap, side, side, side, side));
            imageList.get(6).setImageBitmap(Bitmap.createBitmap(bitmap, side * 2, side, side, side));
            imageList.get(7).setImageBitmap(Bitmap.createBitmap(bitmap, side * 3, side, side, side));
            imageList.get(8).setImageBitmap(Bitmap.createBitmap(bitmap, 0, side * 2, side, side));
            imageList.get(9).setImageBitmap(Bitmap.createBitmap(bitmap, side, side * 2, side, side));
            imageList.get(10).setImageBitmap(Bitmap.createBitmap(bitmap, side * 2, side * 2, side, side));
            imageList.get(11).setImageBitmap(Bitmap.createBitmap(bitmap, side * 3, side * 2, side, side));
            imageList.get(12).setImageBitmap(Bitmap.createBitmap(bitmap, 0, side * 3, side, side));
            imageList.get(13).setImageBitmap(Bitmap.createBitmap(bitmap, side, side * 3, side, side));
            imageList.get(14).setImageBitmap(Bitmap.createBitmap(bitmap, side * 2, side * 3, side, side));

            drawableList.clear();
            for (ImageView iv : imageList) {
                drawableList.add(iv.getDrawable());
                iv.setClickable(true);
            }
            isEdit = false;
            // TODO random here

            startNewGame();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i(TAG, "onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i(TAG, "onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "onDestroy()");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        bitmap = null;

        if (requestCode == GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                selectedImage = imageReturnedIntent.getData();
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
//                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
//                        bitmap.getHeight(), imgMatrix, true);
//                System.out.println("Матрица в user " + imgMatrix);
//
//                int side = bitmap.getHeight() / 4;
//                imageList.get(0).setImageBitmap(Bitmap.createBitmap(bitmap, 0, 0, side, side));
//                imageList.get(1).setImageBitmap(Bitmap.createBitmap(bitmap, side, 0, side, side));
//                imageList.get(2).setImageBitmap(Bitmap.createBitmap(bitmap, side * 2, 0, side, side));
//                imageList.get(3).setImageBitmap(Bitmap.createBitmap(bitmap, side * 3, 0, side, side));
//                imageList.get(4).setImageBitmap(Bitmap.createBitmap(bitmap, 0, side, side, side));
//                imageList.get(5).setImageBitmap(Bitmap.createBitmap(bitmap, side, side, side, side));
//                imageList.get(6).setImageBitmap(Bitmap.createBitmap(bitmap, side * 2, side, side, side));
//                imageList.get(7).setImageBitmap(Bitmap.createBitmap(bitmap, side * 3, side, side, side));
//                imageList.get(8).setImageBitmap(Bitmap.createBitmap(bitmap, 0, side * 2, side, side));
//                imageList.get(9).setImageBitmap(Bitmap.createBitmap(bitmap, side, side * 2, side, side));
//                imageList.get(10).setImageBitmap(Bitmap.createBitmap(bitmap, side * 2, side * 2, side, side));
//                imageList.get(11).setImageBitmap(Bitmap.createBitmap(bitmap, side * 3, side * 2, side, side));
//                imageList.get(12).setImageBitmap(Bitmap.createBitmap(bitmap, 0, side * 3, side, side));
//                imageList.get(13).setImageBitmap(Bitmap.createBitmap(bitmap, side, side * 3, side, side));
//                imageList.get(14).setImageBitmap(Bitmap.createBitmap(bitmap, side * 2, side * 3, side, side));
//
//                drawableList.clear();
//                for (ImageView iv : imageList) {
//                    drawableList.add(iv.getDrawable());
//                    iv.setClickable(true);
//                }
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

//        String mDrawableName = "myimg";
//        int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());

        imageList = new ArrayList<>();
        imageList.add(findViewById(R.id.pos1));
        imageList.add(findViewById(R.id.pos2));
        imageList.add(findViewById(R.id.pos3));
        imageList.add(findViewById(R.id.pos4));
        imageList.add(findViewById(R.id.pos5));
        imageList.add(findViewById(R.id.pos6));
        imageList.add(findViewById(R.id.pos7));
        imageList.add(findViewById(R.id.pos8));
        imageList.add(findViewById(R.id.pos9));
        imageList.add(findViewById(R.id.pos10));
        imageList.add(findViewById(R.id.pos11));
        imageList.add(findViewById(R.id.pos12));
        imageList.add(findViewById(R.id.pos13));
        imageList.add(findViewById(R.id.pos14));
        imageList.add(findViewById(R.id.pos15));
        imageList.add(findViewById(R.id.pos16));
        emptyPos = 16;
        emptySquare = imageList.get(15);

        tagList = new ArrayList<>();
        drawableList = new ArrayList<>();
        for (ImageView iv : imageList) {
            tagList.add(iv.getTag());
            drawableList.add(iv.getDrawable());
            iv.setClickable(false);
        }
    }

    public boolean isPossibleToMove(int tapPosition) {
        if (tapPosition == emptyPos) {
            return false;
        }
        switch (tapPosition) {
            case 6:
            case 7:
            case 10:
            case 11:
                return Math.abs(tapPosition - emptyPos) == 4
                        || Math.abs(tapPosition - emptyPos) == 1;
            case 2:
            case 3:
                return tapPosition + 4 == emptyPos || Math.abs(tapPosition - emptyPos) == 1;
            case 5:
            case 9:
                return tapPosition + 1 == emptyPos || Math.abs(tapPosition - emptyPos) == 4;
            case 8:
            case 12:
                return tapPosition - 1 == emptyPos || Math.abs(tapPosition - emptyPos) == 4;
            case 14:
            case 15:
                return tapPosition - 4 == emptyPos || Math.abs(tapPosition - emptyPos) == 1;
            case 1:
                return emptyPos == 2 || emptyPos == 5;
            case 4:
                return emptyPos == 3 || emptyPos == 8;
            case 13:
                return emptyPos == 9 || emptyPos == 14;
            case 16:
                return emptyPos == 12 || emptyPos == 15;
            default:
                Log.i("MainActivity", "Houston, we have a problem - in isPossibleToMove");
                return false;
        }
    }

    public void swapSquares(int tapPosition) {
        Drawable drawableMoving = imageList.get(tapPosition - 1).getDrawable();
        Drawable drawableEmpty = emptySquare.getDrawable();
        Object tmpTag = imageList.get(tapPosition - 1).getTag();

        emptySquare.setImageDrawable(drawableMoving);
        imageList.get(tapPosition - 1).setImageDrawable(drawableEmpty);

        imageList.get(tapPosition - 1).setTag(emptySquare.getTag());
        emptySquare.setTag(tmpTag);
        tagList.set(emptyPos - 1, imageList.get(emptyPos - 1).getTag());
        tagList.set(tapPosition - 1, imageList.get(tapPosition - 1).getTag());

        emptySquare = imageList.get(tapPosition - 1);
        emptyPos = tapPosition;
    }

    public void shuffleSquares(List<Integer> randomField) {
        tagList.clear();
        for (int i = 0; i < 16; i++) {
            tagList.add(CHECK_STRING.substring(i * 2, i * 2 + 2));
        }
        for (int i = 0; i < 16; i++) {
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

    public boolean isWin() {
        StringBuilder currentField = new StringBuilder();
        for (Object obj : tagList) {
            currentField.append(obj.toString());
        }
        return currentField.toString().equals(CHECK_STRING);
    }

    public boolean isSolvable(List<Integer> randomField) {
        int n = 0;
        int e;
        for (int i = 0; i < 16; i++) {
            if (randomField.get(i) == 16) {
                continue;
            }
            for (int j = i + 1; j < 16; j++) {
                n = (randomField.get(i) > randomField.get(j)) ? n + 1 : n;
            }
        }
        int indexE = randomField.indexOf(16);
        if (indexE <= 3) {
            e = 1;
        } else if (indexE <= 7) {
            e = 2;
        } else if (indexE <= 11) {
            e = 3;
        } else {
            e = 4;
        }
        return (n + e) % 2 == 0;
    }

    public void clickOnField(View view) {
        int tapPosition = 0;

        for (int i = 0; i < 16; i++) {
            if (view.getId() == imageList.get(i).getId()) {
                tapPosition = i + 1;
            }
        }

        if (!isPossibleToMove(tapPosition)) {
            return;
        }
        swapSquares(tapPosition);

        if (tapPosition == 16 && isWin()) {
            FragmentManager manager = getSupportFragmentManager();
            DialogUserWin dialogUserWin = new DialogUserWin();
            dialogUserWin.show(manager, "dialogUserWin");
            dialogUserWin.setCancelable(false);
        }
    }

    public void finishActivity() {
        finish();
    }

    public void startNewGame() {
        List<Integer> randomField = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            randomField.add(i + 1);
        }
        Collections.shuffle(randomField);
        System.out.println(randomField);
        while (!isSolvable(randomField)) {
            Collections.shuffle(randomField);
        }
        System.out.println(randomField);
        shuffleSquares(randomField);
    }
}
