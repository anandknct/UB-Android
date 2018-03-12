package com.unitybound.main.home.fragment.beans.addCommentsList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class AddCommentsResponse implements Parcelable {

	@SerializedName("statuscode")
	private String statuscode;

	@SerializedName("msg")
	private String msg;

	@SerializedName("data")
	private Data data;

	@SerializedName("count")
	private int count;

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

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
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
			"AddCommentsResponse{" + 
			"statuscode = '" + statuscode + '\'' + 
			",msg = '" + msg + '\'' + 
			",data = '" + data + '\'' + 
			",count = '" + count + '\'' + 
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
		dest.writeInt(this.count);
		dest.writeString(this.status);
	}

	public AddCommentsResponse() {
	}

	protected AddCommentsResponse(Parcel in) {
		this.statuscode = in.readString();
		this.msg = in.readString();
		this.data = in.readParcelable(Data.class.getClassLoader());
		this.count = in.readInt();
		this.status = in.readString();
	}

	public static final Parcelable.Creator<AddCommentsResponse> CREATOR = new Parcelable.Creator<AddCommentsResponse>() {
		@Override
		public AddCommentsResponse createFromParcel(Parcel source) {
			return new AddCommentsResponse(source);
		}

		@Override
		public AddCommentsResponse[] newArray(int size) {
			return new AddCommentsResponse[size];
		}
	};
}