package com.jq.code.code.util;

import android.content.Context;

import com.jq.code.R;
import com.jq.code.model.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimeUtil {

    public static final String TIME_FORMAT0 = "yyyy/MM/dd  HH:mm:ss";
    public static final String TIME_FORMAT1 = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT2 = "yyyy-MM-dd";
    public static final String TIME_FORMAT3 = "yyyy/MM/dd";
    public static final String TIME_FORMAT4 = "yyyy年MM月";
    public static final String TIME_FORMAT5 = "yyyy-MM-dd EEEE";
    public static final String TIME_FORMAT6 = "MM/dd, EEEE";
    public static final String TIME_FORMAT7 = "yyyyMMdd";
    public static final String TIME_FORMAT8 = "yyyy/MM/dd EEEE";
    public static final String TIME_FORMAT_EN_1 = "MM/dd/yyyy";
    public static final String TIME_FORMAT_EN_2 = "MM/yyyy";
    public static final String TIME_FORMAT_EN_3 = "MM/dd";
    public static final String TIME_FORMAT_EN_4 = "yyyy年MM月dd日";
    public static final String TIME_FORMAT_EN_5 = "MM月dd日";
    public static final String TIME_FORMAT_EN_6 = "MM月dd日 HH:mm:ss";
    /**
     * 周时间格式语言切换转换
     *
     * @param context
     * @param date
     * @return
     */
    public static String formatReportDate(Context context, String date) {
        //		String time = ""
//				+ TimeUtil.dateFormatChange(date,
//				TIME_FORMAT1, "yyyy"
//						+ context.getString(R.string.year) + "MM"
//						+ context.getString(R.string.monthReport) + "dd"
//						+ context.getString(R.string.date) + "   HH:mm");
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT1);
        Date dtInput = null;
        try {
            dtInput = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String time = "" + TimeUtil.dateFormatChange(date, TIME_FORMAT1, "yyyy/MM/dd HH:mm");
        return time;
    }

    /**
     * 获得两个时间之间的天数间隔
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long getGapDays(String date1, String date2) {
        return getGapDays(TIME_FORMAT2, date1, date2);
    }

    /**
     * 获得两个时间之间的天数间隔
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long getGapDays(String format, String date1, String date2) {
        if (date1 == null || date1.equals(""))
            return 0;
        if (date2 == null || date2.equals(""))
            return 0;
        // 转换为标准时间
        SimpleDateFormat myFormatter = new SimpleDateFormat(format, Locale.getDefault());
        Date date = null;
        Date mydate = null;
        try {
            date = myFormatter.parse(date1);
            mydate = myFormatter.parse(date2);
        } catch (Exception e) {
        }
        long longdate = date.getTime();
        long longmydate = mydate.getTime();
        //updated by lixun on 2016/3/4
        //long day = longdate - longmydate;
        long day = Math.abs(longdate - longmydate);
        day = (day / (24 * 60 * 60 * 1000));
        return day;
    }

    /**
     * 日期格式转换
     *
     * @param src + 原日期格式
     * @param des 目标日期格式
     * @return
     */
    public static String dateFormatChange(String date, String src, String des) {
        SimpleDateFormat format = new SimpleDateFormat(src, Locale.getDefault());
        SimpleDateFormat format1 = new SimpleDateFormat(des,
                Locale.getDefault());
        try {
            return format1.format(format.parse(date));
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前系统时间
     *
     * @return
     */
    public static String getCurrentTime(String dateFormat) {
        SimpleDateFormat myFormatter = new SimpleDateFormat(dateFormat,
                Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return myFormatter.format(date);
    }

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


    /**
     * 根据日期获取一周的日期范围
     *
     * @param time
     * @return
     */
    public static String[] getWeekDateRange(String time) {
        String[] rang = new String[2];
        int weekday = getWeek(time);
        rang[0] = TimeUtil.getDateForSubDay(Calendar.DAY_OF_MONTH,
                -(weekday - 1), time, TIME_FORMAT2);
        rang[1] = TimeUtil.getDateForSubDay(Calendar.DAY_OF_YEAR, 7 - weekday,
                time, TIME_FORMAT2);
        return rang;
    }


    /**
     * 减一天的日期
     *
     * @param time
     * @return
     */
    public static long getSyncMonth(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        calendar.add(Calendar.MONTH, -2);
        Date date = calendar.getTime();
        return date.getTime();
    }

    /**
     * 减数天的日期
     *
     * @param time
     * @param day
     * @return
     */
    public static String minusDay(String time, int day) {
        return TimeUtil.getDateForSubDay(Calendar.DAY_OF_MONTH, -day, time,
                TIME_FORMAT2);
    }


    public static String getMinusDayString(long time, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        Date date = new Date(time + day * Constant.ONE_DAY_MS);
        return sdf.format(date);
    }


    public static int getTwoDay(long startTs, String standardDate) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date thisDay = myFormatter.parse(standardDate);
            return (int) ((thisDay.getTime() - startTs) / (Constant.ONE_DAY_MS));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 加一天的日期
     *
     * @param time
     * @return
     */
    public static String addDay(String time, int day) {
        return TimeUtil.getDateForSubDay(Calendar.DAY_OF_MONTH, day, time,
                TIME_FORMAT2);
    }


    /**
     * 判断当前日期是星期几
     *
     * @param pTime 设置的需要判断的时间 //格式如2012-09-08
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */

    public static int getWeek(String pTime) {

        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT2,
                Locale.getDefault());
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取年月日
     *
     * @param pTime
     * @return
     */
    public static int[] getDate(String pTime) {

        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT2,
                Locale.getDefault());
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int[] date = new int[3];
        date[0] = c.get(Calendar.DATE);
        date[1] = c.get(Calendar.MONTH);
        date[2] = c.get(Calendar.YEAR);
        return date;
    }


    /**
     * 通过当前日期获取距离当前日期数的日期
     *
     * @param
     * @param field 距离当前日期的天数
     * @return
     */
    public static String getDateForSubDay(int field, int value, String tmpDate,
                                          String dateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT2,
                Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format.parse(tmpDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(field, value);
        Date date = calendar.getTime();
        SimpleDateFormat sf = new SimpleDateFormat(dateFormat,
                Locale.getDefault());
        return sf.format(date);
    }

    /**
     * 获得当前日期和时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurDateAndTime() {
        SimpleDateFormat formatter = new SimpleDateFormat(TIME_FORMAT1, Locale.getDefault());
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 将时间戳转化成时间格式
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String parseTimes(long times) {
        return parseTimes(times, TIME_FORMAT1);
    }

    /**
     * 将时间戳转化成时间格式
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String parseTimes(long times, String fotmat) {
        SimpleDateFormat formatter = new SimpleDateFormat(fotmat, Locale.getDefault());
        Date curDate = new Date(times);//
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 获得当前日期和时间
     *
     * @return yyyy-MM-dd
     */
    public static String getCurDate() {
        return getCurDate(TIME_FORMAT2);
    }

    /**
     * 获得当前日期和时间
     *
     * @return yyyy-MM-dd
     */
    public static String getCurDate(String formt) {
        SimpleDateFormat formatter = new SimpleDateFormat(formt, Locale.getDefault());
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    public static int getAgeThroughBirthday(String birthday) {
        int age = 0;
        Calendar born = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        if (birthday != null) {
            now.setTime(new Date());
            try {
                born.setTime(new SimpleDateFormat(TimeUtil.TIME_FORMAT2).parse(birthday));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (born.after(now)) {
                throw new IllegalArgumentException("Can't be born in the future");
            }
            age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
            if (now.get(Calendar.MONTH) < born.get(Calendar.MONTH)) {
                age -= 1;
            } else if (now.get(Calendar.MONTH) == born.get(Calendar.MONTH)) {
                if (now.get(Calendar.DATE) < born.get(Calendar.DATE)) {
                    age -= 1;
                }
            }
        }
        return age;
    }


    /**
     * 转化时间戳
     *
     * @param time
     * @return
     */
    public static long getTimestamp(String time) {
        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT1, Locale.getDefault());
        Date date = new Date();
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 转化时间戳
     *
     * @param time
     * @return
     */
    public static long getTimestamp(String time, String FROMAT) {
        SimpleDateFormat format = new SimpleDateFormat(FROMAT, Locale.getDefault());
        Date date = new Date();
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static List<String> getOneYearDateString() {
        String day = getCurDate();
        List<String> result = new ArrayList<String>();
//        for (int i = 2; i > 36; i--) {
//            String dateStr = minusDay(day, i);
//            result.add(dateStr);
//        }
        String dateStr = minusDay(day, 1);
        result.add("今天");
        result.add(dateStr);
        return result;
    }

    /**
     * 格式转换
     *
     * @param time
     * @return 2016-06-01 星期三
     */
    public static String formatTime(String time, String formt) {
        Date date = new Date(TimeUtil.getTimestamp(time));
        SimpleDateFormat dateFm = new SimpleDateFormat(formt);
        return dateFm.format(date);
    }


    public static Date stringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
        }
        return convertedDate;
    }

    /**
     * 获得一段时间内的周数
     *
     * @param startTs 开始时间戳
     * @return
     */
    public static List<Integer> getWeeks(long startTs) {
        List<Integer> result = new ArrayList<Integer>();
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        cal.setTime(new Date(startTs));
        for (int i = 0; i < 12; i++) {
            int week = cal.get(Calendar.WEEK_OF_YEAR);
            result.add(week);
            cal.add(Calendar.WEEK_OF_YEAR, 1);
        }
        return result;
    }

    public static int getWeekNumberFormDate(String date) {
        long timestamp = getTimestamp(date, TIME_FORMAT2);
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        cal.setTime(new Date(timestamp));
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        return week;
    }

    public static String getWeekFirst(int year, int week, int dayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, 0, 1);

        int day = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println("day->" + day);


        calendar.add(Calendar.DATE, (week - 1) * 7 + (dayOfWeek - day));

        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");

        return sf2.format(calendar.getTime());
    }

    /**
     * 该日期是否在今天
     *
     * @param date
     * @return
     */
    public static boolean isToday(String date) {
        return getGapDays(date, getCurDate()) == 0;
    }

    /**
     * 获取距传入时间段表述
     *
     * @param ts
     * @return
     */
    public static String getTimeSlot(Context context, long ts) {
        long curTs = System.currentTimeMillis();
        if (ts > curTs) {
            return TimeUtil.parseTimes(ts, context.getString(R.string.time_month_day_hour_minute_style));
        }
        long tmpts = curTs - ts;
        if (tmpts < 1 * 60 * 1000) {
            if (tmpts / 1000 == 0) return context.getString(R.string.current_time);
            else return (tmpts / 1000) + context.getString(R.string.secound_pre);
        } else if (tmpts < 60 * 60 * 1000) {
            return (tmpts / (60 * 1000)) + context.getString(R.string.minute_pre);
        } else if (tmpts < 24 * 60 * 60 * 1000) {
            return (tmpts / (60 * 60 * 1000)) + context.getString(R.string.hour_pre);
        } else {
            return TimeUtil.parseTimes(ts, context.getString(R.string.time_month_day_hour_minute_style));
        }
    }


    public static String getTimeSlot1(Context context, long ts) {
        long curTs = System.currentTimeMillis();
        if (ts > curTs) {
            return TimeUtil.parseTimes(ts, context.getString(R.string.time_month_day_hour_minute_style));
        }
        long tmpts = curTs - ts;
        if (tmpts < 1 * 60 * 1000) {
            if (tmpts / 1000 == 0) return context.getString(R.string.current_time);
            else return (tmpts / 1000) + context.getString(R.string.secound_pre);
        } else if (tmpts < 60 * 60 * 1000) {
            return (tmpts / (60 * 1000)) + context.getString(R.string.minute_pre);
        } else if (tmpts < 24 * 60 * 60 * 1000) {
            return (tmpts / (60 * 60 * 1000)) + context.getString(R.string.hour_pre);
        } else {
            return TimeUtil.parseTimes(ts, context.getString(R.string.time_month_day_hour_minute_style));
        }
    }

    //根据秒数转化为时分秒   00:00:00
    public static String getTime(int second) {
//        if (second < 10) {
//            return "00:0" + second;
//        }
        if (second < 60) {
//            return "00:" + second ;
            return "" + second + "秒";
        }
        if (second < 3600) {
            int minute = second / 60;
            second = second - minute * 60;
            if (minute < 10) {
                if (second < 10) {
                    return "0" + minute + "分0" + second+ "秒";
                }
                return "0" + minute + "分" + second+ "秒";
            }
            if (second < 10) {
                return minute + "分0" + second+ "秒";
            }
            return minute + "分" + second+ "秒";
        }
        int hour = second / 3600;
        int minute = (second - hour * 3600) / 60;
        second = second - hour * 3600 - minute * 60;
        if (hour < 10) {
            if (minute < 10) {
                if (second < 10) {
                    return "0" + hour + ":0" + minute + ":0" + second;
                }
                return "0" + hour + ":0" + minute + ":" + second;
            }
            if (second < 10) {
                return "0" + hour + minute + ":0" + second;
            }
            return "0" + hour + minute + ":" + second;
        }
        if (minute < 10) {
            if (second < 10) {
                return hour + ":0" + minute + ":0" + second;
            }
            return hour + ":0" + minute + ":" + second;
        }
        if (second < 10) {
            return hour + minute + ":0" + second;
        }
        return hour + minute + ":" + second;
    }
}
