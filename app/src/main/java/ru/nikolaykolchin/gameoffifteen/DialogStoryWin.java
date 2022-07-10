package ru.nikolaykolchin.gameoffifteen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogStoryWin extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Успех");
        builder.setMessage("Поздравляем, уровень пройден!");
        if (StoryActivity.currentLevel < 15 || (StoryActivity.currentLevel == 15
                && StoryLevelActivity.mSettings.getBoolean("isGameOver", false))) {
            builder.setPositiveButton("Следующий", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ((StoryActivity) requireActivity()).restartActivity();
                }
            });
        }
        builder.setNegativeButton("Назад", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((StoryActivity) requireActivity()).finishActivity();
                    }
                });
        return builder.create();
    }
}