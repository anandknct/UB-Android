package com.unitybound.events.fragment.beans.eventDiscussion;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Data implements Parcelable {

	@SerializedName("isEventMemberStatus")
	private String isEventMemberStatus;

	@SerializedName("eventPage")
	private String eventPage;

	@SerializedName("event")
	private Event event;

	@SerializedName("allposts")
	private ArrayList<AllpostsItem> allposts;

	public void setIsEventMemberStatus(String isEventMemberStatus){
		this.isEventMemberStatus = isEventMemberStatus;
	}

	public String getIsEventMemberStatus(){
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

	public void setAllposts(ArrayList<AllpostsItem> allposts){
		this.allposts = allposts;
	}

	public ArrayList<AllpostsItem> getAllposts(){
		return allposts;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"isEventMemberStatus = '" + isEventMemberStatus + '\'' + 
			",eventPage = '" + eventPage + '\'' + 
			",event = '" + event + '\'' + 
			",allposts = '" + allposts + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.isEventMemberStatus);
		dest.writeString(this.eventPage);
		dest.writeParcelable(this.event, flags);
		dest.writeTypedList(this.allposts);
	}

	public Data() {
	}

	protected Data(Parcel in) {
		this.isEventMemberStatus = in.readString();
		this.eventPage = in.readString();
		this.event = in.readParcelable(Event.class.getClassLoader());
		this.allposts = in.createTypedArrayList(AllpostsItem.CREATOR);
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