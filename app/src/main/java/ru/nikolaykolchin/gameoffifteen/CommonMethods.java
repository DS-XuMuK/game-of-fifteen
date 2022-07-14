package ru.nikolaykolchin.gameoffifteen;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class CommonMethods {
    private static final String CHECK_STRING = "01020304050607080910111213141516";
    private static final int FIELD_SIZE = 16;

    static boolean isWin(ArrayList<Object> tagList) {
        StringBuilder currentField = new StringBuilder();
        for (Object obj : tagList) {
            currentField.append(obj.toString());
        }
        return currentField.toString().equals(CHECK_STRING);
    }

    static boolean isPossibleToMove(int tapPosition, int emptyPos) {
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

    static boolean isSolvable(List<Integer> randomField) {
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

    static void loadBitmapToImageList(ArrayList<ImageView> imageList, Bitmap bitmap) {
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
    }
}