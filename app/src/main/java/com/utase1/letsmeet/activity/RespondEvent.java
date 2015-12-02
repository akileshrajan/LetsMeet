package com.utase1.letsmeet.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Dialog;
import android.app.ProgressDialog;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.utase1.letsmeet.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RespondEvent extends AppCompatActivity {


    private TextView _eventName;
    private TextView _eventDate;
    private TextView _eventTime;
    private TextView _eventLocation;
    private Button btnAccept;
    private Button btnDecline;
    private SessionManager session;
    private SQLiteHandler db;
    private String txtemail;
    private String status;
    private Integer value;
    private ProgressDialog pDialog;
    private static final String TAG = RegisterActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respond_event);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // session manager
        session = new SessionManager(getApplicationContext());

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();
        txtemail = user.get("email");


        _eventName=(TextView)findViewById(R.id.event_name);
        _eventDate=(TextView)findViewById(R.id.event_date);
        _eventTime=(TextView)findViewById(R.id.event_time);
        _eventLocation=(TextView)findViewById(R.id.event_location);

        btnAccept = (Button) findViewById(R.id.accept_event);
        btnDecline = (Button) findViewById(R.id.decline_event);

        _eventName.setText(getIntent().getExtras().getString("EventName"));
        _eventDate.setText(getIntent().getExtras().getString("EventDate"));
        _eventTime.setText(getIntent().getExtras().getString("EventTime"));
        _eventLocation.setText(getIntent().getExtras().getString("EventLocation"));

        btnAccept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                value = 1;
                status = value.toString();
                String eventName = _eventName.getText().toString().trim();
                String eventTime = _eventTime.getText().toString().trim();
                String eventDate = _eventDate.getText().toString().trim();

                userRespondEvent(eventName, eventTime, eventDate, txtemail, status);
                Intent i = new Intent(getApplicationContext(), MySchedule.class);
                startActivity(i);
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                value = 0;
                status = value.toString();
                String eventName = _eventName.getText().toString().trim();
                String eventTime = _eventTime.getText().toString().trim();
                String eventDate = _eventDate.getText().toString().trim();

                userRespondEvent(eventName, eventTime, eventDate, txtemail, status);
                Intent i = new Intent(getApplicationContext(), MySchedule.class);
                startActivity(i);
            }
        });

    }

    public void userRespondEvent(final String eventName, final String eventTime, final String eventDate, final String txtemail, final String status)
    {

        // Tag used to cancel the request
        String tag_string_req = "req_login";
        pDialog.setMessage("Processing...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GETEVENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "User Time Response: " + response);
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
                Log.e(TAG, "Entry Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("emailId", txtemail);
                params.put("eventName", eventName);
                params.put("eventTime",eventTime);
                params.put("eventDate",eventDate);
                params.put("eventStatus",status);

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


}
