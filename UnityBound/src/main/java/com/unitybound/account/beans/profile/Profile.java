package com.unitybound.account.beans.profile;

import com.google.gson.annotations.SerializedName;

public class Profile{

	@SerializedName("country")
	private String country;

	@SerializedName("address")
	private String address;

	@SerializedName("homephone")
	private Object homephone;

	@SerializedName("city")
	private String city;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("mobileNo")
	private Object mobileNo;

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

	@SerializedName("email")
	private String email;

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setHomephone(Object homephone){
		this.homephone = homephone;
	}

	public Object getHomephone(){
		return homephone;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setMobileNo(Object mobileNo){
		this.mobileNo = mobileNo;
	}

	public Object getMobileNo(){
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

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	@Override
 	public String toString(){
		return 
			"Profile{" + 
			"country = '" + country + '\'' + 
			",address = '" + address + '\'' + 
			",homephone = '" + homephone + '\'' + 
			",city = '" + city + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",mobileNo = '" + mobileNo + '\'' + 
			",zip_code = '" + zipCode + '\'' + 
			",about_me = '" + aboutMe + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",user_id = '" + userId + '\'' + 
			",_id = '" + id + '\'' + 
			",state = '" + state + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}
}