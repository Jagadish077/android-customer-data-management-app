package com.example.anmolpipes.datahelperclasses;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {
    public DateFormatter() {
    }

    public String getTime(String dateTime) throws ParseException {
        Log.d("date", ""+dateTime);
        String originalString = "2021-07-06 18:17:13";
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime);
        String newString = new SimpleDateFormat("hh:mm aa").format(date);
        return newString;
    }
    public String getDate(String dateTime) throws ParseException {
        Log.d("date2", ""+dateTime);
        String originalString = "2021-07-06 18:17:13";
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime);
        String newString = new SimpleDateFormat("dd:MM:yyyy").format(date);
        return newString;
    }

    public String getTime() throws ParseException {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss aa");
        String strDate= formatter.format(date);
        System.out.println(strDate);
        return strDate;
    }
}
