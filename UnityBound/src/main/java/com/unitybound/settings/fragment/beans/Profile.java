package com.unitybound.settings.fragment.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Profile implements Parcelable {

	@SerializedName("country")
	private String country;

	@SerializedName("phoneType")
	private String phoneType;

	@SerializedName("address")
	private String address;

	@SerializedName("homephone")
	private String homephone;

	@SerializedName("address2")
	private String address2;

	@SerializedName("city")
	private String city;

	@SerializedName("mobileType")
	private String mobileType;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("mobileNo")
	private String mobileNo;

	@SerializedName("about_me")
	private String aboutMe;

	@SerializedName("zipcode")
	private String zipcode;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("_id")
	private String id;

	@SerializedName("state")
	private String state;

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setPhoneType(String phoneType){
		this.phoneType = phoneType;
	}

	public String getPhoneType(){
		return phoneType;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setHomephone(String homephone){
		this.homephone = homephone;
	}

	public String getHomephone(){
		return homephone;
	}

	public void setAddress2(String address2){
		this.address2 = address2;
	}

	public String getAddress2(){
		return address2;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setMobileType(String mobileType){
		this.mobileType = mobileType;
	}

	public String getMobileType(){
		return mobileType;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setMobileNo(String mobileNo){
		this.mobileNo = mobileNo;
	}

	public String getMobileNo(){
		return mobileNo;
	}

	public void setAboutMe(String aboutMe){
		this.aboutMe = aboutMe;
	}

	public String getAboutMe(){
		return aboutMe;
	}

	public void setZipcode(String zipcode){
		this.zipcode = zipcode;
	}

	public String getZipcode(){
		return zipcode;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	@Override
 	public String toString(){
		return 
			"Profile{" + 
			"country = '" + country + '\'' + 
			",phoneType = '" + phoneType + '\'' + 
			",address = '" + address + '\'' + 
			",homephone = '" + homephone + '\'' + 
			",address2 = '" + address2 + '\'' + 
			",city = '" + city + '\'' + 
			",mobileType = '" + mobileType + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",mobileNo = '" + mobileNo + '\'' + 
			",about_me = '" + aboutMe + '\'' + 
			",zipcode = '" + zipcode + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",user_id = '" + userId + '\'' + 
			",_id = '" + id + '\'' + 
			",state = '" + state + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.country);
		dest.writeString(this.phoneType);
		dest.writeString(this.address);
		dest.writeString(this.homephone);
		dest.writeString(this.address2);
		dest.writeString(this.city);
		dest.writeString(this.mobileType);
		dest.writeString(this.createdAt);
		dest.writeString(this.mobileNo);
		dest.writeString(this.aboutMe);
		dest.writeString(this.zipcode);
		dest.writeString(this.updatedAt);
		dest.writeString(this.userId);
		dest.writeString(this.id);
		dest.writeString(this.state);
	}

	public Profile() {
	}

	protected Profile(Parcel in) {
		this.country = in.readString();
		this.phoneType = in.readString();
		this.address = in.readString();
		this.homephone = in.readString();
		this.address2 = in.readString();
		this.city = in.readString();
		this.mobileType = in.readString();
		this.createdAt = in.readString();
		this.mobileNo = in.readString();
		this.aboutMe = in.readString();
		this.zipcode = in.readString();
		this.updatedAt = in.readString();
		this.userId = in.readString();
		this.id = in.readString();
		this.state = in.readString();
	}

	public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile>() {
		@Override
		public Profile createFromParcel(Parcel source) {
			return new Profile(source);
		}

		@Override
		public Profile[] newArray(int size) {
			return new Profile[size];
		}
	};
}