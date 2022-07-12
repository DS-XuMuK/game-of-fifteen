package ru.nikolaykolchin.gameoffifteen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String APP_PREFERENCES = "my_settings";
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
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
    //TODO кнопка выключения звука
    //TODO звуки в стори моде
    //TODO раздел "об авторах"
    //TODO в юзер моде разрешение на доступ в галерею
    //TODO вынос повторяющихся методов в отдельный класс
}