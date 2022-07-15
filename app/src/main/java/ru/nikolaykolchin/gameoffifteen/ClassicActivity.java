package ru.nikolaykolchin.gameoffifteen;

import static ru.nikolaykolchin.gameoffifteen.CommonMethods.isPossibleToMove;
import static ru.nikolaykolchin.gameoffifteen.CommonMethods.isSolvable;
import static ru.nikolaykolchin.gameoffifteen.CommonMethods.isWin;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassicActivity extends AppCompatActivity implements View.OnClickListener {
    private int emptyPos;
    private ImageView emptySquare;
    private ArrayList<ImageView> imageList;
    private ArrayList<Object> tagList;
    private ArrayList<Drawable> drawableList;
    private static final String APP_PREFERENCES = "my_settings";
    private static final String APP_PREFERENCES_FIELD = "Field";
    private static final int FIELD_SIZE = 16;
    private SharedPreferences mSettings;
    private SoundPool mSoundPool;
    private int sound;
    private AssetManager mAssetManager;
    private int mStreamID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic);

        initLists();

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (!mSettings.getBoolean("hasVisited", false)) {
            FragmentManager manager = getSupportFragmentManager();
            DialogClassicInfo dialogClassicInfo = new DialogClassicInfo();
            dialogClassicInfo.show(manager, "dialogClassicInfo");
            dialogClassicInfo.setCancelable(false);

            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean("hasVisited", true);
            editor.apply();
        }
        if (mSettings.contains(APP_PREFERENCES_FIELD)) {
            loadLastState();
        }
        if (isWin(tagList)) {
            startNewGame();
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
    }

    @Override
    protected void onPause() {
        mSoundPool.release();
        mSoundPool = null;

        StringBuilder savedField = new StringBuilder();
        for (Object obj : tagList) {
            savedField.append(obj.toString());
        }
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_FIELD, savedField.toString());
        editor.apply();

        super.onPause();
    }

    private void initLists() {
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
        }
        ImageButton buttonReset = findViewById(R.id.imageButton);
        buttonReset.setOnClickListener(this);
    }

    private void loadLastState() {
        String lastState = mSettings.getString(APP_PREFERENCES_FIELD, "");
        tagList.clear();
        for (int i = 0; i < FIELD_SIZE; i++) {
            tagList.add(lastState.substring(i * 2, i * 2 + 2));
        }
        for (int i = 0; i < FIELD_SIZE; i++) {
            imageList.get(i).setTag(tagList.get(i));
        }
        for (int i = 0; i < FIELD_SIZE; i++) {
            int tag = Integer.parseInt(imageList.get(i).getTag().toString());
            imageList.get(i).setImageDrawable(drawableList.get(tag - 1));
            if (tag == 16) {
                emptySquare = imageList.get(i);
                emptyPos = i + 1;
            }
        }
    }

    private void swapSquares(int tapPosition) {
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

    private void shuffleSquares(List<Integer> randomField) {
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

    protected void finishActivity() {
        finish();
    }

    protected void startNewGame() {
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
        if (view.getId() == R.id.imageButton) {
            FragmentManager manager = getSupportFragmentManager();
            DialogClassicReset dialogClassicReset = new DialogClassicReset();
            dialogClassicReset.show(manager, "dialogClassicReset");
            dialogClassicReset.setCancelable(false);
            return;
        }

        int tapPosition = 0;
        for (int i = 0; i < FIELD_SIZE; i++) {
            if (view.getId() == imageList.get(i).getId()) {
                tapPosition = i + 1;
                break;
            }
        }

        if (!isPossibleToMove(tapPosition, emptyPos)) {
            return;
        }
        swapSquares(tapPosition);

        if (tapPosition == 16 && isWin(tagList)) {
            if (mSettings.getBoolean("isSoundOn", false)) playSound(sound);

            FragmentManager manager = getSupportFragmentManager();
            DialogClassicWin dialogClassicWin = new DialogClassicWin();
            dialogClassicWin.show(manager, "dialogClassicWin");
            dialogClassicWin.setCancelable(false);
        }
    }
}