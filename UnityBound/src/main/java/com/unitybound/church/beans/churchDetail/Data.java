package com.unitybound.church.beans.churchDetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Data implements Parcelable {

	@SerializedName("church")
	private Church church;

	@SerializedName("memberStatus")
	private String memberStatus;

	@SerializedName("pageName")
	private String pageName;

	@SerializedName("allposts")
	private ArrayList<AllpostsItem> allposts;

	public void setChurch(Church church){
		this.church = church;
	}

	public Church getChurch(){
		return church;
	}

	public void setMemberStatus(String memberStatus){
		this.memberStatus = memberStatus;
	}

	public String getMemberStatus(){
		return memberStatus;
	}

	public void setPageName(String pageName){
		this.pageName = pageName;
	}

	public String getPageName(){
		return pageName;
	}

	public void setAllposts(ArrayList<AllpostsItem> allposts){
		this.allposts = allposts;
	}

	public ArrayList<AllpostsItem> getAllposts(){
		return allposts;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"church = '" + church + '\'' + 
			",memberStatus = '" + memberStatus + '\'' + 
			",pageName = '" + pageName + '\'' + 
			",allposts = '" + allposts + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.church, flags);
		dest.writeString(this.memberStatus);
		dest.writeString(this.pageName);
		dest.writeList(this.allposts);
	}

	public Data() {
	}

	protected Data(Parcel in) {
		this.church = in.readParcelable(Church.class.getClassLoader());
		this.memberStatus = in.readString();
		this.pageName = in.readString();
		this.allposts = new ArrayList<AllpostsItem>();
		in.readList(this.allposts, AllpostsItem.class.getClassLoader());
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