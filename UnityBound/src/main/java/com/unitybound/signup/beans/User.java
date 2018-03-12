package com.unitybound.signup.beans;

import com.google.gson.annotations.SerializedName;

public class User{

	@SerializedName("lastName")
	private String lastName;

	@SerializedName("settings")
	private Settings settings;

	@SerializedName("gender")
	private String gender;

	@SerializedName("active")
	private int active;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("dateOfBirth")
	private String dateOfBirth;

	@SerializedName("userName")
	private String userName;

	@SerializedName("firstName")
	private String firstName;

	@SerializedName("relationship_status")
	private String relationshipStatus;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("accessCode")
	private String accessCode;

	@SerializedName("_id")
	private String id;

	@SerializedName("email")
	private String email;

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setSettings(Settings settings){
		this.settings = settings;
	}

	public Settings getSettings(){
		return settings;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getGender(){
		return gender;
	}

	public void setActive(int active){
		this.active = active;
	}

	public int getActive(){
		return active;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setDateOfBirth(String dateOfBirth){
		this.dateOfBirth = dateOfBirth;
	}

	public String getDateOfBirth(){
		return dateOfBirth;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return userName;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setRelationshipStatus(String relationshipStatus){
		this.relationshipStatus = relationshipStatus;
	}

	public String getRelationshipStatus(){
		return relationshipStatus;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setAccessCode(String accessCode){
		this.accessCode = accessCode;
	}

	public String getAccessCode(){
		return accessCode;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
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
			"User{" + 
			"lastName = '" + lastName + '\'' + 
			",settings = '" + settings + '\'' + 
			",gender = '" + gender + '\'' + 
			",active = '" + active + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",dateOfBirth = '" + dateOfBirth + '\'' + 
			",userName = '" + userName + '\'' + 
			",firstName = '" + firstName + '\'' + 
			",relationship_status = '" + relationshipStatus + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",accessCode = '" + accessCode + '\'' + 
			",_id = '" + id + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}
}