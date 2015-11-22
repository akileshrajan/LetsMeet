package com.utase1.letsmeet.activity;

/**
 * Created by akilesh on 10/10/2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.utase1.letsmeet.R;
import com.utase1.letsmeet.app.AppConfig;
import com.utase1.letsmeet.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EventSummary extends AppCompatActivity {

    private TextView eventName;
    private TextView eventDate;
    private TextView eventTime;
    private TextView eventLocation;
    private Button btnConfirmevent;
    private Button btnPrevious;
    private static final String TAG = "EventSummary";

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
                //JSONObject obj;
                StringBuilder message;
                try {
                    /*obj = new JSONObject();
                    obj.put("alert", getIntent().getExtras().getString("EventName"));
                    obj.put("action", "com.utase1.letsmeet.activity.UPDATE_STATUS");
                    obj.put("EventName", eventName.getText().toString());
                    obj.put("EventDate", eventDate.getText().toString());
                    obj.put("EventTime", eventTime.getText().toString());
                    obj.put("EventLocation", eventLocation.getText().toString());
                    obj.put("EventorMeeting","Event");
                    obj.put("customdata", eventTime + " " + eventDate + " " + eventLocation);
                    */
                    message = new StringBuilder();
                    message.append("Event;");
                    message.append(eventName.getText().toString() + ";");
                    message.append(eventDate.getText().toString() + ";");
                    message.append(eventTime.getText().toString() + ";");
                    if (!eventLocation.getText().toString().isEmpty()){
                        message.append(eventLocation.getText().toString());}
                        //message.append("event location");}
                    else{
                        message.append("NO LOCATION");
                    }
                    String users = getIntent().getExtras().getString("Participants");

                    String[] userLists = users.split(",");

                    StringBuilder MailIds = new StringBuilder();

                    for (int i = 0; i < userLists.length; i++) {
                        MailIds.append("'" + userLists[i] + "'");

                        if (i != userLists.length-1){
                            MailIds.append(",");
                        }
                    }

                    Log.d(TAG, "Sending Message To: " + MailIds.toString());
                    Log.d(TAG, "Sending Message: " + message.toString());

                    sendMessage(MailIds.toString(), message.toString());

                   /* ParsePush push = new ParsePush();
                    ParseQuery query = ParseInstallation.getQuery();

                    // Notification for Android users
                    query.whereEqualTo("deviceType", "android");

                    query.whereContainedIn("username", Arrays.asList(userLists));

                    push.setQuery(query);
                    push.setData(obj);
                    push.sendInBackground();*/

                } catch (Exception e) {
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

    public void sendMessage(final String mailIDs, final String message) {

        // Tag used to cancel the request
        String tag_string_req = "req_sendmessgcm";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SENDMESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Send Message Response: " + response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error Sending Message GCM: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to create meeting url
                Map<String, String> params = new HashMap<String, String>();
                params.put("pushMessage", message);
                params.put("mail_ids", mailIDs);

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}



