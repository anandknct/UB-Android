package com.unitybound.events.fragment.beans.eventDetailAbout;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable {

	@SerializedName("isEventMemberStatus")
	private boolean isEventMemberStatus;

	@SerializedName("eventPage")
	private String eventPage;

	@SerializedName("event")
	private Event event;

	public void setIsEventMemberStatus(boolean isEventMemberStatus){
		this.isEventMemberStatus = isEventMemberStatus;
	}

	public boolean isIsEventMemberStatus(){
		return isEventMemberStatus;
	}

	public void setEventPage(String eventPage){
		this.eventPage = eventPage;
	}

	public String getEventPage(){
		return eventPage;
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
			",eventPage = '" + eventPage + '\'' + 
			",event = '" + event + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte(this.isEventMemberStatus ? (byte) 1 : (byte) 0);
		dest.writeString(this.eventPage);
		dest.writeParcelable(this.event, flags);
	}

	public Data() {
	}

	protected Data(Parcel in) {
		this.isEventMemberStatus = in.readByte() != 0;
		this.eventPage = in.readString();
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