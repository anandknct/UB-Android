package com.unitybound.events.fragment.beans.updateEvent;

import com.google.gson.annotations.SerializedName;

public class UpdateEventResponse{

	@SerializedName("statuscode")
	private String statuscode;

	@SerializedName("msg")
	private String msg;

	@SerializedName("event_id")
	private String eventId;

	@SerializedName("status")
	private String status;

	public void setStatuscode(String statuscode){
		this.statuscode = statuscode;
	}

	public String getStatuscode(){
		return statuscode;
	}

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setEventId(String eventId){
		this.eventId = eventId;
	}

	public String getEventId(){
		return eventId;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"UpdateEventResponse{" + 
			"statuscode = '" + statuscode + '\'' + 
			",msg = '" + msg + '\'' + 
			",event_id = '" + eventId + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}