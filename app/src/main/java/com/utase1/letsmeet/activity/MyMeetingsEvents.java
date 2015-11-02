package com.utase1.letsmeet.activity;

import android.app.ProgressDialog;
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
import com.utase1.letsmeet.app.AppConfig;
import com.utase1.letsmeet.app.AppController;
import com.utase1.letsmeet.dto.DataWrapper;
import com.utase1.letsmeet.dto.ParticipantDetails;
import com.utase1.letsmeet.dto.TimeParticipantMap;
import com.utase1.letsmeet.helper.ScheduleTime;

import com.utase1.letsmeet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by akilesh on 10/31/2015.
 */
public class MyMeetingsEvents extends AppCompatActivity {
    private static final String TAG = MyMeetingsEvents.class.getSimpleName();
    private TextView meetName;
    private TextView meetDate;
    private TextView meetTimefrom;
    private TextView meetTimeto;
    private TextView meetLocation;
    private Button btnProceed;
    private Button btnPrevious;
    private TextView meetParticipants;

    ArrayList<ParticipantDetails> partList = new ArrayList<ParticipantDetails>();
    private ProgressDialog pDialog;
    private String meet_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_meetings_events);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        meetName=(TextView)findViewById(R.id.meetName);
        meetDate=(TextView)findViewById(R.id.meetDate);
        meetTimefrom=(TextView)findViewById(R.id.meetTimeFrom);
        meetTimeto = (TextView)findViewById(R.id.meetTimeTo);
        meetLocation=(TextView)findViewById(R.id.meetLocation);
        meetParticipants =(TextView)findViewById(R.id.meetParticipants);

        btnProceed = (Button)findViewById(R.id.btnProceed);
        btnPrevious = (Button)findViewById(R.id.btnPrevious);

        final Bundle extras = getIntent().getExtras();
        meet_id = extras.getString("meet_id");


        meetName.setText(extras.getString("meet_name"));
        meetLocation.setText(extras.getString("meet_location"));
        meetDate.setText(extras.getString("meet_date"));
        meetTimefrom.setText(extras.getString("time_from"));
        meetTimeto.setText(extras.getString("time_to"));
        meetParticipants.setText(extras.getString("participants"));

        //Link to Previous Page
        btnPrevious.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MySchedule.class);
                startActivity(i);
                finish();
            }
        });

        //Proceed Button on Click
        btnProceed.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                createData(meet_id);
            }
        });


    }

    private void createData(final String meetingId) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Getting users and their free time data  ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_getUsersAndFreeTime, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "getUsersAndFreeTime Response: " + response);
                hideDialog();

                try {
                    JSONObject jsonObj = new JSONObject(response);
                    if(jsonObj!=null)
                    {    //session.setLogin(true);
                        JSONArray jsonArray = jsonObj.getJSONArray("users");

                        for (int i = 0 ; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String email = obj.getString("email");
                            Log.i("email",email);
                            String fromTime = obj.getString("from_time");
                            Log.i("fromTime",fromTime);
                            String toTime = obj.getString("to_time");
                            ParticipantDetails participantDetails=new ParticipantDetails();
                            participantDetails.setParticipantName(email);
                            participantDetails.setStartTime(Integer.parseInt(fromTime));
                            participantDetails.setEndTime(Integer.parseInt(toTime));
                            Log.i("startTime", participantDetails.getStartTime() + "");
                            Log.i("endTime",participantDetails.getEndTime()+"");
                            partList.add(participantDetails);

                        }
                        calculateFreeTime();
                    }

                    else {
                        // Error in login. Get the error message
                        String errorMsg = jsonObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
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
                Log.e(TAG, "No Users Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("meeting_id", meetingId);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        // return partArrayList;

    }

    private void calculateFreeTime() {
        if (partList != null && partList.size() > 0) {
            ScheduleTime scheduleTest=new ScheduleTime();
            ArrayList<TimeParticipantMap> commonFreeTime= scheduleTest.getFreeTime(partList);
            Intent intent = new Intent(MyMeetingsEvents.this, GenerateFreeCommonTime.class);
            Bundle bundleObject = new Bundle();
            bundleObject.putSerializable("finalParticipants", new DataWrapper(commonFreeTime));
            intent.putExtras(bundleObject);
            MyMeetingsEvents.this.startActivity(intent);
            //intent.putExtra("finalParticipants", new DataWrapper(finalParticipants));
            //   intent.putExtra("finalParticipants", finalParticipants);

            finish();
        }


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
