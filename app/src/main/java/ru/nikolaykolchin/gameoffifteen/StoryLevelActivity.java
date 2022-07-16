package ru.nikolaykolchin.gameoffifteen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class StoryLevelActivity extends AppCompatActivity implements View.OnClickListener {
    static SharedPreferences mSettings;
    private ArrayList<ImageView> imageList;
    private ArrayList<Drawable> drawableList;
    private static final int LEVELS = 16;
    private int openLevels;
    private int bonusCounter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_level);
        mSettings = getSharedPreferences("my_settings", Context.MODE_PRIVATE);

        if (!mSettings.getBoolean("hasVisitedStory", false)) {
            FragmentManager manager = getSupportFragmentManager();
            DialogStoryInfo dialogStoryInfo = new DialogStoryInfo();
            dialogStoryInfo.show(manager, "dialogStoryInfo");
            dialogStoryInfo.setCancelable(false);

            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean("hasVisitedStory", true);
            editor.apply();
        }

        if (!mSettings.contains("openLevels")) {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putInt("openLevels", 1);
            editor.apply();
        }
        openLevels = mSettings.getInt("openLevels", 0);
        bonusCounter = mSettings.getInt("bonusCounter", 0);

        initList();
    }

    @Override
    protected void onStart() {
        super.onStart();

        openLevels = mSettings.getInt("openLevels", 0);
        for (int i = 0; i < LEVELS; i++) {
            if (i < openLevels) {
                imageList.get(i).setImageDrawable(drawableList.get(i));
                imageList.get(i).setClickable(true);
            } else {
                imageList.get(i).setImageResource(R.drawable.lvl_close);
                imageList.get(i).setClickable(false);
            }
        }
        if (openLevels == LEVELS) {
            if (bonusCounter != 0 && bonusCounter != 16) {
                imageList.get(15).setImageDrawable(drawableList.get(bonusCounter - 1));
            } else if (bonusCounter == 16) {
                imageList.get(15).setImageDrawable(getDrawable(R.drawable.lvl_bonus));
            }
        }

        if (mSettings.getBoolean("isGameOver", false) && !mSettings.getBoolean("isMessageShown",
                false)) {
            Toast.makeText(getApplicationContext(), "Сюжет пройден! Открыт раздел \"Об авторе\"",
                    Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean("isMessageShown", true);
            editor.apply();
        }
    }

    @Override
    protected void onStop() {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt("bonusCounter", bonusCounter);
        editor.apply();

        super.onStop();
    }

    private void initList() {
        imageList = new ArrayList<>();
        for (int i = 0; i < LEVELS; i++) {
            String posName = "lvl" + (i + 1);
            int resID = getResources().getIdentifier(posName, "id", getPackageName());
            imageList.add(findViewById(resID));
        }

        drawableList = new ArrayList<>();
        for (ImageView iv : imageList) {
            drawableList.add(iv.getDrawable());
            iv.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        int tapLevel = 0;

        for (int i = 0; i < LEVELS; i++) {
            if (view.getId() == imageList.get(i).getId()) {
                tapLevel = i + 1;
                break;
            }
        }

        if (tapLevel == 16 && bonusCounter < 16) {
            bonusCounter++;
            if (bonusCounter != 16) {
                imageList.get(15).setImageDrawable(drawableList.get(bonusCounter - 1));
            } else {
                imageList.get(15).setImageDrawable(getDrawable(R.drawable.lvl_bonus));
            }
            return;
        }

        Intent intent = new Intent(this, StoryActivity.class);
        intent.putExtra("currentLevel", tapLevel);
        startActivity(intent);
    }
}