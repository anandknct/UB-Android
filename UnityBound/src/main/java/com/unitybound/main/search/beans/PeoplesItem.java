package com.unitybound.main.search.beans;

import com.google.gson.annotations.SerializedName;

public class PeoplesItem{

	@SerializedName("GoogleAccountId")
	private String googleAccountId;

	@SerializedName("suspend")
	private int suspend;

	@SerializedName("lastName")
	private String lastName;

	@SerializedName("notification_email")
	private String notificationEmail;

	@SerializedName("gender")
	private String gender;

	@SerializedName("nickName")
	private String nickName;

	@SerializedName("facebookAccountId")
	private String facebookAccountId;

	@SerializedName("active")
	private int active;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("dateOfBirth")
	private String dateOfBirth;

	@SerializedName("profileImage")
	private String profileImage;

	@SerializedName("userName")
	private String userName;

	@SerializedName("firstName")
	private String firstName;

	@SerializedName("twitterAccountId")
	private String twitterAccountId;

	@SerializedName("relationship_status")
	private String relationshipStatus;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("provider")
	private String provider;

	@SerializedName("accessCode")
	private String accessCode;

	@SerializedName("_id")
	private String id;

	@SerializedName("email")
	private String email;

	public void setGoogleAccountId(String googleAccountId){
		this.googleAccountId = googleAccountId;
	}

	public String getGoogleAccountId(){
		return googleAccountId;
	}

	public void setSuspend(int suspend){
		this.suspend = suspend;
	}

	public int getSuspend(){
		return suspend;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setNotificationEmail(String notificationEmail){
		this.notificationEmail = notificationEmail;
	}

	public String getNotificationEmail(){
		return notificationEmail;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getGender(){
		return gender;
	}

	public void setNickName(String nickName){
		this.nickName = nickName;
	}

	public String getNickName(){
		return nickName;
	}

	public void setFacebookAccountId(String facebookAccountId){
		this.facebookAccountId = facebookAccountId;
	}

	public String getFacebookAccountId(){
		return facebookAccountId;
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

	public void setProfileImage(String profileImage){
		this.profileImage = profileImage;
	}

	public String getProfileImage(){
		return profileImage;
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

	public void setTwitterAccountId(String twitterAccountId){
		this.twitterAccountId = twitterAccountId;
	}

	public String getTwitterAccountId(){
		return twitterAccountId;
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

	public void setProvider(String provider){
		this.provider = provider;
	}

	public String getProvider(){
		return provider;
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
			"PeoplesItem{" + 
			"googleAccountId = '" + googleAccountId + '\'' + 
			",suspend = '" + suspend + '\'' + 
			",lastName = '" + lastName + '\'' + 
			",notification_email = '" + notificationEmail + '\'' + 
			",gender = '" + gender + '\'' + 
			",nickName = '" + nickName + '\'' + 
			",facebookAccountId = '" + facebookAccountId + '\'' + 
			",active = '" + active + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",dateOfBirth = '" + dateOfBirth + '\'' + 
			",profileImage = '" + profileImage + '\'' + 
			",userName = '" + userName + '\'' + 
			",firstName = '" + firstName + '\'' + 
			",twitterAccountId = '" + twitterAccountId + '\'' + 
			",relationship_status = '" + relationshipStatus + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",provider = '" + provider + '\'' + 
			",accessCode = '" + accessCode + '\'' + 
			",_id = '" + id + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}
}