package com.utase1.letsmeet.Model;

import java.util.ArrayList;

/**
 * Created by akilesh on 11/20/2015.
 */
public class PendingModel {

    private String txtmeetID, Meetname, location, txtDate, txtTimefrom, txtTimeto, txtParticipants;


    public void setMeetId (String meetId) {
        this.txtmeetID = meetId;
    }

    public String getMeetId() {
        return txtmeetID;
    }

    public void setMeetname (String MeetName) {
        this.Meetname = MeetName;
    }

    public String getMeetname() {
        return Meetname;
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

    public void setTimefrom (String meetTimefrom) {
        this.txtTimefrom = meetTimefrom;
    }

    public String getTimefrom() {
        return txtTimefrom;
    }

    public void setTimeto (String meetTimeto) {
        this.txtTimeto = meetTimeto;
    }

    public String getTimeto() {
        return txtTimeto;
    }

    public void setParticipants (String meetPart) {
        this.txtParticipants = meetPart;
    }

    public String getParticipants() {
        return txtParticipants;
    }


}
