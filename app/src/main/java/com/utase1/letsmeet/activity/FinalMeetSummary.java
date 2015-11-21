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

        _meetName.setText(getIntent().getExtras().getString("meet_name"));
        _meetDate.setText(getIntent().getExtras().getString("meet_date"));
        _meetTimefrom.setText(getIntent().getExtras().getString("time_from"));
        _meetTimeto.setText(getIntent().getExtras().getString("time_to"));
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

    }
}
