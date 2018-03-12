package com.unitybound.events.fragment.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("pageName")
	private String pageName;

	@SerializedName("events")
	private List<EventsItem> events;

	public void setPageName(String pageName){
		this.pageName = pageName;
	}

	public String getPageName(){
		return pageName;
	}

	public void setEvents(List<EventsItem> events){
		this.events = events;
	}

	public List<EventsItem> getEvents(){
		return events;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"pageName = '" + pageName + '\'' + 
			",events = '" + events + '\'' + 
			"}";
		}
}