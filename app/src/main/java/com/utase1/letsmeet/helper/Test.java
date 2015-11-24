package com.utase1.letsmeet.helper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("ValidFragment")
public class Test extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    private TextView time;

    private String militaryHour;
    private String militaryMinute;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
         public Test() {}

             	public Test(TextView time)
     	{
         		this.time = time;


         	}

    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour)
    	{
         		// set the 24-hour, military time
         		this.militaryHour = Integer.toString(hourOfDay);
        		this.militaryMinute = Integer.toString(minuteOfHour);







            // set full 'pretty' time, i.e. 2:45 PM
            if(hourOfDay>12)
            {
                time.setText(hourOfDay+":00"+" PM");

            }
            else
            {
                time.setText(hourOfDay+":00"+" AM");

            }

         	}

             	/** Returns the time in HH:SS format. */
             	public String getTime()
     	{
        		return militaryHour + ":" + militaryMinute;
         	}






}