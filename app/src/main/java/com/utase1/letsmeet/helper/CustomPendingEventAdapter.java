package com.utase1.letsmeet.helper;


/**
 * Created by akilesh on 11/21/2015.
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

import com.utase1.letsmeet.Model.AcceptedEventModel;
import com.utase1.letsmeet.Model.AcceptedModel;
import com.utase1.letsmeet.Model.PendingEventModel;
import com.utase1.letsmeet.R;
import com.utase1.letsmeet.activity.FinalEventSummary;
import com.utase1.letsmeet.activity.FinalMeetSummary;
import com.utase1.letsmeet.activity.MyMeetingsEvents;


public class CustomPendingEventAdapter extends ArrayAdapter {

    private Activity activity;
    private List<PendingEventModel> acceptedArrayList;

    private LayoutInflater inflater;

    public CustomPendingEventAdapter(Context context,int resource ,List<PendingEventModel> results) {
        super(context,resource,results);
        acceptedArrayList =results;

        inflater = LayoutInflater.from(context);

    }


    public View getView(final int position, View convertView, final ViewGroup parent) {

        final ViewHolder holder;
        notifyDataSetChanged();

        if (convertView == null){
            convertView = inflater.inflate(R.layout.pending_rows, null);
            holder = new ViewHolder();

            holder.txtEventName = (TextView) convertView.findViewById(R.id.title);
            holder.txtLocation = (TextView) convertView.findViewById(R.id.meetingLocation);
            holder.txtDate = (TextView) convertView.findViewById(R.id.meetingDate);
            holder.btnView = (Button) convertView.findViewById(R.id.btnView);
            //holder.lstSchedule = (ListView) convertView.findViewById(R.id.lst_sch_rows);

            //YOU NEED TO ADD THE BUTTON CLICK LISTNER
            holder.btnView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent joinIntent= new Intent(parent.getContext(),FinalEventSummary.class);

                    joinIntent.putExtra("event_name", holder.txtEventName.getText());
                    joinIntent.putExtra("event_location",holder.txtLocation.getText());
                    joinIntent.putExtra("event_date",holder.txtDate.getText());
                    joinIntent.putExtra("event_time",acceptedArrayList.get(position).getTime());
                    joinIntent.putExtra("event_participants",acceptedArrayList.get(position).getParticipants());
                    parent.getContext().startActivity(joinIntent);

                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtEventName.setText(acceptedArrayList.get(position).getEventname());
        holder.txtLocation.setText(acceptedArrayList.get(position).getLocation());
        holder.txtDate.setText(acceptedArrayList.get(position).getDate());



        return convertView;
    }

    static class ViewHolder {

        TextView txtEventName;
        TextView txtLocation;
        TextView txtDate;

        Button btnView;
    }

}


