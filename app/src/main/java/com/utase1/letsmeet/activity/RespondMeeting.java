package com.utase1.letsmeet.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.RadioButton;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.utase1.letsmeet.R;
import com.utase1.letsmeet.app.AppConfig;
import com.utase1.letsmeet.app.AppController;
import com.utase1.letsmeet.helper.SQLiteHandler;
import com.utase1.letsmeet.helper.SessionManager;
import com.utase1.letsmeet.helper.Test;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import android.widget.CompoundButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RespondMeeting extends AppCompatActivity  implements NumberPicker.OnValueChangeListener {
    private static final String TAG = RegisterActivity.class.getSimpleName();


    private TextView _meetName;
    private TextView _meetDate;
    private TextView _meetTime;
    private TextView _meetLocation;
    private Button btnRespondMeet;
    private TextView txtTimefrom;
    private TextView txtTimeto;
    private ProgressDialog pDialog;

    private SQLiteHandler db;
    private SessionManager session;
    private String txtEmail;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respond_meeting);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());
        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        //String name = user.get("name");
        txtEmail = user.get("email");

        // Displaying the user details on the screen
        //txtName.setText(name);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        txtTimefrom = (TextView) findViewById(R.id.meeting_timefrom);
        txtTimeto = (TextView) findViewById(R.id.meeting_timeto);

        _meetName=(TextView)findViewById(R.id.textView3);
        _meetDate=(TextView)findViewById(R.id.textView4);
        _meetTime=(TextView)findViewById(R.id.textView5);
        _meetLocation=(TextView)findViewById(R.id.textView6);
        setTimePicker(txtTimefrom,txtTimeto);
        RadioButton repeatChkBx = (RadioButton) findViewById( R.id.userCalTime );
        repeatChkBx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtTimefrom.setVisibility(View.GONE);
                    txtTimeto.setVisibility(View.GONE);
                    Intent calIntent = new Intent(getApplicationContext(),CalendarActivity.class);
                    calIntent.putExtra("MeetName",getIntent().getExtras().getString("MeetName"));
                    calIntent.putExtra("Mail",txtEmail);
                    calIntent.putExtra("MeetDate",getIntent().getExtras().getString("MeetDate"));
                    startActivity(calIntent);
                }

            }
        });
        RadioButton userFreeTime = (RadioButton) findViewById( R.id.userFreeTime);
        userFreeTime.setChecked(true);
        userFreeTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtTimefrom.setVisibility(View.VISIBLE);
                    txtTimeto.setVisibility(View.VISIBLE);
                }

            }
        });



        btnRespondMeet = (Button) findViewById(R.id.button_respond);
        // addTime = (Button) findViewById(R.id.addTime);

        _meetName.setText(getIntent().getExtras().getString("MeetName"));
        _meetDate.setText(getIntent().getExtras().getString("MeetDate"));
        _meetTime.setText(getIntent().getExtras().getString("MeetTime"));
        _meetLocation.setText(getIntent().getExtras().getString("MeetLocation"));

        btnRespondMeet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String meetName = _meetName.getText().toString().trim();
                String fromTime = txtTimefrom.getText().toString().trim();
                String toTime = txtTimeto.getText().toString().trim();
                try
                {
                    java.text.DateFormat formatter  = new SimpleDateFormat("hh:mm");
                    Date  date =  (Date)formatter.parse(fromTime);
                    fromTime=date.getHours()+"";
                    System.out.print("fromtime"+fromTime);
                    Date  date1 =  (Date)formatter.parse(toTime);
                    toTime=date1.getHours()+"";
                    System.out.print("totime"+toTime);


                }
                catch(ParseException ps)
                {
                    ps.printStackTrace();
                }
                registerFreeUserTime(meetName,fromTime,toTime,txtEmail);
                Intent i = new Intent(getApplicationContext(), MySchedule.class);
                startActivity(i);
            }
        });
      /*  addTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });*/

    }

    private void setTimePicker(TextView txtTimefrom, TextView txtTimeto) {

        final Calendar c = Calendar.getInstance();

        int hour = c.get(Calendar.HOUR_OF_DAY);
        if(hour<12)
        {
            int  minute = c.get(Calendar.MINUTE);
            this.txtTimefrom.setText(hour + ":" + minute + "am");
            this.txtTimeto.setText(hour + ":" + minute + "pm");
        }
        else
        {
            int  minute = c.get(Calendar.MINUTE);
            this.txtTimefrom.setText(hour + ":" + minute + "am");
            this.txtTimeto.setText(hour + ":" + minute + "pm");
        }

    }
    public void showTimePickerDialog(View v) {
        switch(v.getId())  //get the id of the view clicked. (in this case button)
        {
            case R.id.meeting_timefrom : // if its button1
                this.txtTimefrom = (TextView) findViewById(R.id.meeting_timefrom);
                DialogFragment newFragment = new Test(this.txtTimefrom);
                newFragment.show(RespondMeeting.this.getFragmentManager(), "setTime");
                break;
            case R.id.meeting_timeto : // if its button1
                //do something
                this.txtTimeto = (TextView) findViewById(R.id.meeting_timeto);
                DialogFragment newFragment1 = new Test(this.txtTimeto);
                newFragment1.show(RespondMeeting.this.getFragmentManager(), "setTime");
                break;
        }

    }

    public void registerFreeUserTime(final String meetName,final String fromTime,final String toTime,final String email) {

        // Tag used to cancel the request
        String tag_string_req = "req_login";
        pDialog.setMessage("Processing...");



        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_USERTIME, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "User Time Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        Toast.makeText(getApplicationContext(), "Processing Completed", Toast.LENGTH_LONG).show();
                    } else {
                        // Error in login. Get the error message
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
                params.put("emailId", email);
                params.put("meetingName", meetName);
                params.put("startTime",fromTime);
                params.put("endTime",toTime);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_respond_meeting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.i("value is",""+newVal);


    }
}
