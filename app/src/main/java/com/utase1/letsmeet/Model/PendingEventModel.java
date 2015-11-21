package com.utase1.letsmeet.Model;

import java.util.ArrayList;

/**
 * Created by akilesh on 11/20/2015.
 */
public class PendingEventModel {

    private String Eventname, location, txtDate, txtTime, txtParticipants;




    public void setEventname (String EventName) {
        this.Eventname = EventName;
    }

    public String getEventname() {
        return Eventname;
    }

    public void setLocation (String meetLocation) {
        this.location = meetLocation;
    }

    public String getLocation() {
        return location;
    }

    public void setDate (String meetDate) {
        this.txtDate = meetDate;
    }

    public String getDate() {
        return txtDate;
    }

    public void setTime (String meetTimefrom) {
        this.txtTime = meetTimefrom;
    }

    public String getTime() {
        return txtTime;
    }

    public void setParticipants (String meetPart) {
        this.txtParticipants = meetPart;
    }

    public String getParticipants() {
        return txtParticipants;
    }


}
