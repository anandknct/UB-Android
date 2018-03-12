package com.unitybound.main.search.beans;

import com.google.gson.annotations.SerializedName;

public class EventsItem{

	@SerializedName("event_image_m")
	private String eventImageM;

	@SerializedName("eventEndDate")
	private String eventEndDate;

	@SerializedName("eventScope")
	private String eventScope;

	@SerializedName("eventLocation")
	private String eventLocation;

	@SerializedName("eventCountry")
	private String eventCountry;

	@SerializedName("event_image_l")
	private String eventImageL;

	@SerializedName("event_image")
	private String eventImage;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("eventStartTimeStamp")
	private int eventStartTimeStamp;

	@SerializedName("event_image_s")
	private String eventImageS;

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

	@SerializedName("event_image_path")
	private String eventImagePath;

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

	public void setEventImageM(String eventImageM){
		this.eventImageM = eventImageM;
	}

	public String getEventImageM(){
		return eventImageM;
	}

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

	public void setEventImageL(String eventImageL){
		this.eventImageL = eventImageL;
	}

	public String getEventImageL(){
		return eventImageL;
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

	public void setEventStartTimeStamp(int eventStartTimeStamp){
		this.eventStartTimeStamp = eventStartTimeStamp;
	}

	public int getEventStartTimeStamp(){
		return eventStartTimeStamp;
	}

	public void setEventImageS(String eventImageS){
		this.eventImageS = eventImageS;
	}

	public String getEventImageS(){
		return eventImageS;
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

	public void setEventImagePath(String eventImagePath){
		this.eventImagePath = eventImagePath;
	}

	public String getEventImagePath(){
		return eventImagePath;
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
			"EventsItem{" + 
			"event_image_m = '" + eventImageM + '\'' + 
			",eventEndDate = '" + eventEndDate + '\'' + 
			",eventScope = '" + eventScope + '\'' + 
			",eventLocation = '" + eventLocation + '\'' + 
			",eventCountry = '" + eventCountry + '\'' + 
			",event_image_l = '" + eventImageL + '\'' + 
			",event_image = '" + eventImage + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",eventStartTimeStamp = '" + eventStartTimeStamp + '\'' + 
			",event_image_s = '" + eventImageS + '\'' + 
			",eventCity = '" + eventCity + '\'' + 
			",eventZip = '" + eventZip + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",eventState = '" + eventState + '\'' + 
			",added_by = '" + addedBy + '\'' + 
			",event_image_path = '" + eventImagePath + '\'' + 
			",eventDescription = '" + eventDescription + '\'' + 
			",eventEndTime = '" + eventEndTime + '\'' + 
			",eventName = '" + eventName + '\'' + 
			",eventStartTime = '" + eventStartTime + '\'' + 
			",_id = '" + id + '\'' + 
			",eventStartDate = '" + eventStartDate + '\'' + 
			"}";
		}
}