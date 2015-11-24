package com.utase1.letsmeet.dto;



import java.io.Serializable;
import java.util.ArrayList;


public class TimeParticipantMap implements Serializable {
	ArrayList<ParticipantDetails> participantDetails=new ArrayList<ParticipantDetails>();
	int timeSlot;
	int maxParticipants;
	String  meetingid;

	public String getMeetingid() {
		return meetingid;
	}

	public void setMeetingid(String meetingid) {
		this.meetingid = meetingid;
	}
	
	public ArrayList<ParticipantDetails> getParticipantDetails() {
		return participantDetails;
	}
	public void setParticipantDetails(
			ArrayList<ParticipantDetails> participantDetails) {
		this.participantDetails = participantDetails;
	}
	public int getTimeSlot() {
		return timeSlot;
	}
	public void setTimeSlot(int timeSlot) {
		this.timeSlot = timeSlot;
	}
	public TimeParticipantMap(ArrayList<ParticipantDetails> participantDetails,
			int timeSlot,int participants) {
		super();
		this.participantDetails = participantDetails;
		this.timeSlot = timeSlot;
		this.maxParticipants=participants;
	}
	
	public int getMaxParticipants() {
		return maxParticipants;
	}
	public void setMaxParticipants(int maxParticipants) {
		this.maxParticipants = maxParticipants;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "timeSlot"+timeSlot;
	}
	
	

}
