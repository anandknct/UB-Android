package com.unitybound.events.fragment.beans.eventDiscussion;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Event implements Parcelable {

	@SerializedName("eventEndDate")
	private String eventEndDate;

	@SerializedName("eventScope")
	private String eventScope;

	@SerializedName("eventLocation")
	private String eventLocation;

	@SerializedName("eventCountry")
	private String eventCountry;

	@SerializedName("event_image")
	private String eventImage;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("eventCity")
	private String eventCity;

	@SerializedName("eventZip")
	private String eventZip;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("eventState")
	private String eventState;

	@SerializedName("added_by")
	private String addedBy;

	@SerializedName("eventDescription")
	private String eventDescription;

	@SerializedName("eventEndTime")
	private String eventEndTime;

	@SerializedName("eventName")
	private String eventName;

	@SerializedName("eventStartTime")
	private String eventStartTime;

	@SerializedName("_id")
	private String id;

	@SerializedName("eventStartDate")
	private String eventStartDate;

	public void setEventEndDate(String eventEndDate){
		this.eventEndDate = eventEndDate;
	}

	public String getEventEndDate(){
		return eventEndDate;
	}

	public void setEventScope(String eventScope){
		this.eventScope = eventScope;
	}

	public String getEventScope(){
		return eventScope;
	}

	public void setEventLocation(String eventLocation){
		this.eventLocation = eventLocation;
	}

	public String getEventLocation(){
		return eventLocation;
	}

	public void setEventCountry(String eventCountry){
		this.eventCountry = eventCountry;
	}

	public String getEventCountry(){
		return eventCountry;
	}

	public void setEventImage(String eventImage){
		this.eventImage = eventImage;
	}

	public String getEventImage(){
		return eventImage;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setEventCity(String eventCity){
		this.eventCity = eventCity;
	}

	public String getEventCity(){
		return eventCity;
	}

	public void setEventZip(String eventZip){
		this.eventZip = eventZip;
	}

	public String getEventZip(){
		return eventZip;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setEventState(String eventState){
		this.eventState = eventState;
	}

	public String getEventState(){
		return eventState;
	}

	public void setAddedBy(String addedBy){
		this.addedBy = addedBy;
	}

	public String getAddedBy(){
		return addedBy;
	}

	public void setEventDescription(String eventDescription){
		this.eventDescription = eventDescription;
	}

	public String getEventDescription(){
		return eventDescription;
	}

	public void setEventEndTime(String eventEndTime){
		this.eventEndTime = eventEndTime;
	}

	public String getEventEndTime(){
		return eventEndTime;
	}

	public void setEventName(String eventName){
		this.eventName = eventName;
	}

	public String getEventName(){
		return eventName;
	}

	public void setEventStartTime(String eventStartTime){
		this.eventStartTime = eventStartTime;
	}

	public String getEventStartTime(){
		return eventStartTime;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setEventStartDate(String eventStartDate){
		this.eventStartDate = eventStartDate;
	}

	public String getEventStartDate(){
		return eventStartDate;
	}

	@Override
 	public String toString(){
		return 
			"Event{" + 
			"eventEndDate = '" + eventEndDate + '\'' + 
			",eventScope = '" + eventScope + '\'' + 
			",eventLocation = '" + eventLocation + '\'' + 
			",eventCountry = '" + eventCountry + '\'' + 
			",event_image = '" + eventImage + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",eventCity = '" + eventCity + '\'' + 
			",eventZip = '" + eventZip + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",eventState = '" + eventState + '\'' + 
			",added_by = '" + addedBy + '\'' + 
			",eventDescription = '" + eventDescription + '\'' + 
			",eventEndTime = '" + eventEndTime + '\'' + 
			",eventName = '" + eventName + '\'' + 
			",eventStartTime = '" + eventStartTime + '\'' + 
			",_id = '" + id + '\'' + 
			",eventStartDate = '" + eventStartDate + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.eventEndDate);
		dest.writeString(this.eventScope);
		dest.writeString(this.eventLocation);
		dest.writeString(this.eventCountry);
		dest.writeString(this.eventImage);
		dest.writeString(this.createdAt);
		dest.writeString(this.eventCity);
		dest.writeString(this.eventZip);
		dest.writeString(this.updatedAt);
		dest.writeString(this.eventState);
		dest.writeString(this.addedBy);
		dest.writeString(this.eventDescription);
		dest.writeString(this.eventEndTime);
		dest.writeString(this.eventName);
		dest.writeString(this.eventStartTime);
		dest.writeString(this.id);
		dest.writeString(this.eventStartDate);
	}

	public Event() {
	}

	protected Event(Parcel in) {
		this.eventEndDate = in.readString();
		this.eventScope = in.readString();
		this.eventLocation = in.readString();
		this.eventCountry = in.readString();
		this.eventImage = in.readString();
		this.createdAt = in.readString();
		this.eventCity = in.readString();
		this.eventZip = in.readString();
		this.updatedAt = in.readString();
		this.eventState = in.readString();
		this.addedBy = in.readString();
		this.eventDescription = in.readString();
		this.eventEndTime = in.readString();
		this.eventName = in.readString();
		this.eventStartTime = in.readString();
		this.id = in.readString();
		this.eventStartDate = in.readString();
	}

	public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
		@Override
		public Event createFromParcel(Parcel source) {
			return new Event(source);
		}

		@Override
		public Event[] newArray(int size) {
			return new Event[size];
		}
	};
}