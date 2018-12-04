package com.jq.code.view.wheel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hfei on 2016/5/13.
 */
public class DatePicker extends LinearLayout implements NumberPicker.OnNumberItemSelectedListener {
    private int year = 2000;
    private int month = 6;
    private int day = 15;
    private NumberPicker yearView, monthView, dayView;
    private String yearUnitText = "年";
    private String monthUnitText = "月";
    private String dayUnitText = "日";

    public DatePicker(Context context) {
        super(context);
        initialize(context);
    }

    public DatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public DatePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams params =
                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        int[] yearRange = getYearRange();
        year = yearRange[1] - 24;
        yearView = new NumberPicker(context, yearRange[1], yearRange[0], year);
        yearView.setLayoutParams(params);
        yearView.setText(yearUnitText);
        yearView.setOnNumberItemSelectedListener(this);

        monthView = new NumberPicker(context, 12, 1, month);
        monthView.setLayoutParams(params);
        monthView.setText(monthUnitText);
        monthView.setOnNumberItemSelectedListener(this);

        dayView = new NumberPicker(context, getDays(), 1, day);
        dayView.setLayoutParams(params);
        dayView.setText(dayUnitText);
        dayView.setOnNumberItemSelectedListener(this);

        setGravity(Gravity.CENTER);
        addView(yearView);
        addView(monthView);
        addView(dayView);
    }

    public void setUnitText(String yearUnitText, String monthUnitText, String dayUnitText) {
        this.yearUnitText = yearUnitText;
        this.monthUnitText = monthUnitText;
        this.dayUnitText = dayUnitText;
        if(null != yearView) {
            yearView.setText(yearUnitText);
        }

        if(null != monthView) {
            monthView.setText(monthUnitText);
        }

        if(null != dayView) {
            dayView.setText(dayUnitText);
        }
    }

    /**
     * 设置字体大小
     * @param textSize 字体大小，以sp为单位
     */
    public void setTextSize(int textSize) {
        if(null != yearView) {
            yearView.setTextSize(textSize);
        }

        if(null != monthView) {
            monthView.setTextSize(textSize);
        }

        if(null != dayView) {
            dayView.setTextSize(textSize);
        }
    }

    /**
     * 获取日期
     *
     * @return
     */
    public String getDate() {
        return year
                + "-"
                + ((month < 10) ? ("0" + month) : month)
                + "-"
                + day;
    }

    private void monthChangedHandle(){
        Calendar now = Calendar.getInstance();
        int curYear=now.get(Calendar.YEAR);
        int curMonth=now.get(Calendar.MONTH) + 1;
        int curDay=now.get(Calendar.DAY_OF_MONTH);

        if(year==curYear && month==curMonth){
            dayView.setRange(1,curDay);
            if(day>curDay){
                day=curDay;
                dayView.setCenter(curDay);
            }
        }else{
            dayView.setRange(1, getDays());
        }
    }


    @Override
    public void onSelected(ViewGroup parent, int number) {
        Calendar now = Calendar.getInstance();
        int curYear=now.get(Calendar.YEAR);
        int curMonth=now.get(Calendar.MONTH) + 1;
        int curDay=now.get(Calendar.DAY_OF_MONTH);
        if (parent == yearView) {
            year = number;
            if(curYear==year){
                monthView.setRange(1,curMonth);
                if(month>=curMonth){
                    month=curMonth;
                    monthView.setCenter(curMonth);
                    monthChangedHandle();
                }else{
                    dayView.setRange(1, getDays());
                }
            }else{
                monthView.setRange(1,12);
                dayView.setRange(1, getDays());
            }
        } else if (parent == monthView) {
            month = number;
            monthChangedHandle();
        } else if (parent == dayView) {
            day = number;
        }
    }

    @Override
    public void onNothing(ViewGroup parent) {

    }

    private int getDays() {
        return getDaysWithDate(year + "-"
                + (month < 10 ? "0" + month : month)
                + "-01");
    }

    public static final String TIME_FORMAT2 = "yyyy-MM-dd";

    public static int getDaysWithDate(String date) {
        Calendar a = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT2,
                Locale.getDefault());
        try {
            a.setTime(format.parse(date));
            a.set(Calendar.YEAR, a.get(Calendar.YEAR));
            a.set(Calendar.MONTH, a.get(Calendar.MONTH));
            a.set(Calendar.DATE, 1);
            a.roll(Calendar.DATE, -1);
            int maxDate = a.get(Calendar.DATE);
            return maxDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 30;
    }


    public static int[] getYearRange() {
        int[] range = new int[2];
        SimpleDateFormat format = new SimpleDateFormat("yyyy",
                Locale.getDefault());
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        range[1] = Integer.parseInt(format.format(date));
        range[0] = range[1] - 100;
        return range;
    }
}
