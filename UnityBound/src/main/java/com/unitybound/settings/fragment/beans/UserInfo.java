package com.unitybound.settings.fragment.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserInfo implements Parcelable {

	@SerializedName("GoogleAccountId")
	private String googleAccountId;

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

	@SerializedName("profile")
	private Profile profile;

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

	@SerializedName("profileImage_s")
	private String profileImageS;

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

	@SerializedName("profileCoverImage")
	private String profileCoverImage;

	@SerializedName("email")
	private String email;

	public void setGoogleAccountId(String googleAccountId){
		this.googleAccountId = googleAccountId;
	}

	public String getGoogleAccountId(){
		return googleAccountId;
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

	public void setProfile(Profile profile){
		this.profile = profile;
	}

	public Profile getProfile(){
		return profile;
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

	public void setProfileImageS(String profileImageS){
		this.profileImageS = profileImageS;
	}

	public String getProfileImageS(){
		return profileImageS;
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

	public void setProfileCoverImage(String profileCoverImage){
		this.profileCoverImage = profileCoverImage;
	}

	public String getProfileCoverImage(){
		return profileCoverImage;
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
			"UserInfo{" + 
			"googleAccountId = '" + googleAccountId + '\'' + 
			",lastName = '" + lastName + '\'' + 
			",notification_email = '" + notificationEmail + '\'' +
			",gender = '" + gender + '\'' + 
			",nickName = '" + nickName + '\'' + 
			",facebookAccountId = '" + facebookAccountId + '\'' + 
			",profile = '" + profile + '\'' + 
			",active = '" + active + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",dateOfBirth = '" + dateOfBirth + '\'' + 
			",profileImage = '" + profileImage + '\'' + 
			",userName = '" + userName + '\'' + 
			",profileImage_s = '" + profileImageS + '\'' + 
			",firstName = '" + firstName + '\'' + 
			",twitterAccountId = '" + twitterAccountId + '\'' + 
			",relationship_status = '" + relationshipStatus + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",provider = '" + provider + '\'' + 
			",accessCode = '" + accessCode + '\'' + 
			",_id = '" + id + '\'' + 
			",profileCoverImage = '" + profileCoverImage + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.googleAccountId);
		dest.writeString(this.lastName);
		dest.writeString(this.notificationEmail);
		dest.writeString(this.gender);
		dest.writeString(this.nickName);
		dest.writeString(this.facebookAccountId);
		dest.writeParcelable(this.profile, flags);
		dest.writeInt(this.active);
		dest.writeString(this.createdAt);
		dest.writeString(this.dateOfBirth);
		dest.writeString(this.profileImage);
		dest.writeString(this.userName);
		dest.writeString(this.profileImageS);
		dest.writeString(this.firstName);
		dest.writeString(this.twitterAccountId);
		dest.writeString(this.relationshipStatus);
		dest.writeString(this.updatedAt);
		dest.writeString(this.provider);
		dest.writeString(this.accessCode);
		dest.writeString(this.id);
		dest.writeString(this.profileCoverImage);
		dest.writeString(this.email);
	}

	public UserInfo() {
	}

	protected UserInfo(Parcel in) {
		this.googleAccountId = in.readString();
		this.lastName = in.readString();
		this.notificationEmail = in.readString();
		this.gender = in.readString();
		this.nickName = in.readString();
		this.facebookAccountId = in.readString();
		this.profile = in.readParcelable(Profile.class.getClassLoader());
		this.active = in.readInt();
		this.createdAt = in.readString();
		this.dateOfBirth = in.readString();
		this.profileImage = in.readString();
		this.userName = in.readString();
		this.profileImageS = in.readString();
		this.firstName = in.readString();
		this.twitterAccountId = in.readString();
		this.relationshipStatus = in.readString();
		this.updatedAt = in.readString();
		this.provider = in.readString();
		this.accessCode = in.readString();
		this.id = in.readString();
		this.profileCoverImage = in.readString();
		this.email = in.readString();
	}

	public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
		@Override
		public UserInfo createFromParcel(Parcel source) {
			return new UserInfo(source);
		}

		@Override
		public UserInfo[] newArray(int size) {
			return new UserInfo[size];
		}
	};
}