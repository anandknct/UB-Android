package com.unitybound.events.fragment.beans.eventDetailParticipants;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class EventDetailParticipantsResponse implements Parcelable {

	@SerializedName("statuscode")
	private String statuscode;

	@SerializedName("msg")
	private String msg;

	@SerializedName("data")
	private Data data;

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

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
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
			"EventDetailParticipantsResponse{" + 
			"statuscode = '" + statuscode + '\'' + 
			",msg = '" + msg + '\'' + 
			",data = '" + data + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.statuscode);
		dest.writeString(this.msg);
		dest.writeParcelable(this.data, flags);
		dest.writeString(this.status);
	}

	public EventDetailParticipantsResponse() {
	}

	protected EventDetailParticipantsResponse(Parcel in) {
		this.statuscode = in.readString();
		this.msg = in.readString();
		this.data = in.readParcelable(Data.class.getClassLoader());
		this.status = in.readString();
	}

	public static final Parcelable.Creator<EventDetailParticipantsResponse> CREATOR = new Parcelable.Creator<EventDetailParticipantsResponse>() {
		@Override
		public EventDetailParticipantsResponse createFromParcel(Parcel source) {
			return new EventDetailParticipantsResponse(source);
		}

		@Override
		public EventDetailParticipantsResponse[] newArray(int size) {
			return new EventDetailParticipantsResponse[size];
		}
	};
}