package com.utase1.letsmeet.activity;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.utase1.letsmeet.R;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.i(TAG, "Received: " + message);
        String[] meet_event_Details = message.split(";");

        final int notificationId = 1;
        PendingIntent pendingIntent = null;
        if(meet_event_Details[0].equals("Event")) {
            Intent pupInt = new Intent(this, RespondEvent.class);
            pupInt.putExtra("EventName", meet_event_Details[1]);
            pupInt.putExtra("EventDate", meet_event_Details[2]);
            pupInt.putExtra("EventTime", meet_event_Details[3]);
            pupInt.putExtra("EventLocation", meet_event_Details[4]);
            pendingIntent = PendingIntent.getActivity(this, 0,
                    pupInt, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        else if(meet_event_Details[0].equals("Meeting"))
        {
            Intent pupInt = new Intent(this, RespondMeeting.class);

            pupInt.putExtra("MeetName", meet_event_Details[1]);
            pupInt.putExtra("MeetDate", meet_event_Details[2]);
            pupInt.putExtra("MeetTimeFrom", meet_event_Details[3]);
            pupInt.putExtra("MeetTime", meet_event_Details[3]);
            pupInt.putExtra("MeetTimeTo", meet_event_Details[4]);
            pupInt.putExtra("MeetLocation", meet_event_Details[5]);

            pendingIntent = PendingIntent.getActivity(this, 0,
                    pupInt, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        else if(meet_event_Details[0].equals("FreeTimeScheduler"))
        {
            Intent pupInt = new Intent(this, AcceptDeclineMeeting.class);

            pupInt.putExtra("MeetName", meet_event_Details[1]);
            pupInt.putExtra("MeetDate", meet_event_Details[2]);
            pupInt.putExtra("MeetTime", meet_event_Details[3]);
            pupInt.putExtra("MeetTimeTo", meet_event_Details[3]+1);
            pupInt.putExtra("MeetLocation", meet_event_Details[4]);

            pendingIntent = PendingIntent.getActivity(this, 0,
                    pupInt, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Lets Meet")
                .setContentText(meet_event_Details[1])
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager nm = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        nm.notify(notificationId, mBuilder.build());
        // [END_EXCLUDE]
    }
    // [END receive_message]
}