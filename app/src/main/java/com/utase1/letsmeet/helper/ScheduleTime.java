package com.utase1.letsmeet.helper;

import com.utase1.letsmeet.dto.ParticipantDetails;
import com.utase1.letsmeet.dto.TimeParticipantMap;

import java.util.ArrayList;
import java.util.Collections;



public class ScheduleTime {

	ArrayList<TimeParticipantMap> timeParticipantMapsNew=null;

	public ArrayList<TimeParticipantMap> getFreeTime(ArrayList<com.utase1.letsmeet.dto.ParticipantDetails> participantDetails,String meet_id) {
		// TODO Auto-generated method stub
		/*ArrayList<ParticipantDetails> participantDetails = new ArrayList<ParticipantDetails>();
		participantDetails = createData(participantDetails);*/
		if (participantDetails != null && participantDetails.size() > 0) {
			int participantListSize = participantDetails.size();
			// System.out.println("size"+participantListSize);
			// int[participantListSize][24] time=new
			// int[participantListSize][24];
			int[][] time = new int[participantListSize][24];
			for (int i = 0; i < participantListSize; i++) {
				for (int j = participantDetails.get(i).getStartTime() - 1; j < participantDetails
						.get(i).getEndTime() - 1; j++) {

					time[i][j] = 1;
				}

			}
			int max = 0;
			ArrayList<Integer> finalFreeCommonTime = new ArrayList<Integer>();
			TimeParticipantMap timeParticipantMap = null;
			ArrayList<TimeParticipantMap> timeParticipantMaps = new ArrayList<TimeParticipantMap>();

			for (int j = 0; j < 24; j++) {
				int countFreeTime = 0;
				ArrayList<ParticipantDetails> partListForTime = new ArrayList<ParticipantDetails>();
				for (int i = 0; i < participantListSize; i++) {
					// timeParticipantMap=getNumberOfParticipantForParticularTime(i,j,participantDetails,time);

					if (time[i][j] == 1) {
						partListForTime.add(participantDetails.get(i));
						countFreeTime++;

					}

				}
				if(countFreeTime>=1)
				{
					timeParticipantMap = new TimeParticipantMap(
							partListForTime,j+1,countFreeTime);
					finalFreeCommonTime.add(countFreeTime);
					timeParticipantMap.setMeetingid(meet_id);
					timeParticipantMaps.add(timeParticipantMap);

				}


			}

			int maximum = Collections.max(finalFreeCommonTime);
			System.out.println(maximum);
			 timeParticipantMapsNew = new ArrayList<TimeParticipantMap>();
			for (int i = 0; i < timeParticipantMaps.size(); i++) {

				if (timeParticipantMaps.get(i).getMaxParticipants() == maximum) {
					timeParticipantMapsNew.add(timeParticipantMaps.get(i));
				}
			}
			System.out.println("final size" + timeParticipantMapsNew);

		}
		return timeParticipantMapsNew;
	}

	

	private static ArrayList<ParticipantDetails> createData(
			ArrayList<ParticipantDetails> partList) {

		ParticipantDetails participantDetails = new ParticipantDetails();
		participantDetails.setEndTime(4);
		participantDetails.setStartTime(1);
		participantDetails.setParticipantName("Ashay 2");
		partList.add(participantDetails);
		ParticipantDetails participantDetails1 = new ParticipantDetails();
		participantDetails1.setEndTime(6);
		participantDetails1.setStartTime(3);
		participantDetails1.setParticipantName("Ashay 3");
		partList.add(participantDetails1);
		ParticipantDetails participantDetails2 = new ParticipantDetails();
		participantDetails2.setEndTime(6);
		participantDetails2.setStartTime(5);
		participantDetails2.setParticipantName("Ashay 4");
		partList.add(participantDetails2);
		return partList;
	}

}
