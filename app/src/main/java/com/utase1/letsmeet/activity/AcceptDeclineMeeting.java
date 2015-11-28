package com.utase1.letsmeet.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.utase1.letsmeet.R;
import com.utase1.letsmeet.app.AppConfig;
import com.utase1.letsmeet.app.AppController;
import com.utase1.letsmeet.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AcceptDeclineMeeting extends AppCompatActivity {
    String meetingName="";
    String meetingDate="";
    String meetingTime="";
    String meetingLocation="";
    private ProgressDialog pDialog;
    private String email="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_decline_meeting);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
         meetingName = getIntent().getExtras().getString("MeetName");
         meetingDate = getIntent().getExtras().getString("MeetDate");
         meetingTime = getIntent().getExtras().getString("MeetTime");
         meetingLocation = getIntent().getExtras().getString("MeetLocation");
        TextView meetName= (TextView)findViewById(R.id.meetNameId);
        TextView date= (TextView)findViewById(R.id.meetdateid);
        TextView time= (TextView)findViewById(R.id.timeid);
       TextView location= (TextView)findViewById(R.id.locationid);

        meetName.setText(meetingName);
        date.setText(meetingDate);
        location.setText(meetingLocation);
        time.setText(meetingTime);
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        email = user.get("email");


    }
    public void acceptMeeting(View view)
    {
        
        String status = "1";

        userRespondEvent(meetingName, meetingTime, meetingDate,email, status,meetingLocation);
        Intent i = new Intent(getApplicationContext(), MySchedule.class);
        startActivity(i);
    }


    private void userRespondEvent(final String meetingName, final String meetingTime, final String meetingDate, final String email, final String status, final String location)
    {

        // Tag used to cancel the request
        String tag_string_req = "req_login";
        pDialog.setMessage("Processing...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ACCEPTDECLINEMEET, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "User Time Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        Toast.makeText(getApplicationContext(), "Processing Completed", Toast.LENGTH_LONG).show();
                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               // Log.e(TAG, "Entry Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("emailId", email);
                params.put("meetName", meetingName);
                params.put("status",status);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void declineMeet(View v)
    {

        String status="0";

        userRespondEvent(meetingName, meetingTime, meetingDate,"android2015@gmail.com", status,meetingLocation);
        Intent i = new Intent(getApplicationContext(), MySchedule.class);
        startActivity(i);
    }
}
