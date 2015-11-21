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
public class FinalEventSummary extends AppCompatActivity {
    private TextView _eventName;
    private TextView _eventDate;
    private TextView _eventTime;
    private TextView _eventLocation;
    private Button _button_prev;
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
        setContentView(R.layout.activity_final_event_summary);

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
        _button_prev = (Button) findViewById(R.id.button_prev);

        _eventName.setText(getIntent().getExtras().getString("event_name"));
        _eventDate.setText(getIntent().getExtras().getString("event_date"));
        _eventTime.setText(getIntent().getExtras().getString("event_time"));
        _eventLocation.setText(getIntent().getExtras().getString("event_location"));


        _button_prev.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MySchedule.class);
                startActivity(i);
                finish();
            }
        });

    }
}
