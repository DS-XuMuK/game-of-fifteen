package ru.nikolaykolchin.gameoffifteen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogUserWin extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Победа")
                .setMessage("Вы собрали изображение! Хотите сыграть ещё раз?")
                .setPositiveButton("Повторить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((UserActivity) requireActivity()).startNewGame();
                    }
                })
                .setNeutralButton("Выбрать другое изображение", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        //TODO: вызов метода выбора другого изображения
                    }
                })
                .setNegativeButton("Выйти", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((UserActivity) requireActivity()).finishActivity();
                    }
                });
        return builder.create();
    }
}