package com.utase1.letsmeet.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.utase1.letsmeet.Model.scheduleModel;
import com.utase1.letsmeet.R;
import com.utase1.letsmeet.dto.TimeParticipantMap;

import java.util.List;

/**
 * Created by Ashay Rajimwale on 11/14/2015.
 */
public class FreeTimeSchedulerInitiator  extends ArrayAdapter {
    private Activity activity;
    private List<TimeParticipantMap> scheduleArrayList;
    String meetingName;
    String meetingDate;
    String meetingLocation;

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




    static class ViewHolder {

        TextView txtMeetName;
        TextView txtLocation;
        TextView txtDate;
        TextView txtTime;
        TextView participants;
        ListView lstSchedule;

        Button btnSch;


    }}
