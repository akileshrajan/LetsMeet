package com.utase1.letsmeet.helper;


/**
 * Created by akilesh on 10/21/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import com.utase1.letsmeet.activity.MyMeetingsEvents;
import com.utase1.letsmeet.Model.scheduleModel;
import com.utase1.letsmeet.R;

public class CustomScheduleAdapter extends ArrayAdapter {

    private Activity activity;
    private List<scheduleModel> scheduleArrayList;

    private LayoutInflater inflater;

    public CustomScheduleAdapter(Context context,int resource ,List<scheduleModel> results) {
        super(context,resource,results);
        scheduleArrayList =results;

        inflater = LayoutInflater.from(context);

    }


    public View getView(final int position, View convertView, final ViewGroup parent) {

        final ViewHolder holder;
        notifyDataSetChanged();

        if (convertView == null){
            convertView = inflater.inflate(R.layout.myschedule_rows, null);
            holder = new ViewHolder();

            holder.txtMeetName = (TextView) convertView.findViewById(R.id.title);
            holder.txtLocation = (TextView) convertView.findViewById(R.id.meetingLocation);
            holder.txtDate = (TextView) convertView.findViewById(R.id.meetingDate);
            holder.btnSch = (Button) convertView.findViewById(R.id.btnSch);
            //holder.lstSchedule = (ListView) convertView.findViewById(R.id.lst_sch_rows);

            //YOU NEED TO ADD THE BUTTON CLICK LISTNER
            holder.btnSch.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent joinIntent= new Intent(parent.getContext(),MyMeetingsEvents.class);
                    joinIntent.putExtra("meet_id",scheduleArrayList.get(position).getMeetId());
                    joinIntent.putExtra("meet_name", holder.txtMeetName.getText());
                    joinIntent.putExtra("meet_location",holder.txtLocation.getText());
                    joinIntent.putExtra("meet_date",holder.txtDate.getText());
                    joinIntent.putExtra("time_from",scheduleArrayList.get(position).getTimefrom());
                    joinIntent.putExtra("time_to",scheduleArrayList.get(position).getTimeto());
                    joinIntent.putExtra("participants",scheduleArrayList.get(position).getParticipants());
                    parent.getContext().startActivity(joinIntent);

                }
            });

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtMeetName.setText(scheduleArrayList.get(position).getMeetname());
        holder.txtLocation.setText(scheduleArrayList.get(position).getLocation());
        holder.txtDate.setText(scheduleArrayList.get(position).getDate());



        return convertView;
    }

    static class ViewHolder {

        TextView txtMeetName;
        TextView txtLocation;
        TextView txtDate;
        ListView lstSchedule;

        Button btnSch;


    }
    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

}


