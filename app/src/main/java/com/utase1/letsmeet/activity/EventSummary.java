package com.utase1.letsmeet.activity;

/**
 * Created by akilesh on 10/10/2015.
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

public class EventSummary extends AppCompatActivity {

    private TextView eventName;
    private TextView eventDate;
    private TextView eventTime;
    private TextView eventLocation;
    private Button btnConfirmevent;
    private Button btnPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_summary);

        eventName = (TextView) findViewById(R.id.event_name);
        eventDate = (TextView) findViewById(R.id.event_date);
        eventTime = (TextView) findViewById(R.id.event_time);
        eventLocation = (TextView) findViewById(R.id.event_location);

        btnConfirmevent = (Button) findViewById(R.id.confirm);
        btnPrevious = (Button) findViewById(R.id.button_prev);

        eventName.setText(getIntent().getExtras().getString("EventName"));
        eventDate.setText(getIntent().getExtras().getString("EventDate"));
        eventTime.setText(getIntent().getExtras().getString("EventTime"));
        eventLocation.setText(getIntent().getExtras().getString("EventLocation"));

        //Link to Previous Page
        btnPrevious.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        CreateEvent.class);
                startActivity(i);
                finish();
            }
        });

        //On Confirm Click  -- Here, notification will be triggered to participants

        btnConfirmevent.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                JSONObject obj;
                try {
                    obj = new JSONObject();
                    obj.put("alert", getIntent().getExtras().getString("EventName"));
                    obj.put("action", "com.utase1.letsmeet.activity.UPDATE_STATUS");
                    obj.put("EventName", eventName.getText().toString());
                    obj.put("EventDate", eventDate.getText().toString());
                    obj.put("EventTime", eventTime.getText().toString());
                    obj.put("EventLocation", eventLocation.getText().toString());
                    obj.put("EventorMeeting","Event");
                    obj.put("customdata", eventTime + " " + eventDate + " " + eventLocation);

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
                        EventCreated.class);


                startActivity(i);
                finish();
            }
        });

    }

}



