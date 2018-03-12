package com.unitybound.settings.fragment.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data implements Parcelable {

	@SerializedName("userInfo")
	public UserInfo userInfo;

	@SerializedName("countries")
	public List<String> countries;

	@SerializedName("notificationSetting")
	public NotificationSetting notificationSetting;

	public void setUserInfo(UserInfo userInfo){
		this.userInfo = userInfo;
	}

	public UserInfo getUserInfo(){
		return userInfo;
	}

	public void setCountries(List<String> countries){
		this.countries = countries;
	}

	public List<String> getCountries(){
		return countries;
	}

	public void setNotificationSetting(NotificationSetting notificationSetting){
		this.notificationSetting = notificationSetting;
	}

	public NotificationSetting getNotificationSetting(){
		return notificationSetting;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"userInfo = '" + userInfo + '\'' + 
			",countries = '" + countries + '\'' + 
			",notificationSetting = '" + notificationSetting + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.userInfo, flags);
		dest.writeStringList(this.countries);
		dest.writeParcelable(this.notificationSetting, flags);
	}

	public Data() {
	}

	protected Data(Parcel in) {
		this.userInfo = in.readParcelable(UserInfo.class.getClassLoader());
		this.countries = in.createStringArrayList();
		this.notificationSetting = in.readParcelable(NotificationSetting.class.getClassLoader());
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