package com.nix.dimablyznyuk.student.contacts.model;

/**
 * Created by Dima Blyznyuk on 20.07.15.
 */
import android.graphics.Rect;

import com.nix.dimablyznyuk.student.contacts.model.Cell;

public class DayCell extends Cell {

    private int dayOfMonth = 1;
    private boolean isCurrentMonth;

    public DayCell(boolean isCurrentMonth, int dayOfMon, Rect rect, float textSize, boolean bold) {
        super(String.valueOf(dayOfMon),rect,textSize,bold);
        this.dayOfMonth = dayOfMon;
       this.isCurrentMonth = isCurrentMonth;
    }


    public DayCell(boolean isCurrentMonth,int dayOfMon, Rect rect, float textSize, int color) {
        this(isCurrentMonth,dayOfMon, rect, textSize, false);
        mPaint.setColor(color);
    }


    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public boolean isCurrentMonth(){
        return this.isCurrentMonth;
    }

    public boolean isInArea(int x, int y) {
        return mBound.contains(x, y);
    }

}