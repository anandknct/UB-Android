package com.unitybound.church.beans.blockedUsers;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Data implements Parcelable {

	@SerializedName("church")
	private Church church;

	@SerializedName("churchUserDetails")
	private ArrayList<ChurchUserDetailsItem> churchUserDetails;

	@SerializedName("pageName")
	private String pageName;

	public void setChurch(Church church){
		this.church = church;
	}

	public Church getChurch(){
		return church;
	}

	public void setChurchUserDetails(ArrayList<ChurchUserDetailsItem> churchUserDetails){
		this.churchUserDetails = churchUserDetails;
	}

	public ArrayList<ChurchUserDetailsItem> getChurchUserDetails(){
		return churchUserDetails;
	}

	public void setPageName(String pageName){
		this.pageName = pageName;
	}

	public String getPageName(){
		return pageName;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"church = '" + church + '\'' + 
			",churchUserDetails = '" + churchUserDetails + '\'' + 
			",pageName = '" + pageName + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.church, flags);
		dest.writeList(this.churchUserDetails);
		dest.writeString(this.pageName);
	}

	public Data() {
	}

	protected Data(Parcel in) {
		this.church = in.readParcelable(Church.class.getClassLoader());
		this.churchUserDetails = new ArrayList<ChurchUserDetailsItem>();
		in.readList(this.churchUserDetails, ChurchUserDetailsItem.class.getClassLoader());
		this.pageName = in.readString();
	}

	public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
		@Override
		public Data createFromParcel(Parcel source) {
			return new Data(source);
		}

		@Override
		public Data[] newArray(int size) {
			return new Data[size];
		}
	};
}