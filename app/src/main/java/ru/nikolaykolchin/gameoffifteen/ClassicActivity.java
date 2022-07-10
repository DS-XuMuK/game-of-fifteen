package ru.nikolaykolchin.gameoffifteen;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassicActivity extends AppCompatActivity implements View.OnClickListener {
    int emptyPos;
    ImageView emptySquare;
    ArrayList<ImageView> imageList;
    ArrayList<Object> tagList;
    ArrayList<Drawable> drawableList;
    static final String APP_PREFERENCES = "my_settings";
    static final String APP_PREFERENCES_FIELD = "Field";
    static final String CHECK_STRING = "01020304050607080910111213141516";
    static final int FIELD_SIZE = 16;
    SharedPreferences mSettings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic);

        initLists();

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean hasVisited = mSettings.getBoolean("hasVisited", false);

        if (!hasVisited) {
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
        if (isWin()) {
            startNewGame();
        }
    }

    @Override
    protected void onPause() {
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
            iv.setOnClickListener(this);
        }
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

    private boolean isPossibleToMove(int tapPosition) {
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
        tagList.clear();
        for (int i = 0; i < FIELD_SIZE; i++) {
            tagList.add(CHECK_STRING.substring(i * 2, i * 2 + 2));
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

    private boolean isWin() {
        StringBuilder currentField = new StringBuilder();
        for (Object obj : tagList) {
            currentField.append(obj.toString());
        }
        return currentField.toString().equals(CHECK_STRING);
    }

    private boolean isSolvable(List<Integer> randomField) {
        int n = 0;
        int e;
        for (int i = 0; i < FIELD_SIZE; i++) {
            if (randomField.get(i) == 16) {
                continue;
            }
            for (int j = i + 1; j < FIELD_SIZE; j++) {
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

    public void clickReset(View view) {
        FragmentManager manager = getSupportFragmentManager();
        DialogClassicReset dialogClassicReset = new DialogClassicReset();
        dialogClassicReset.show(manager, "dialogClassicReset");
        dialogClassicReset.setCancelable(false);
    }

    @Override
    public void onClick(View view) {
        int tapPosition = 0;

        for (int i = 0; i < FIELD_SIZE; i++) {
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
            FragmentManager manager = getSupportFragmentManager();
            DialogClassicWin dialogClassicWin = new DialogClassicWin();
            dialogClassicWin.show(manager, "dialogClassicWin");
            dialogClassicWin.setCancelable(false);
        }
    }
}