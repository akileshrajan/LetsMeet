package com.utase1.letsmeet.activity;

/**
 * Created by akilesh on 10/8/2015.
 */
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.utase1.letsmeet.app.AppConfig;
import com.utase1.letsmeet.app.AppController;
import com.utase1.letsmeet.helper.SQLiteHandler;
import com.utase1.letsmeet.helper.SessionManager;
import com.utase1.letsmeet.R;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateMeeting extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    private static final String TAG = CreateMeeting.class.getSimpleName();
    private static final String LOG_TAG = "CreateMeeting";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;
    private TextView mNameTextView;
    private TextView mAddressTextView;
    private TextView mIdTextView;
    private TextView mPhoneTextView;
    private TextView mWebTextView;
    private TextView mAttTextView;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    private TextView tvDisplayDate;
    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;
    private TextView tvDisplayTimeFrom;
    private TextView tvDisplayTimeTo;
    private int hour;
    private int minute;
    static final int TIME_DIALOG_ID_FROM = 997;
    static final int TIME_DIALOG_ID_TO = 998;

    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    private Button btnCreateMeeting;
    private EditText txtName;
    private TextView txtDate;
    private TextView txtTimefrom;
    private TextView txtTimeto;
    private TextView txtLocation;
    private EditText participants;
    private String creator_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meet);
        mGoogleApiClient = new GoogleApiClient.Builder(CreateMeeting.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id
                .autoCompleteTextView);
        mAutocompleteTextView.setThreshold(3); // The minimum number of letters you should type in before suggestion appears
        mNameTextView = (TextView) findViewById(R.id.name);
        mAddressTextView = (TextView) findViewById(R.id.address);
        mIdTextView = (TextView) findViewById(R.id.place_id);
        mPhoneTextView = (TextView) findViewById(R.id.phone);
        mWebTextView = (TextView) findViewById(R.id.web);
        mAttTextView = (TextView) findViewById(R.id.att);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

        setCurrentDateOnView();
        setCurrentTimeOnView();
        //addListenerOnButton();
        addListenerOnButtonnew();
        addListenerOnTime();

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. get his details
            HashMap<String, String> user = db.getUserDetails();

            //String name = user.get("name");
            creator_email = user.get("email");


        }

        btnCreateMeeting = (Button) findViewById(R.id.btncreatemeet);
        txtName = (EditText) findViewById(R.id.meeting_name);
        txtDate = (TextView) findViewById(R.id.meeting_date);
        txtTimefrom = (TextView) findViewById(R.id.meeting_timefrom);
        txtTimeto = (TextView) findViewById(R.id.meeting_timeto);

        participants = (EditText) findViewById(R.id.participants);

        btnCreateMeeting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Intent intent = new Intent(this,MeetingSummary.class);
                //Validation of inputs

                String meetName = txtName.getText().toString();
                String meetDate = txtDate.getText().toString();
                String meetTimefrom = txtTimefrom.getText().toString();
                String meetTimeto = txtTimeto.getText().toString();
                String meetLocation = mAutocompleteTextView.getText().toString();
                String meetParticipants = participants.getText().toString();

                txtTimefrom.setHint("Click here to choose time");
                txtDate.setHint("Click here to choose date");

                if(TextUtils.isEmpty(meetName)&&TextUtils.isEmpty(meetDate)&&TextUtils.isEmpty(meetLocation)&&TextUtils.isEmpty(meetTimefrom)&&TextUtils.isEmpty(meetTimeto)) {
                    Toast.makeText(getApplicationContext(), "Please enter all the fields", Toast.LENGTH_LONG).show();
                }
                else
                {
                    createMeeting(meetName, meetDate, meetTimefrom, meetTimeto, meetLocation, creator_email, meetParticipants);
                }

            }

        });


    }
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            //mNameTextView.setText(Html.fromHtml(place.getName() + ""));
            //mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
            //mIdTextView.setText(Html.fromHtml(place.getId() + ""));
            //if(Html.fromHtml(place.getPhoneNumber() + "")!=null){
                //mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));//}
            //else
                //mPhoneTextView.setText("No Phone to display");
            //if(Html.fromHtml(place.getWebsiteUri()+"")!=null){
                //mWebTextView.setText(place.getWebsiteUri() + "");//}
           // else
                //mWebTextView.setText("No website to display");
            //if (attributions != null) {
                //mAttTextView.setText(Html.fromHtml(attributions.toString()));
            //}
        }

    };


    private void setCurrentTimeOnView() {
        tvDisplayTimeFrom = (TextView) findViewById(R.id.meeting_timefrom);
        tvDisplayTimeTo = (TextView) findViewById(R.id.meeting_timeto);
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        // set current time into textview
        tvDisplayTimeFrom.setText(
                new StringBuilder().append(pad(hour))
                        .append(":").append(pad(minute)));

        tvDisplayTimeTo.setText(
                new StringBuilder().append(pad(hour))
                        .append(":").append(pad(minute)));


    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID_FROM:
                // set time picker as current time
                return new TimePickerDialog(this,
                        timePickerListenerFrom, hour, minute,false);
            case TIME_DIALOG_ID_TO:
                // set time picker as current time
                return new TimePickerDialog(this,
                        timePickerListenerTo, hour, minute,false);
            case DATE_DIALOG_ID:
                // set time picker as current time
                return new DatePickerDialog(this, datePickerListener,
                        year, month,day);


        }
        return null;
    }



    public void addListenerOnButtonnew() {

        tvDisplayDate = (TextView) findViewById(R.id.meeting_date);

        tvDisplayDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID);

            }

        });
    }
    public void addListenerOnTime() {
        tvDisplayTimeFrom = (TextView) findViewById(R.id.meeting_timefrom);
        tvDisplayTimeFrom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID_FROM);

            }

        });
        tvDisplayTimeTo = (TextView) findViewById(R.id.meeting_timeto);
        tvDisplayTimeTo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID_TO);

            }

        });


    }

    // display current date
    public void setCurrentDateOnView() {

        tvDisplayDate = (TextView) findViewById(R.id.meeting_date);
        //dpResult = (DatePicker) findViewById(R.id.dpResult);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        tvDisplayDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(month + 1).append("-").append(day).append("-")
                .append(year).append(" "));

        // set current date into datepicker
        //dpResult.init(year, month, day, null);

    }





    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview
            tvDisplayDate.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));

            // set selected date into datepicker also


        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListenerFrom =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;

                    // set current time into textview
                    tvDisplayTimeFrom.setText(new StringBuilder().append(pad(hour))
                            .append(":").append(pad(minute)));



                }
            };
    private TimePickerDialog.OnTimeSetListener timePickerListenerTo =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;

                    // set current time into textview
                    tvDisplayTimeTo.setText(new StringBuilder().append(pad(hour))
                            .append(":").append(pad(minute)));



                }
            };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    public void createMeeting(final String name, final String date, final String timeFrom, final String timeTo, final String location, final String email, final String participants) {

        // Tag used to cancel the request
        String tag_string_req = "req_createmeet";

        pDialog.setMessage("Creating...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_CREATEMEET, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Create Meeting Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        // User successfully stored in MySQL
                        Toast.makeText(getApplicationContext(), "Meeting successfully created!", Toast.LENGTH_LONG).show();

                        // Launch Meeting summary activity
                        Intent intent = new Intent(CreateMeeting.this, MeetingSummary.class);

                        intent.putExtra("MeetName",name);
                        intent.putExtra("MeetDate",date);
                        intent.putExtra("MeetTimeFrom",timeFrom);
                        intent.putExtra("MeetTimeTo",timeTo);
                        intent.putExtra("MeetLocation",location);
                        intent.putExtra("Participants",participants);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
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
                Log.e(TAG, "Error Creating Meeting: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })  {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to create meeting url
                Map<String, String> params = new HashMap<String, String>();
                params.put("meetName", name);
                params.put("meetDate", date);
                params.put("meetTimeFrom", timeFrom);
                params.put("meetTimeTo", timeTo);
                params.put("meetLocation", location);
                params.put("createdBy",email);
                params.put("Participants", participants);

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

    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

}

