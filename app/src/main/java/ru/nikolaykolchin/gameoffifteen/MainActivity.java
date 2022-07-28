package ru.nikolaykolchin.gameoffifteen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences mSettings;
    private ImageButton soundButton;
    private ImageButton vibrationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSettings = getSharedPreferences("my_settings", Context.MODE_PRIVATE);
        if (!mSettings.contains("isSoundOn") || !mSettings.contains("isVibrationOn")) {
            SharedPreferences.Editor editor = mSettings.edit();
            if (!mSettings.contains("isSoundOn")) editor.putBoolean("isSoundOn", true);
            if (!mSettings.contains("isVibrationOn")) editor.putBoolean("isVibrationOn", true);
            editor.apply();
        }
        soundButton = findViewById(R.id.soundButton);
        vibrationButton = findViewById(R.id.vibrationButton);
        if (mSettings.getBoolean("isSoundOn", false)) {
            soundButton.setImageResource(R.drawable.ic_baseline_volume_up_40);
        } else {
            soundButton.setImageResource(R.drawable.ic_baseline_volume_off_40);
        }
        if (mSettings.getBoolean("isVibrationOn", false)) {
            vibrationButton.setImageResource(R.drawable.ic_baseline_vibration_40);
        } else {
            vibrationButton.setImageResource(R.drawable.ic_baseline_smartphone_40);
        }

        soundButton.setOnClickListener(this);
        vibrationButton.setOnClickListener(this);
        Button buttonStory = findViewById(R.id.buttonStory);
        buttonStory.setOnClickListener(this);
        Button buttonUser = findViewById(R.id.buttonUser);
        buttonUser.setOnClickListener(this);
        Button buttonClassic = findViewById(R.id.buttonClassic);
        buttonClassic.setOnClickListener(this);
        Button buttonAbout = findViewById(R.id.buttonAbout);
        buttonAbout.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Button button = findViewById(R.id.buttonAbout);
        if (!mSettings.contains("isGameOver") || !mSettings.getBoolean("isGameOver", false)) {
            button.setVisibility(View.INVISIBLE);
        } else {
            button.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.soundButton) {
            SharedPreferences.Editor editor = mSettings.edit();
            if (mSettings.getBoolean("isSoundOn", false)) {
                editor.putBoolean("isSoundOn", false);
                soundButton.setImageResource(R.drawable.ic_baseline_volume_off_40);
            } else {
                editor.putBoolean("isSoundOn", true);
                soundButton.setImageResource(R.drawable.ic_baseline_volume_up_40);
            }
            editor.apply();
            return;
        }
        if (view.getId() == R.id.vibrationButton) {
            SharedPreferences.Editor editor = mSettings.edit();
            if (mSettings.getBoolean("isVibrationOn", false)) {
                editor.putBoolean("isVibrationOn", false);
                vibrationButton.setImageResource(R.drawable.ic_baseline_smartphone_40);
            } else {
                editor.putBoolean("isVibrationOn", true);
                vibrationButton.setImageResource(R.drawable.ic_baseline_vibration_40);
            }
            editor.apply();
            return;
        }

        if (view.getId() == R.id.buttonAbout) {
            Dialog aboutDialog = new Dialog(this);
            aboutDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            aboutDialog.setContentView(getLayoutInflater().inflate(R.layout.about, null));
            aboutDialog.show();
            return;
        }
        Intent intent = null;
        if (view.getId() == R.id.buttonStory) intent = new Intent(this, StoryLevelActivity.class);
        if (view.getId() == R.id.buttonClassic) intent = new Intent(this, ClassicActivity.class);
        if (view.getId() == R.id.buttonUser) intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }
}