package com.nix.dimablyznyuk.student.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.nix.dimablyznyuk.student.contacts.CustomCalendarView.OnCellTouchListener;
import com.nix.dimablyznyuk.student.contacts.model.DayCell;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by Dima Blyznyuk on 20.07.15.
 */

public class CalendarActivity extends AppCompatActivity {

    public static final String TAG = "CalendarActivity";
    public static final String EXTRA_DATE = "date";

    private String day;
    private CustomCalendarView customCalendarView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);

        this.setTitle(R.string.select_date);

        customCalendarView = (CustomCalendarView) findViewById(R.id.calendar);

        customCalendarView.setOnCellTouchListener(new OnCellTouchListener() {

            @Override
            public void onTouch(DayCell cell) {

                day = String.valueOf(cell.getDayOfMonth());

                Log.d(TAG, "ClickedDate = " + day);
                Log.d(TAG, "ClickedYear = " + customCalendarView.getYear());
                Log.d(TAG, "ClickedMonth = " + customCalendarView.getMonth());

                String date = new StringBuilder()
                        .append(day)
                        .append(" / ")
                        .append(customCalendarView.getMonth() + 1)
                        .append(" / ")
                        .append(customCalendarView.getYear()).toString();

                long dateMillis = 0;
                try {
                    dateMillis = MyDateUtils.toMilliseconds(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(dateMillis);
                cal.get(Calendar.DAY_OF_MONTH);

                Intent intent = new Intent();
                intent.putExtra(EXTRA_DATE, dateMillis);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
