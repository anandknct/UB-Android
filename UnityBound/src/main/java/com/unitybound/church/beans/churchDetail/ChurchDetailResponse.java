package com.unitybound.church.beans.churchDetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ChurchDetailResponse implements Parcelable {

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
			"ChurchDetailResponse{" + 
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

	public ChurchDetailResponse() {
	}

	protected ChurchDetailResponse(Parcel in) {
		this.statuscode = in.readString();
		this.msg = in.readString();
		this.data = in.readParcelable(Data.class.getClassLoader());
		this.status = in.readString();
	}

	public static final Parcelable.Creator<ChurchDetailResponse> CREATOR = new Parcelable.Creator<ChurchDetailResponse>() {
		@Override
		public ChurchDetailResponse createFromParcel(Parcel source) {
			return new ChurchDetailResponse(source);
		}

		@Override
		public ChurchDetailResponse[] newArray(int size) {
			return new ChurchDetailResponse[size];
		}
	};
}