package com.utase1.letsmeet.activity;

/**
 * Created by akilesh on 10/8/2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.utase1.letsmeet.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class MeetingSummary extends AppCompatActivity {
    private TextView meetName;
    private TextView meetDate;
    private TextView meetTimeFrom;
    private TextView meetTimeTo;
    private TextView meetLocation;
    private Button btnConfirmmeet;
    private Button btnPrevious;
    private TextView meetParticipants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_summary);
        meetName = (TextView) findViewById(R.id.meetName);
        meetDate = (TextView) findViewById(R.id.meetDate);
        meetTimeFrom = (TextView) findViewById(R.id.meetTimeFrom);
        meetTimeTo = (TextView) findViewById(R.id.meetTimeTo);
        meetLocation = (TextView) findViewById(R.id.meetLocation);


        btnConfirmmeet = (Button) findViewById(R.id.btnConfirm);
        btnPrevious = (Button) findViewById(R.id.btnPrevious);

        meetName.setText(getIntent().getExtras().getString("MeetName"));
        meetDate.setText(getIntent().getExtras().getString("MeetDate"));
        meetTimeFrom.setText(getIntent().getExtras().getString("MeetTimeFrom"));
        meetTimeTo.setText(getIntent().getExtras().getString("MeetTimeTo"));
        meetLocation.setText(getIntent().getExtras().getString("MeetLocation"));



        //Link to Previous Page
        btnPrevious.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        CreateMeeting.class);
                startActivity(i);
                finish();
            }
        });

        //On Confirm Click  -- Here, notification will be triggered to participants

        btnConfirmmeet.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                JSONObject obj;
                try {
                    obj = new JSONObject();
                    obj.put("alert", getIntent().getExtras().getString("MeetName"));
                    obj.put("action", "com.utase1.letsmeet.activity.UPDATE_STATUS");
                    obj.put("MeetName", meetName.getText().toString());
                    obj.put("MeetDate", meetDate.getText().toString());
                    obj.put("MeetTime", meetTimeFrom.getText().toString());
                    obj.put("MeetLocation", meetLocation.getText().toString());
                    obj.put("EventorMeeting","Meeting");
                    obj.put("customdata", meetTimeFrom + " " + meetDate + " " + meetLocation);

                    String users = getIntent().getExtras().getString("Participants");

                    String[] userLists = users.split(",");

                    ParsePush push = new ParsePush();
                    ParseQuery query = ParseInstallation.getQuery();

                    // Notification for Android users
                    query.whereEqualTo("deviceType", "android");

                    query.whereContainedIn("username", Arrays.asList(userLists));

                    push.setQuery(query);
                    push.setData(obj);
                    push.sendInBackground();

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Intent i = new Intent(getApplicationContext(),
                        MeetingCreated.class);


                startActivity(i);
                finish();
            }
        });

    }

}
