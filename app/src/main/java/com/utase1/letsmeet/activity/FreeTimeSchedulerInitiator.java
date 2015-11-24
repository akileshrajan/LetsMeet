package com.utase1.letsmeet.activity;

import android.app.Activity;
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
                    StringBuilder message;
                    try
                    {
                        message = new StringBuilder();
                        message.append("FreeTimeScheduler;");
                        message.append(holder.txtMeetName.toString() + ";");
                        message.append(holder.txtDate + ";");
                        message.append(holder.txtTime.toString() + ";");
                        if (!holder.txtLocation.toString().isEmpty()) {
                            message.append(holder.txtLocation.toString());
                        } else {
                            message.append("NO LOCATION");
                        }

                        String[] userLists = holder.participants.toString().split(",");
                        StringBuilder MailIds = new StringBuilder();

                        for (int i = 0; i < userLists.length; i++) {
                            MailIds.append("'" + userLists[i] + "'");

                            if (i != userLists.length - 1) {
                                MailIds.append(",");
                            }
                        }

                        Log.d(TAG, "Sending Message To: " + MailIds.toString());
                        Log.d(TAG, "Sending Message: " + message.toString());

                        sendMessage(MailIds.toString(), message.toString());


                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
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
        for(int i=0;i<scheduleArrayList.get(position).getParticipantDetails().size();i++)
        {
            holder.participants.setText(scheduleArrayList.get(position).getParticipantDetails().get(i).getParticipantName()+"\n");
        }

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
                Log.d(TAG, "Send Message Response: " + response);

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
                Log.e(TAG, "Error Sending Message GCM: " + error.getMessage());
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
}
