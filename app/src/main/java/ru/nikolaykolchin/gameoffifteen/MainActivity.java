package ru.nikolaykolchin.gameoffifteen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String APP_PREFERENCES = "my_settings";
    SharedPreferences mSettings;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (!mSettings.contains("isSoundOn")) {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean("isSoundOn", true);
            editor.apply();
        }
        imageButton = findViewById(R.id.soundButton);
        if (mSettings.getBoolean("isSoundOn", false)) {
            imageButton.setImageResource(R.drawable.ic_baseline_volume_up_40);
        } else {
            imageButton.setImageResource(R.drawable.ic_baseline_volume_off_40);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Button button = findViewById(R.id.button4);
        if (!mSettings.contains("isGameOver") || !mSettings.getBoolean("isGameOver", false)) {
            button.setVisibility(View.INVISIBLE);
        } else {
            button.setVisibility(View.VISIBLE);
        }
    }

    public void buttonStory(View view) {
        Intent intent = new Intent(this, StoryLevelActivity.class);
        startActivity(intent);
    }

    public void buttonClassic(View view) {
        Intent intent = new Intent(this, ClassicActivity.class);
        startActivity(intent);
    }

    public void buttonUser(View view) {
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }

    public void buttonAbout(View view) {
        Toast.makeText(getApplicationContext(), "Я красивый, правда )", Toast.LENGTH_SHORT).show();
    }

    public void buttonSound(View view) {
        SharedPreferences.Editor editor = mSettings.edit();
        if (mSettings.getBoolean("isSoundOn", false)) {
            editor.putBoolean("isSoundOn", false);
            imageButton.setImageResource(R.drawable.ic_baseline_volume_off_40);
        } else {
            editor.putBoolean("isSoundOn", true);
            imageButton.setImageResource(R.drawable.ic_baseline_volume_up_40);
        }
        editor.apply();
    }

    //TODO раздел "об авторах"
    //TODO в юзер моде разрешение на доступ в галерею
}