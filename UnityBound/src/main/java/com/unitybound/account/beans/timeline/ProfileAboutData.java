package com.unitybound.account.beans.timeline;

import com.google.gson.annotations.SerializedName;

public class ProfileAboutData{

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

	@SerializedName("latitude")
	private double latitude;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("mobileType")
	private String mobileType;

	@SerializedName("mobileNo")
	private String mobileNo;

	@SerializedName("zip_code")
	private String zipCode;

	@SerializedName("about_me")
	private String aboutMe;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("_id")
	private String id;

	@SerializedName("state")
	private String state;

	@SerializedName("longitude")
	private double longitude;

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

	public void setLatitude(double latitude){
		this.latitude = latitude;
	}

	public double getLatitude(){
		return latitude;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setMobileType(String mobileType){
		this.mobileType = mobileType;
	}

	public String getMobileType(){
		return mobileType;
	}

	public void setMobileNo(String mobileNo){
		this.mobileNo = mobileNo;
	}

	public String getMobileNo(){
		return mobileNo;
	}

	public void setZipCode(String zipCode){
		this.zipCode = zipCode;
	}

	public String getZipCode(){
		return zipCode;
	}

	public void setAboutMe(String aboutMe){
		this.aboutMe = aboutMe;
	}

	public String getAboutMe(){
		return aboutMe;
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

	public void setLongitude(double longitude){
		this.longitude = longitude;
	}

	public double getLongitude(){
		return longitude;
	}

	@Override
 	public String toString(){
		return 
			"ProfileAboutData{" + 
			"country = '" + country + '\'' + 
			",phoneType = '" + phoneType + '\'' + 
			",address = '" + address + '\'' + 
			",homephone = '" + homephone + '\'' + 
			",address2 = '" + address2 + '\'' + 
			",city = '" + city + '\'' + 
			",latitude = '" + latitude + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",mobileType = '" + mobileType + '\'' + 
			",mobileNo = '" + mobileNo + '\'' + 
			",zip_code = '" + zipCode + '\'' + 
			",about_me = '" + aboutMe + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",user_id = '" + userId + '\'' + 
			",_id = '" + id + '\'' + 
			",state = '" + state + '\'' + 
			",longitude = '" + longitude + '\'' + 
			"}";
		}
}