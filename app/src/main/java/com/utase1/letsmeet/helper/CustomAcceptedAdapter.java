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

import com.utase1.letsmeet.Model.AcceptedModel;
import com.utase1.letsmeet.R;
import com.utase1.letsmeet.activity.FinalMeetSummary;
import com.utase1.letsmeet.activity.MyMeetingsEvents;


public class CustomAcceptedAdapter extends ArrayAdapter {

    private Activity activity;
    private List<AcceptedModel> acceptedArrayList;

    private LayoutInflater inflater;

    public CustomAcceptedAdapter(Context context,int resource ,List<AcceptedModel> results) {
        super(context,resource,results);
        acceptedArrayList =results;

        inflater = LayoutInflater.from(context);

    }


    public View getView(final int position, View convertView, final ViewGroup parent) {

        final ViewHolder holder;
        notifyDataSetChanged();

        if (convertView == null){
            convertView = inflater.inflate(R.layout.accepted_rows, null);
            holder = new ViewHolder();

            holder.txtMeetName = (TextView) convertView.findViewById(R.id.title);
            holder.txtLocation = (TextView) convertView.findViewById(R.id.meetingLocation);
            holder.txtDate = (TextView) convertView.findViewById(R.id.meetingDate);
            holder.btnView = (Button) convertView.findViewById(R.id.btnView);
            //holder.lstSchedule = (ListView) convertView.findViewById(R.id.lst_sch_rows);

            //YOU NEED TO ADD THE BUTTON CLICK LISTNER
            holder.btnView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent joinIntent= new Intent(parent.getContext(),FinalMeetSummary.class);

                    joinIntent.putExtra("meet_name", holder.txtMeetName.getText());
                    joinIntent.putExtra("meet_location",holder.txtLocation.getText());
                    joinIntent.putExtra("meet_date",holder.txtDate.getText());
                    joinIntent.putExtra("time_from",acceptedArrayList.get(position).getTimefrom());
                    joinIntent.putExtra("time_to",acceptedArrayList.get(position).getTimeto());
                    joinIntent.putExtra("participants",acceptedArrayList.get(position).getParticipants());
                    parent.getContext().startActivity(joinIntent);

                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtMeetName.setText(acceptedArrayList.get(position).getMeetname());
        holder.txtLocation.setText(acceptedArrayList.get(position).getLocation());
        holder.txtDate.setText(acceptedArrayList.get(position).getDate());



        return convertView;
    }

    static class ViewHolder {

        TextView txtMeetName;
        TextView txtLocation;
        TextView txtDate;

        Button btnView;
    }

}


