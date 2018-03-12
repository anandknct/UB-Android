package com.unitybound.settings.fragment.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class NotificationSetting implements Parcelable {

	@SerializedName("Church")
	private Church church;

	@SerializedName("Events")
	private Events events;

	@SerializedName("Friends")
	private Friends friends;

	@SerializedName("Groups")
	private Groups groups;

	public void setChurch(Church church){
		this.church = church;
	}

	public Church getChurch(){
		return church;
	}

	public void setEvents(Events events){
		this.events = events;
	}

	public Events getEvents(){
		return events;
	}

	public void setFriends(Friends friends){
		this.friends = friends;
	}

	public Friends getFriends(){
		return friends;
	}

	public void setGroups(Groups groups){
		this.groups = groups;
	}

	public Groups getGroups(){
		return groups;
	}

	@Override
 	public String toString(){
		return 
			"NotificationSetting{" + 
			"church = '" + church + '\'' + 
			",events = '" + events + '\'' + 
			",friends = '" + friends + '\'' + 
			",groups = '" + groups + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.church, flags);
		dest.writeParcelable(this.events, flags);
		dest.writeParcelable(this.friends, flags);
		dest.writeParcelable(this.groups, flags);
	}

	public NotificationSetting() {
	}

	protected NotificationSetting(Parcel in) {
		this.church = in.readParcelable(Church.class.getClassLoader());
		this.events = in.readParcelable(Events.class.getClassLoader());
		this.friends = in.readParcelable(Friends.class.getClassLoader());
		this.groups = in.readParcelable(Groups.class.getClassLoader());
	}

	public static final Parcelable.Creator<NotificationSetting> CREATOR = new Parcelable.Creator<NotificationSetting>() {
		@Override
		public NotificationSetting createFromParcel(Parcel source) {
			return new NotificationSetting(source);
		}

		@Override
		public NotificationSetting[] newArray(int size) {
			return new NotificationSetting[size];
		}
	};
}