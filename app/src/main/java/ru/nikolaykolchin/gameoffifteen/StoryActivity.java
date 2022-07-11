package ru.nikolaykolchin.gameoffifteen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StoryActivity extends AppCompatActivity implements View.OnClickListener {
    public int emptyPos;
    public ImageView emptySquare;
    public ArrayList<ImageView> imageList;
    public ArrayList<Object> tagList;
    public ArrayList<Drawable> drawableList;
    public static final String APP_PREFERENCES = "my_settings";
    public static final String CHECK_STRING = "01020304050607080910111213141516";
    private static final int LEVELS = 16;
    SharedPreferences mSettings;
    public static int currentLevel;
    private SoundPool mSoundPool;
    private AssetManager mAssetManager;
    private int sound;
    private int mStreamID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        initLists();

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentLevel = extras.getInt("currentLevel");
        } else {
            currentLevel = 0;
        }

        setTitle("Уровень " + currentLevel);
        loadImage();
        startNewGame();
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
        String soundName = "sound" + currentLevel + ".ogg";

        sound = loadSound(soundName);
    }

    @Override
    protected void onPause() {
        mSoundPool.release();
        mSoundPool = null;

        super.onPause();
    }

    public void loadImage() {
        Drawable drawable;
        switch (currentLevel) {
            case 1:
                drawable = getDrawable(R.drawable.story1);
                break;
            case 2:
                drawable = getDrawable(R.drawable.story2);
                break;
            case 3:
                drawable = getDrawable(R.drawable.story3);
                break;
            case 4:
                drawable = getDrawable(R.drawable.story4);
                break;
            case 5:
                drawable = getDrawable(R.drawable.story5);
                break;
            case 6:
                drawable = getDrawable(R.drawable.story6);
                break;
            case 7:
                drawable = getDrawable(R.drawable.story7);
                break;
            case 8:
                drawable = getDrawable(R.drawable.story8);
                break;
            case 9:
                drawable = getDrawable(R.drawable.story9);
                break;
            case 10:
                drawable = getDrawable(R.drawable.story10);
                break;
            case 11:
                drawable = getDrawable(R.drawable.story11);
                break;
            case 12:
                drawable = getDrawable(R.drawable.story12);
                break;
            case 13:
                drawable = getDrawable(R.drawable.story13);
                break;
            case 14:
                drawable = getDrawable(R.drawable.story14);
                break;
            case 15:
                drawable = getDrawable(R.drawable.story15);
                break;
            case 16:
                drawable = getDrawable(R.drawable.story16);
                break;
            default:
                throw new IllegalStateException("Unexpected value in loadImage: " + currentLevel);
        }

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
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
        imageList.get(10).setImageBitmap(Bitmap.createBitmap(bitmap, side * 2, side * 2, side,
                side));
        imageList.get(11).setImageBitmap(Bitmap.createBitmap(bitmap, side * 3, side * 2, side,
                side));
        imageList.get(12).setImageBitmap(Bitmap.createBitmap(bitmap, 0, side * 3, side, side));
        imageList.get(13).setImageBitmap(Bitmap.createBitmap(bitmap, side, side * 3, side, side));
        imageList.get(14).setImageBitmap(Bitmap.createBitmap(bitmap, side * 2, side * 3, side,
                side));

        int randomNum = (int) (Math.random() * 9 + 1);
        switch (randomNum) {
            case 1:
                imageList.get(15).setImageResource(R.drawable.mem1);
                break;
            case 2:
                imageList.get(15).setImageResource(R.drawable.mem2);
                break;
            case 3:
                imageList.get(15).setImageResource(R.drawable.mem3);
                break;
            case 4:
                imageList.get(15).setImageResource(R.drawable.mem4);
                break;
            case 5:
                imageList.get(15).setImageResource(R.drawable.mem5);
                break;
            case 6:
                imageList.get(15).setImageResource(R.drawable.mem6);
                break;
            case 7:
                imageList.get(15).setImageResource(R.drawable.mem7);
                break;
            case 8:
                imageList.get(15).setImageResource(R.drawable.mem8);
                break;
            case 9:
                imageList.get(15).setImageResource(R.drawable.mem9);
                break;
            case 10:
                imageList.get(15).setImageResource(R.drawable.mem10);
                break;
        }

        drawableList = new ArrayList<>();
        for (ImageView iv : imageList) {
            drawableList.add(iv.getDrawable());
        }
    }

    protected void initLists() {
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
        for (ImageView iv : imageList) {
            tagList.add(iv.getTag());
            iv.setOnClickListener(this);
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
                throw new IllegalStateException("Unexpected value in isPossibleToMove: "
                        + tapPosition);
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

    @Override
    public void onClick(View view) {
        int tapPosition = 0;

        for (int i = 0; i < 16; i++) {
            if (view.getId() == imageList.get(i).getId()) {
                tapPosition = i + 1;
                break;
            }
        }

        if (!isPossibleToMove(tapPosition)) {
            return;
        }
        swapSquares(tapPosition);

        if (tapPosition == 16 && isWin()) {
            if (mSettings.getInt("openLevels", 0) == currentLevel && currentLevel < LEVELS) {
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putInt("openLevels", currentLevel + 1);
                editor.apply();
            } else if (currentLevel == LEVELS) {
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putBoolean("isGameOver", true);
                editor.apply();
            }

            playSound(sound);

            FragmentManager manager = getSupportFragmentManager();
            DialogStoryWin dialogStoryWin = new DialogStoryWin();
            dialogStoryWin.show(manager, "dialogStoryWin");
            dialogStoryWin.setCancelable(false);
        }
    }

    public void finishActivity() {
        finish();
    }

    public void restartActivity() {
        finish();
        if (currentLevel < LEVELS) {
            Intent intent = getIntent();
            intent.putExtra("currentLevel", ++currentLevel);
            startActivity(intent);
        }
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
