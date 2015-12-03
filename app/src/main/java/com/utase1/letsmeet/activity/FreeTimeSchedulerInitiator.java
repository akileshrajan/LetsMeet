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
import com.utase1.letsmeet.Model.scheduleModel;
import com.utase1.letsmeet.R;
import com.utase1.letsmeet.app.AppConfig;
import com.utase1.letsmeet.app.AppController;
import com.utase1.letsmeet.dto.TimeParticipantMap;

import org.json.JSONException;
import org.json.JSONObject;

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
    private static final String TAG = "FreeTimeSchedulerInitiator";
    private LayoutInflater inflater;
    private ProgressDialog pDialog;
    StringBuilder users=new StringBuilder();

    public FreeTimeSchedulerInitiator(Context context,int resource ,List<TimeParticipantMap> results,String meetingName,String meetingDate,String meetingLocation) {
        super(context, resource, results);
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

                public void onClick(View view) {

                    insertFinalMeeting(scheduleArrayList, position, parent);
                    pushNotification(position);

                    // Add the activity to where you want to redirect to after sending the notification.
                }
            });

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
        StringBuilder partStringBuilder= partStringBuilder=new StringBuilder();
        for(int i=0;i<scheduleArrayList.get(position).getParticipantDetails().size();i++)
        {


            partStringBuilder.append(scheduleArrayList.get(position).getParticipantDetails().get(i).getParticipantName() + "\n");

        }
        holder.participants.setText(partStringBuilder.toString());
        holder.txtMeetName.setText(meetingName);
        holder.txtLocation.setText(meetingLocation);
        holder.txtDate.setText(meetingDate);

        return convertView;
    }




    static class ViewHolder {

        TextView txtMeetName;
        TextView txtLocation;
        TextView txtDate;
        TextView txtTime;
        TextView participants;
        ListView lstSchedule;

        Button btnSch;


    }

    public void sendMessage(final String mailIDs, final String message) {

        // Tag used to cancel the request
        String tag_string_req = "req_sendmessgcm";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SENDMESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Send Message Response: " + response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.e(TAG, "Error Sending Message GCM: " + error.getMessage());
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
    private void insertFinalMeeting(List<TimeParticipantMap> participants,int position,ViewGroup parent) {
        {

            //Database

            for(int i=0;i<scheduleArrayList.get(position).getParticipantDetails().size();i++)
            {
                users.append(scheduleArrayList.get(position).getParticipantDetails().get(i).getParticipantName() + ",");
                Integer time_to = scheduleArrayList.get(position).getTimeSlot();
                time_to += 1;
                createFinalMeet(meetingName, meetingDate, Integer.toString(scheduleArrayList.get(position).getTimeSlot()),Integer.toString(time_to), meetingLocation, scheduleArrayList.get(position).getMeetingid(), parent, scheduleArrayList.get(position).getParticipantDetails().get(i).getParticipantName());
            }


            Intent joinIntent= new Intent(parent.getContext(),MeetingCreated.class);

           /* joinIntent.putExtra("meet_name", meetingName);
            joinIntent.putExtra("meet_location",meetingLocation);
            joinIntent.putExtra("meet_date",meetingDate);
            joinIntent.putExtra("meet_time",scheduleArrayList.get(position).getTimeSlot());*/

            parent.getContext().startActivity(joinIntent);
            Toast.makeText(parent.getContext(), "Meeting is scheduled", Toast.LENGTH_LONG).show();
            //pushNotification(position);

        }
    }
    private void pushNotification(int position) {
        try
        {
            StringBuilder  message = new StringBuilder();
            message.append("FreeTimeScheduler;");
            message.append(meetingName + ";");
            message.append(meetingDate + ";");
            message.append(scheduleArrayList.get(position).getTimeSlot() + ";");
            Integer time_to = scheduleArrayList.get(position).getTimeSlot();
            time_to += 1;
            message.append(Integer.toString(time_to) + ";");
           // message.append(scheduleArrayList.get(position).getMeetingid()+ ";");

            if (!meetingLocation.isEmpty()) {
                message.append(meetingLocation);
            } else {
                message.append("NO LOCATION");
            }

            String[] userLists = users.toString().split(",");
            StringBuilder MailIds = new StringBuilder();

            for (int i = 0; i < userLists.length; i++) {
                MailIds.append("'" + userLists[i] + "'");

                if (i != userLists.length - 1) {
                    MailIds.append(",");
                }
            }

            //  Log.d(TAG, "Sending Message To: " + MailIds.toString());
            // Log.d(TAG, "Sending Message: " + message.toString());
            System.out.println("Mail Ids to send notification"+MailIds.toString());
            sendMessage(MailIds.toString(), message.toString());


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    private void createFinalMeet(final String meetingName, final String meetingDate, final String timeSlot,final String timeSlotTo, final String meetingLocation, final String meetingid, final ViewGroup parent, final String userLists) {

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
                params.put("meetTimeFrom", timeSlot);
                params.put("meetTimeTo", timeSlotTo);
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
}
