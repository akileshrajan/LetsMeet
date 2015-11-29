package com.utase1.letsmeet.activity;

/**
 * Created by akilesh on 10/12/2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.utase1.letsmeet.R;


public class MeetingCreated extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_created);

        Button btnMyschedule = (Button) findViewById(R.id.btnLinkToscheduleScreen_meet);

        //Link to My Schedule Page -- Need to change to My Schedule
        btnMyschedule.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MySchedule.class);
                startActivity(i);
                finish();
            }
        });
    }

}
