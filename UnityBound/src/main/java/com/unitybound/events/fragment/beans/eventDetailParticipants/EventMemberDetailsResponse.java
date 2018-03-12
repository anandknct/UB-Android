package com.unitybound.events.fragment.beans.eventDetailParticipants;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class EventMemberDetailsResponse implements Parcelable {

	@SerializedName("firstName")
	private String firstName;

	@SerializedName("lastName")
	private String lastName;

	@SerializedName("id")
	private String id;

	@SerializedName("profileImage")
	private String profileImage;

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setProfileImage(String profileImage){
		this.profileImage = profileImage;
	}

	public String getProfileImage(){
		return profileImage;
	}

	@Override
 	public String toString(){
		return 
			"EventMemberDetailsResponse{" + 
			"firstName = '" + firstName + '\'' + 
			",lastName = '" + lastName + '\'' + 
			",id = '" + id + '\'' + 
			",profileImage = '" + profileImage + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.firstName);
		dest.writeString(this.lastName);
		dest.writeString(this.id);
		dest.writeString(this.profileImage);
	}

	public EventMemberDetailsResponse() {
	}

	protected EventMemberDetailsResponse(Parcel in) {
		this.firstName = in.readString();
		this.lastName = in.readString();
		this.id = in.readString();
		this.profileImage = in.readString();
	}

	public static final Parcelable.Creator<EventMemberDetailsResponse> CREATOR = new Parcelable.Creator<EventMemberDetailsResponse>() {
		@Override
		public EventMemberDetailsResponse createFromParcel(Parcel source) {
			return new EventMemberDetailsResponse(source);
		}

		@Override
		public EventMemberDetailsResponse[] newArray(int size) {
			return new EventMemberDetailsResponse[size];
		}
	};
}