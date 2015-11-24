package com.utase1.letsmeet.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.utase1.letsmeet.Model.scheduleModel;
import com.utase1.letsmeet.R;
import com.utase1.letsmeet.app.AppConfig;
import com.utase1.letsmeet.app.AppController;
import com.utase1.letsmeet.dto.ParticipantDetails;
import com.utase1.letsmeet.dto.TimeParticipantMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ashay Rajimwale on 11/14/2015.
 */
public class FreeTimeSchedulerInitiator  extends ArrayAdapter {
    private Activity activity;
    private List<TimeParticipantMap> scheduleArrayList;
    String meetingName;
    String meetingDate;
    String meetingLocation;
    private ProgressDialog pDialog;
    private LayoutInflater inflater;

    public FreeTimeSchedulerInitiator(Context context,int resource ,List<TimeParticipantMap> results,String meetingName,String meetingDate,String meetingLocation) {
        super(context,resource,results);
        scheduleArrayList =results;
        this.meetingName=meetingName;
        this.meetingDate=meetingDate;
        this.meetingLocation=meetingLocation;
        inflater = LayoutInflater.from(context);

    }




    public View getView(final int position, View convertView, final ViewGroup parent) {

        final ViewHolder holder;
        notifyDataSetChanged();
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.freetimeschedulerows, null);
            holder = new ViewHolder();

            holder.txtMeetName = (TextView) convertView.findViewById(R.id.title);
            holder.txtLocation = (TextView) convertView.findViewById(R.id.meetingLocation);
            holder.txtDate = (TextView) convertView.findViewById(R.id.meetingDate);
            holder.btnSch = (Button) convertView.findViewById(R.id.btnSch);
            //holder.lstSchedule = (ListView) convertView.findViewById(R.id.lst_sch_rows);
            holder.txtTime = (TextView) convertView.findViewById(R.id.meetingTime);
            holder.participants = (TextView) convertView.findViewById(R.id.participants);
            //YOU NEED TO ADD THE BUTTON CLICK LISTNER
            holder.btnSch.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    insertFinalMeeting(scheduleArrayList, position, parent);

                }
            });
         /*   //YOU NEED TO ADD THE BUTTON CLICK LISTNER
            holder.btnSch.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent joinIntent= new Intent(parent.getContext(),MyMeetingsEvents.class);
                    joinIntent.putExtra("meet_id","meet");
                    joinIntent.putExtra("meet_name", holder.txtMeetName.getText());
                    joinIntent.putExtra("meet_location",holder.txtLocation.getText());
                    joinIntent.putExtra("meet_date",holder.txtDate.getText());
                    joinIntent.putExtra("time_from",scheduleArrayList.get(position).getTimefrom());
                    joinIntent.putExtra("time_to",scheduleArrayList.get(position).getTimeto());
                    joinIntent.putExtra("participants",scheduleArrayList.get(position).getParticipants());
                    parent.getContext().startActivity(joinIntent);

                }
            });*/

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(scheduleArrayList.get(position).getTimeSlot()+1>12)
        {
            holder.txtTime.setText(scheduleArrayList.get(position).getTimeSlot()+"PM");
        }
        else
        {
            holder.txtTime.setText(scheduleArrayList.get(position).getTimeSlot()+"AM");
        }
        for(int i=0;i<scheduleArrayList.get(position).getParticipantDetails().size();i++)
        {
            holder.participants.setText(scheduleArrayList.get(position).getParticipantDetails().get(i).getParticipantName()+"\n");
        }

        holder.txtMeetName.setText(meetingName);
        holder.txtLocation.setText(meetingLocation);
        holder.txtDate.setText(meetingDate);



        return convertView;
    }

    private void insertFinalMeeting(List<TimeParticipantMap> participants,int position,ViewGroup parent) {
        {

            //Database

            for(int i=0;i<scheduleArrayList.get(position).getParticipantDetails().size();i++)
            {
                //users.append(scheduleArrayList.get(position).getParticipantDetails().get(i).getParticipantName() + ",");
                createFinalMeet(meetingName, meetingDate, scheduleArrayList.get(position).getTimeSlot(), meetingLocation, scheduleArrayList.get(position).getMeetingid(), parent, scheduleArrayList.get(position).getParticipantDetails().get(i).getParticipantName());
            }


            Intent joinIntent= new Intent(parent.getContext(),MeetingCreated.class);

           /* joinIntent.putExtra("meet_name", meetingName);
            joinIntent.putExtra("meet_location",meetingLocation);
            joinIntent.putExtra("meet_date",meetingDate);
            joinIntent.putExtra("meet_time",scheduleArrayList.get(position).getTimeSlot());*/

            parent.getContext().startActivity(joinIntent);
            Toast.makeText(parent.getContext(), "Meeting is scheduled", Toast.LENGTH_LONG).show();
            pushNotification(position);

        }
    }

    private void pushNotification(int position) {
        StringBuilder users=new StringBuilder();


        ParsePush push = new ParsePush();
        ParseQuery query = ParseInstallation.getQuery();
        String[] userLists = users.toString().split(",");
        query.whereEqualTo("deviceType", "android");

        query.whereContainedIn("username", Arrays.asList(users));

        push.setQuery(query);
        JSONObject obj;
        try {
            obj = new JSONObject();
            //  obj.put("alert", getIntent().getExtras().getString("MeetName"));
            obj.put("action", "com.utase1.letsmeet.activity.UPDATE_STATUS");
            obj.put("MeetName", meetingName);
            obj.put("MeetDate", meetingDate);
            obj.put("MeetTime", scheduleArrayList.get(position).getTimeSlot());
            obj.put("MeetLocation", meetingLocation);
            obj.put("meetId", scheduleArrayList.get(position).getMeetingid());
            //  obj.put("EventorMeeting", "Meeting");
            obj.put("customdata", "" + " time" + "" + "time " + "location");
            push.setData(obj);
        }
        catch (JSONException e)
        {

        }

        push.sendInBackground();
    }

    private void createFinalMeet(final String meetingName, final String meetingDate, final int timeSlot, final String meetingLocation, final String meetingid, final ViewGroup parent, final String userLists) {

        // Tag used to cancel the request
        String tag_string_req = "req_createmeet";
       /* pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Creating...");
        showDialog();*/
        pDialog.setMessage("Creating...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SCHEDULEFINALMEET, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Create Meeting Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {




                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(parent.getContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Error Creating Meeting: " + error.getMessage());
                Toast.makeText(parent.getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })  {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to create meeting url
                Map<String, String> params = new HashMap<String, String>();
                params.put("meetName", meetingName);
                params.put("meetDate", meetingDate);
                params.put("meetTimeFrom", timeSlot+"");
                params.put("meetTimeTo", timeSlot+1+"");
                params.put("meetLocation", meetingLocation);
                params.put("meetId",meetingid);
                params.put("Participants", userLists);

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

    static class ViewHolder {

        TextView txtMeetName;
        TextView txtLocation;
        TextView txtDate;
        TextView txtTime;
        TextView participants;
        ListView lstSchedule;

        Button btnSch;


    }}
