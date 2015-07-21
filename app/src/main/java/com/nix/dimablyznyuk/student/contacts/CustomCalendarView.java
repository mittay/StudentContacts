package com.nix.dimablyznyuk.student.contacts;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.MonthDisplayHelper;
import android.view.MotionEvent;
import android.view.View;

import com.nix.dimablyznyuk.student.contacts.model.Cell;
import com.nix.dimablyznyuk.student.contacts.model.DayCell;

import java.util.Calendar;


public class CustomCalendarView extends View {

    private static final String TAG = "CalendarView";

    private static int CELL_WIDTH;
    private static int CELL_HEIGHT;
    private static int CELL_MARGIN_TOP;
    private static int CELL_MARGIN_LEFT;
    private static float CELL_TEXT_SIZE;
    private static int CAL_HEIGHT;
    private static int CAL_WIDTH;
    private String[] monthNamesWithLocale = getResources()
            .getStringArray(R.array.month_name_with_locale);
    private String[] monthNames =  getResources().getStringArray(R.array.month_name);
    private String[] weekDayString = getResources().getStringArray(R.array.week_day_array);
    private Cell[] weekDayCells = new Cell[7];
    private String[] buttonStr = getResources().getStringArray(R.array.button_label);
    private Cell[] buttonCells = new Cell[2];
    private Calendar rightNow = null;
    private Cell[] buttonMonthCells;
    private Cell[] buttonYearCells;
    private Cell currentMonth;
    private Cell currentYear;
    private DayCell[][] dayMonthCells = new DayCell[6][7];
    private OnCellTouchListener onCellTouchListener = null;
    private MonthDisplayHelper monthDisplayHelper;

    public interface OnCellTouchListener {
        public void onTouch(DayCell cell);
    }

    public CustomCalendarView(Context context) {
        this(context, null);
    }

    public CustomCalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCalendarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initCalendarView();
    }

    private void initCalendarView() {

        dayMonthCells = new DayCell[6][7];
        weekDayCells = new Cell[7];
        buttonMonthCells = new Cell[2];
        buttonYearCells = new Cell[2];

        rightNow = Calendar.getInstance();

        Resources res = getResources();

        CELL_WIDTH = (int) res.getDimension(R.dimen.cell_width);
        CELL_HEIGHT = (int) res.getDimension(R.dimen.cell_height);
        CELL_MARGIN_TOP = (int) res.getDimension(R.dimen.cell_margin_top);
        CELL_MARGIN_LEFT = (int) res.getDimension(R.dimen.cell_margin_left);
        CELL_TEXT_SIZE = res.getDimension(R.dimen.cell_text_size);
        CAL_HEIGHT = (int)res.getDimension(R.dimen.calendar_height);
        CAL_WIDTH = (int)res.getDimension(R.dimen.calendar_width);

        monthDisplayHelper = new MonthDisplayHelper(rightNow.get(Calendar.YEAR),
                rightNow.get(Calendar.MONTH));
    }

    private class MonthDay {
        public int day;
        public boolean thisMonth;
        public MonthDay(int d, boolean b) {
            day = d;
            thisMonth = b;
        }
        public MonthDay(int d) {
            this(d, false);
        }
    };

    private void initCells() {

        MonthDay tmp[][] = new MonthDay[6][7];

        for(int i = 0; i < tmp.length; i ++) {
            int n[] = monthDisplayHelper.getDigitsForRow(i);
            for(int d = 0; d < n.length; d ++) {
                if(monthDisplayHelper.isWithinCurrentMonth(i,d))
                    tmp[i][d] = new MonthDay(n[d], true);
                else
                    tmp[i][d] = new MonthDay(n[d]);
            }
        }
        // build cells
        Rect Bound = new Rect(CELL_MARGIN_LEFT, CELL_MARGIN_TOP, CELL_WIDTH + CELL_MARGIN_LEFT,
                CELL_HEIGHT + CELL_MARGIN_TOP);

        buttonMonthCells[0] = new Cell(buttonStr[0], new Rect(Bound), CELL_TEXT_SIZE,true);
        Bound.offset( 3 * CELL_WIDTH, 0);
        currentMonth = new Cell(getStringMonthWithLocale(), new Rect(Bound)
                , CELL_TEXT_SIZE,true);
        Bound.offset( 3 * CELL_WIDTH, 0);
        buttonMonthCells[1] = new Cell(buttonStr[1], new Rect(Bound), CELL_TEXT_SIZE,true);

        Bound.offset(0, CELL_HEIGHT); // move to next row and first column
        Bound.left = CELL_MARGIN_LEFT;
        Bound.right = CELL_MARGIN_LEFT + CELL_WIDTH;
        buttonYearCells[0] = new Cell(buttonStr[0], new Rect(Bound), CELL_TEXT_SIZE,true);
        Bound.offset( 3 * CELL_WIDTH, 0);
        currentYear = new Cell(String.valueOf(getYear()), new Rect(Bound)
                , CELL_TEXT_SIZE,true);
        Bound.offset( 3 * CELL_WIDTH, 0);
        buttonYearCells[1] = new Cell(buttonStr[1], new Rect(Bound), CELL_TEXT_SIZE,true);

        Bound.offset(0, CELL_HEIGHT); // move to next row and first column
        Bound.left = CELL_MARGIN_LEFT;
        Bound.right = CELL_MARGIN_LEFT + CELL_WIDTH;

        for(int dayWeek=0; dayWeek < weekDayString.length; dayWeek++) {
            weekDayCells[dayWeek] = new Cell(weekDayString[dayWeek], new Rect(Bound),
                    CELL_TEXT_SIZE,true);
            Bound.offset(CELL_WIDTH, 0);
        }

        Bound.offset(0, CELL_HEIGHT); // move to next row and first column
        Bound.left = CELL_MARGIN_LEFT;
        Bound.right = CELL_MARGIN_LEFT + CELL_WIDTH;


        for(int week = 0; week < dayMonthCells.length; week ++) {
            for(int day = 0; day < dayMonthCells[week].length; day ++)
            {

                if(tmp[week][day].thisMonth) {
                    if(day == 0 || day == 6 )
                        dayMonthCells[week][day] = new DayCell(true,tmp[week][day].day,
                                new Rect(Bound), CELL_TEXT_SIZE,Color.RED);
                    else
                        dayMonthCells[week][day] = new DayCell(true,tmp[week][day].day,
                                new Rect(Bound), CELL_TEXT_SIZE, Color.GRAY);
                } else {

                    dayMonthCells[week][day] = new DayCell(false, tmp[week][day].day,
                            new Rect(Bound), CELL_TEXT_SIZE,Color.LTGRAY);
                }

                Bound.offset(CELL_WIDTH, 0); // move to next column
            }
            Bound.offset(0, CELL_HEIGHT); // move to next row and first column
            Bound.left = CELL_MARGIN_LEFT;
            Bound.right = CELL_MARGIN_LEFT + CELL_WIDTH;

        }
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        initCells();
        super.onLayout(changed, left, top, right, bottom);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int measuredHeight = measureHeight(heightMeasureSpec);
    int measuredWidth = measureWidth(widthMeasureSpec);
    setMeasuredDimension(measuredHeight, measuredWidth);
}

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = CAL_HEIGHT;

        if (specMode == MeasureSpec.AT_MOST) {
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }
    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = CAL_WIDTH;
        if (specMode == MeasureSpec.AT_MOST) {
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }

    public int getYear() {
        return monthDisplayHelper.getYear();
    }

    public String getStringMonth() {
        return monthNames [monthDisplayHelper.getMonth()];
    }
    public int getMonth() {
        return monthDisplayHelper.getMonth();
    }
    public String getStringMonthWithLocale() {
        return monthNamesWithLocale[monthDisplayHelper.getMonth()];
    }
    public void nextMonth() {
        monthDisplayHelper.nextMonth();
        initCells();
        invalidate();
    }

    public void previousMonth() {
        monthDisplayHelper.previousMonth();
        initCells();
        invalidate();
    }
        public void nextYear() {
        monthDisplayHelper = new MonthDisplayHelper(getYear()+1, rightNow.get(Calendar.MONTH));
        initCells();
        invalidate();
    }
    public void previousYear() {
        monthDisplayHelper = new MonthDisplayHelper(getYear()-1, rightNow.get(Calendar.MONTH));
        initCells();
        invalidate();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(buttonMonthCells[0].isInArea((int) event.getX(), (int) event.getY())) {
            previousMonth();
        }

        if(buttonMonthCells[1].isInArea((int) event.getX(), (int) event.getY())) {
            nextMonth(); ;
        }

        if(buttonYearCells[0].isInArea((int) event.getX(), (int) event.getY())) {
            previousYear();
        }

        if(buttonYearCells[1].isInArea((int) event.getX(), (int) event.getY())) {
            nextYear() ;
        }
        if(onCellTouchListener != null) {
            for(DayCell[] days : dayMonthCells) {
                for(DayCell day : days) {
                    if(day.isInArea((int) event.getX(), (int) event.getY())
                            && day.isCurrentMonth()) {
                        onCellTouchListener.onTouch(day);
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public void setOnCellTouchListener(OnCellTouchListener p) {
        onCellTouchListener = p;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
          /* previous month cell*/
        buttonMonthCells[0].draw(canvas);

        currentMonth.draw(canvas);
        /* next month cell*/
        buttonMonthCells[1].draw(canvas);

         /* previous year cell*/
        buttonYearCells[0].draw(canvas);

        currentYear.draw(canvas);
        /* next year cell*/
        buttonYearCells[1].draw(canvas);
        for(Cell weekDays: weekDayCells) {
            weekDays.draw(canvas);
        }
         /* days of month cells*/
        for(DayCell[] week : dayMonthCells) {
            for(DayCell day : week) {
                day.draw(canvas);
            }
        }
    }
}