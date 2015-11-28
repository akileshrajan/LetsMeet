package com.utase1.letsmeet.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.utase1.letsmeet.R;
import com.utase1.letsmeet.helper.SQLiteHandler;
import com.utase1.letsmeet.helper.SessionManager;

import java.util.HashMap;

/**
 * Created by akilesh on 11/19/2015.
 */
public class FinalMeetSummary extends AppCompatActivity {
    private TextView _meetName;
    private TextView _meetDate;
    private TextView _meetTimefrom;
    private TextView _meetTimeto;
    private TextView _meetLocation;
    private TextView _meet_participants;
    private Button _button_prev;
    private Button _button_drive;
    private Button _button_map;
    private SessionManager session;
    private SQLiteHandler db;
    private String txtemail;

    private ProgressDialog pDialog;
    private static final String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_meet_summary);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // session manager
        session = new SessionManager(getApplicationContext());

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();
        txtemail = user.get("email");


        _meetName=(TextView)findViewById(R.id.meet_name);
        _meetDate=(TextView)findViewById(R.id.meet_date);
        _meetTimefrom=(TextView)findViewById(R.id.time_from);
        _meetTimeto=(TextView)findViewById(R.id.time_to);
        _meetLocation=(TextView)findViewById(R.id.meet_location);
        //_meet_participants =(TextView) findViewById(R.id.meet_part);
        _button_prev = (Button) findViewById(R.id.button_prev_meet);
        _button_map = (Button) findViewById(R.id.mapButton);
        _button_drive = (Button) findViewById(R.id.driveButton);

        _meetName.setText(getIntent().getExtras().getString("meet_name"));
        _meetDate.setText(getIntent().getExtras().getString("meet_date"));
        if(Integer.parseInt(getIntent().getExtras().getString("time_from"))>12)
        {
            _meetTimefrom.setText(getIntent().getExtras().getString("time_from")+"PM");
        }
        else
        {
            _meetTimefrom.setText(getIntent().getExtras().getString("time_from")+"AM");
        }
        if(Integer.parseInt(getIntent().getExtras().getString("time_to"))>12)
        {
            _meetTimeto.setText(getIntent().getExtras().getString("time_to")+"PM");
        }
        else
        {
            _meetTimeto.setText(getIntent().getExtras().getString("time_to")+"AM");
        }
        _meetLocation.setText(getIntent().getExtras().getString("meet_location"));
        //_meet_participants.setText(getIntent().getExtras().getString("participants"));

        _button_prev.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MySchedule.class);
                startActivity(i);
                finish();
            }
        });

        _button_map.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MapsActivity.class);
                // Adding intent to make sure when we come back to this page after map activity, we have all the data we need.
                i.putExtra("meet_name", getIntent().getExtras().getString("meet_name"));
                i.putExtra("meet_date", getIntent().getExtras().getString("meet_date"));
                i.putExtra("time_from", getIntent().getExtras().getString("time_from"));
                i.putExtra("time_to", getIntent().getExtras().getString("time_to"));
                i.putExtra("meet_location", getIntent().getExtras().getString("meet_location"));
                i.putExtra("location", getIntent().getExtras().getString("meet_location"));
                startActivity(i);
                finish();
            }
        });

        _button_drive.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        EventUpload.class);
                // Adding intent to make sure when we come back to this page after drive activity, we have all the data we need.
                i.putExtra("meet_name", getIntent().getExtras().getString("meet_name"));
                i.putExtra("meet_date", getIntent().getExtras().getString("meet_date"));
                i.putExtra("time_from", getIntent().getExtras().getString("time_from"));
                i.putExtra("time_to", getIntent().getExtras().getString("time_to"));
                i.putExtra("meet_location", getIntent().getExtras().getString("meet_location"));
                i.putExtra("event_or_meet", "MEET");
                startActivity(i);
                finish();
            }
        });
    }
}
