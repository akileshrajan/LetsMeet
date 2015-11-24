package com.utase1.letsmeet.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.utase1.letsmeet.R;

public class AcceptDeclineMeeting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_decline_meeting);

        String MeetingName = getIntent().getExtras().getString("MeetName");
        String MeetingDate = getIntent().getExtras().getString("MeetDate");
        String MeetingTime = getIntent().getExtras().getString("MeetTime");
        String MeetingLocation = getIntent().getExtras().getString("MeetLocation");

    }
}
