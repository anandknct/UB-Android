package com.unitybound.events.fragment.beans.eventDetailParticipants;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Data implements Parcelable {

	@SerializedName("isEventMemberStatus")
	private String isEventMemberStatus;

	@SerializedName("eventDeclinedMemberDetails")
	private List<Object> eventDeclinedMemberDetails;

	@SerializedName("eventInvitedMemberDetails")
	private ArrayList<EventInvitedMemberDetailsItem> eventInvitedMemberDetails;

	@SerializedName("eventPage")
	private String eventPage;

	@SerializedName("eventMemberDetails")
	private ArrayList<EventMemberDetailsResponse> eventMemberDetails;

	@SerializedName("eventInterestedMemberDetails")
	private List<Object> eventInterestedMemberDetails;

	@SerializedName("event")
	private Event event;

	public void setIsEventMemberStatus(String isEventMemberStatus){
		this.isEventMemberStatus = isEventMemberStatus;
	}

	public String getIsEventMemberStatus(){
		return isEventMemberStatus;
	}

	public void setEventDeclinedMemberDetails(List<Object> eventDeclinedMemberDetails){
		this.eventDeclinedMemberDetails = eventDeclinedMemberDetails;
	}

	public List<Object> getEventDeclinedMemberDetails(){
		return eventDeclinedMemberDetails;
	}

	public void setEventInvitedMemberDetails(ArrayList<EventInvitedMemberDetailsItem> eventInvitedMemberDetails){
		this.eventInvitedMemberDetails = eventInvitedMemberDetails;
	}

	public ArrayList<EventInvitedMemberDetailsItem> getEventInvitedMemberDetails(){
		return eventInvitedMemberDetails;
	}

	public void setEventPage(String eventPage){
		this.eventPage = eventPage;
	}

	public String getEventPage(){
		return eventPage;
	}

	public void setEventMemberDetails(ArrayList<EventMemberDetailsResponse> eventMemberDetails){
		this.eventMemberDetails = eventMemberDetails;
	}

	public ArrayList<EventMemberDetailsResponse> getEventMemberDetails(){
		return eventMemberDetails;
	}

	public void setEventInterestedMemberDetails(List<Object> eventInterestedMemberDetails){
		this.eventInterestedMemberDetails = eventInterestedMemberDetails;
	}

	public List<Object> getEventInterestedMemberDetails(){
		return eventInterestedMemberDetails;
	}

	public void setEvent(Event event){
		this.event = event;
	}

	public Event getEvent(){
		return event;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"isEventMemberStatus = '" + isEventMemberStatus + '\'' + 
			",eventDeclinedMemberDetails = '" + eventDeclinedMemberDetails + '\'' + 
			",eventInvitedMemberDetails = '" + eventInvitedMemberDetails + '\'' + 
			",eventPage = '" + eventPage + '\'' + 
			",eventMemberDetails = '" + eventMemberDetails + '\'' + 
			",eventInterestedMemberDetails = '" + eventInterestedMemberDetails + '\'' + 
			",event = '" + event + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.isEventMemberStatus);
		dest.writeList(this.eventDeclinedMemberDetails);
		dest.writeList(this.eventInvitedMemberDetails);
		dest.writeString(this.eventPage);
		dest.writeList(this.eventMemberDetails);
		dest.writeList(this.eventInterestedMemberDetails);
		dest.writeParcelable(this.event, flags);
	}

	public Data() {
	}

	protected Data(Parcel in) {
		this.isEventMemberStatus = in.readString();
		this.eventDeclinedMemberDetails = new ArrayList<Object>();
		in.readList(this.eventDeclinedMemberDetails, Object.class.getClassLoader());
		this.eventInvitedMemberDetails = new ArrayList<EventInvitedMemberDetailsItem>();
		in.readList(this.eventInvitedMemberDetails, EventInvitedMemberDetailsItem.class.getClassLoader());
		this.eventPage = in.readString();
		this.eventMemberDetails = new ArrayList<EventMemberDetailsResponse>();
		in.readList(this.eventMemberDetails, Object.class.getClassLoader());
		this.eventInterestedMemberDetails = new ArrayList<Object>();
		in.readList(this.eventInterestedMemberDetails, Object.class.getClassLoader());
		this.event = in.readParcelable(Event.class.getClassLoader());
	}

	public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
		@Override
		public Data createFromParcel(Parcel source) {
			return new Data(source);
		}

		@Override
		public Data[] newArray(int size) {
			return new Data[size];
		}
	};
}