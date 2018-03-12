package com.unitybound.main.search.beans;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Data{

	@SerializedName("peoples")
	private ArrayList<PeoplesItem> peoples;

	@SerializedName("groups")
	private ArrayList<GroupsItem> groups;

	@SerializedName("churches")
	private ArrayList<ChurchesItem> churches;

	@SerializedName("events")
	private ArrayList<EventsItem> events;

	public void setPeoples(ArrayList<PeoplesItem> peoples){
		this.peoples = peoples;
	}

	public ArrayList<PeoplesItem> getPeoples(){
		return peoples;
	}

	public void setGroups(ArrayList<GroupsItem> groups){
		this.groups = groups;
	}

	public ArrayList<GroupsItem> getGroups(){
		return groups;
	}

	public void setChurches(ArrayList<ChurchesItem> churches){
		this.churches = churches;
	}

	public ArrayList<ChurchesItem> getChurches(){
		return churches;
	}

	public void setEvents(ArrayList<EventsItem> events){
		this.events = events;
	}

	public ArrayList<EventsItem> getEvents(){
		return events;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"peoples = '" + peoples + '\'' + 
			",groups = '" + groups + '\'' + 
			",churches = '" + churches + '\'' + 
			",events = '" + events + '\'' + 
			"}";
		}
}