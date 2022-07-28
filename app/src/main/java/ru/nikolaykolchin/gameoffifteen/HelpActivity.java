package ru.nikolaykolchin.gameoffifteen;

import static ru.nikolaykolchin.gameoffifteen.StoryActivity.currentLevel;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ImageView imageView = findViewById(R.id.resultImage);

        String storyName = "story" + currentLevel;
        int drawableID = getResources().getIdentifier(storyName, "drawable", getPackageName());
        imageView.setImageDrawable(getDrawable(drawableID));
    }
}