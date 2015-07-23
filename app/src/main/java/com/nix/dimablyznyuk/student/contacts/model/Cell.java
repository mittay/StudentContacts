package com.nix.dimablyznyuk.student.contacts.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Dima Blyznyuk on 20.07.15.
 */
public class Cell {

    protected Rect mBound = null;
    protected String buttonStr;
    protected Paint mPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG
            | Paint.ANTI_ALIAS_FLAG);
    int dx, dy;
    public Cell(){}

    public Cell(String btnString, Rect rect, float textSize, boolean bold) {
        buttonStr = btnString;
        mBound = rect;
        mPaint.setTextSize(textSize);
        mPaint.setColor(Color.BLACK);
        if (bold) mPaint.setFakeBoldText(true);

        dx = (int) mPaint.measureText(btnString) / 2;
        dy = (int) (-mPaint.ascent() + mPaint.descent()) / 2;
    }

    public void draw(Canvas canvas) {
        canvas.drawText(buttonStr, mBound.centerX() - dx, mBound.centerY() + dy, mPaint);
    }

    public boolean isInArea(int x, int y) {
        return mBound.contains(x, y);
    }
}