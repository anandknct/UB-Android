package com.unitybound.groups.beans.groupMembers;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Data implements Parcelable {

	@SerializedName("groupUserDetails")
	private List<GroupUserDetailsItem> groupUserDetails;

	@SerializedName("pageName")
	private String pageName;

	@SerializedName("group")
	private Group group;

	public void setGroupUserDetails(List<GroupUserDetailsItem> groupUserDetails){
		this.groupUserDetails = groupUserDetails;
	}

	public List<GroupUserDetailsItem> getGroupUserDetails(){
		return groupUserDetails;
	}

	public void setPageName(String pageName){
		this.pageName = pageName;
	}

	public String getPageName(){
		return pageName;
	}

	public void setGroup(Group group){
		this.group = group;
	}

	public Group getGroup(){
		return group;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"groupUserDetails = '" + groupUserDetails + '\'' + 
			",pageName = '" + pageName + '\'' + 
			",group = '" + group + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(this.groupUserDetails);
		dest.writeString(this.pageName);
		dest.writeParcelable(this.group, flags);
	}

	public Data() {
	}

	protected Data(Parcel in) {
		this.groupUserDetails = new ArrayList<GroupUserDetailsItem>();
		in.readList(this.groupUserDetails, GroupUserDetailsItem.class.getClassLoader());
		this.pageName = in.readString();
		this.group = in.readParcelable(Group.class.getClassLoader());
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