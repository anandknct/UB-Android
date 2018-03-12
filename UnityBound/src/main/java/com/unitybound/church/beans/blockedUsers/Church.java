package com.unitybound.church.beans.blockedUsers;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Church implements Parcelable {

	@SerializedName("churchMailZip")
	private String churchMailZip;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("churchMailAddress2")
	private String churchMailAddress2;

	@SerializedName("churchMailAddress1")
	private String churchMailAddress1;

	@SerializedName("churchStatus")
	private String churchStatus;

	@SerializedName("churchMailState")
	private String churchMailState;

	@SerializedName("churchPhoneNumber")
	private String churchPhoneNumber;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("added_by")
	private String addedBy;

	@SerializedName("accessCode")
	private String accessCode;

	@SerializedName("churchCity")
	private String churchCity;

	@SerializedName("churchDescription")
	private String churchDescription;

	@SerializedName("churchName")
	private String churchName;

	@SerializedName("memberCount")
	private int memberCount;

	@SerializedName("churchPastorName")
	private String churchPastorName;

	@SerializedName("churchMailCountry")
	private String churchMailCountry;

	@SerializedName("churchZip")
	private String churchZip;

	@SerializedName("approved_by")
	private String approvedBy;

	@SerializedName("churchAddress1")
	private String churchAddress1;

	@SerializedName("churchState")
	private String churchState;

	@SerializedName("churchMailCity")
	private String churchMailCity;

	@SerializedName("_id")
	private String id;

	@SerializedName("churchMembers")
	private int churchMembers;

	@SerializedName("churchDenominations")
	private String churchDenominations;

	@SerializedName("churchCountry")
	private String churchCountry;

	public void setChurchMailZip(String churchMailZip){
		this.churchMailZip = churchMailZip;
	}

	public String getChurchMailZip(){
		return churchMailZip;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setChurchMailAddress2(String churchMailAddress2){
		this.churchMailAddress2 = churchMailAddress2;
	}

	public Object getChurchMailAddress2(){
		return churchMailAddress2;
	}

	public void setChurchMailAddress1(String churchMailAddress1){
		this.churchMailAddress1 = churchMailAddress1;
	}

	public String getChurchMailAddress1(){
		return churchMailAddress1;
	}

	public void setChurchStatus(String churchStatus){
		this.churchStatus = churchStatus;
	}

	public String getChurchStatus(){
		return churchStatus;
	}

	public void setChurchMailState(String churchMailState){
		this.churchMailState = churchMailState;
	}

	public String getChurchMailState(){
		return churchMailState;
	}

	public void setChurchPhoneNumber(String churchPhoneNumber){
		this.churchPhoneNumber = churchPhoneNumber;
	}

	public String getChurchPhoneNumber(){
		return churchPhoneNumber;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setAddedBy(String addedBy){
		this.addedBy = addedBy;
	}

	public String getAddedBy(){
		return addedBy;
	}

	public void setAccessCode(String accessCode){
		this.accessCode = accessCode;
	}

	public String getAccessCode(){
		return accessCode;
	}

	public void setChurchCity(String churchCity){
		this.churchCity = churchCity;
	}

	public String getChurchCity(){
		return churchCity;
	}

	public void setChurchDescription(String churchDescription){
		this.churchDescription = churchDescription;
	}

	public String getChurchDescription(){
		return churchDescription;
	}

	public void setChurchName(String churchName){
		this.churchName = churchName;
	}

	public String getChurchName(){
		return churchName;
	}

	public void setMemberCount(int memberCount){
		this.memberCount = memberCount;
	}

	public int getMemberCount(){
		return memberCount;
	}

	public void setChurchPastorName(String churchPastorName){
		this.churchPastorName = churchPastorName;
	}

	public String getChurchPastorName(){
		return churchPastorName;
	}

	public void setChurchMailCountry(String churchMailCountry){
		this.churchMailCountry = churchMailCountry;
	}

	public String getChurchMailCountry(){
		return churchMailCountry;
	}

	public void setChurchZip(String churchZip){
		this.churchZip = churchZip;
	}

	public String getChurchZip(){
		return churchZip;
	}

	public void setApprovedBy(String approvedBy){
		this.approvedBy = approvedBy;
	}

	public String getApprovedBy(){
		return approvedBy;
	}

	public void setChurchAddress1(String churchAddress1){
		this.churchAddress1 = churchAddress1;
	}

	public String getChurchAddress1(){
		return churchAddress1;
	}

	public void setChurchState(String churchState){
		this.churchState = churchState;
	}

	public String getChurchState(){
		return churchState;
	}

	public void setChurchMailCity(String churchMailCity){
		this.churchMailCity = churchMailCity;
	}

	public String getChurchMailCity(){
		return churchMailCity;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setChurchMembers(int churchMembers){
		this.churchMembers = churchMembers;
	}

	public int getChurchMembers(){
		return churchMembers;
	}

	public void setChurchDenominations(String churchDenominations){
		this.churchDenominations = churchDenominations;
	}

	public String getChurchDenominations(){
		return churchDenominations;
	}

	public void setChurchCountry(String churchCountry){
		this.churchCountry = churchCountry;
	}

	public String getChurchCountry(){
		return churchCountry;
	}

	@Override
 	public String toString(){
		return 
			"Church{" + 
			"churchMailZip = '" + churchMailZip + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",churchMailAddress2 = '" + churchMailAddress2 + '\'' + 
			",churchMailAddress1 = '" + churchMailAddress1 + '\'' + 
			",churchStatus = '" + churchStatus + '\'' + 
			",churchMailState = '" + churchMailState + '\'' + 
			",churchPhoneNumber = '" + churchPhoneNumber + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",added_by = '" + addedBy + '\'' + 
			",accessCode = '" + accessCode + '\'' + 
			",churchCity = '" + churchCity + '\'' + 
			",churchDescription = '" + churchDescription + '\'' + 
			",churchName = '" + churchName + '\'' + 
			",memberCount = '" + memberCount + '\'' + 
			",churchPastorName = '" + churchPastorName + '\'' + 
			",churchMailCountry = '" + churchMailCountry + '\'' + 
			",churchZip = '" + churchZip + '\'' + 
			",approved_by = '" + approvedBy + '\'' + 
			",churchAddress1 = '" + churchAddress1 + '\'' + 
			",churchState = '" + churchState + '\'' + 
			",churchMailCity = '" + churchMailCity + '\'' + 
			",_id = '" + id + '\'' + 
			",churchMembers = '" + churchMembers + '\'' + 
			",churchDenominations = '" + churchDenominations + '\'' + 
			",churchCountry = '" + churchCountry + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.churchMailZip);
		dest.writeString(this.createdAt);
		dest.writeString(this.churchMailAddress2);
		dest.writeString(this.churchMailAddress1);
		dest.writeString(this.churchStatus);
		dest.writeString(this.churchMailState);
		dest.writeString(this.churchPhoneNumber);
		dest.writeString(this.updatedAt);
		dest.writeString(this.addedBy);
		dest.writeString(this.accessCode);
		dest.writeString(this.churchCity);
		dest.writeString(this.churchDescription);
		dest.writeString(this.churchName);
		dest.writeInt(this.memberCount);
		dest.writeString(this.churchPastorName);
		dest.writeString(this.churchMailCountry);
		dest.writeString(this.churchZip);
		dest.writeString(this.approvedBy);
		dest.writeString(this.churchAddress1);
		dest.writeString(this.churchState);
		dest.writeString(this.churchMailCity);
		dest.writeString(this.id);
		dest.writeInt(this.churchMembers);
		dest.writeString(this.churchDenominations);
		dest.writeString(this.churchCountry);
	}

	public Church() {
	}

	protected Church(Parcel in) {
		this.churchMailZip = in.readString();
		this.createdAt = in.readString();
		this.churchMailAddress2 = in.readParcelable(Object.class.getClassLoader());
		this.churchMailAddress1 = in.readString();
		this.churchStatus = in.readString();
		this.churchMailState = in.readString();
		this.churchPhoneNumber = in.readString();
		this.updatedAt = in.readString();
		this.addedBy = in.readString();
		this.accessCode = in.readString();
		this.churchCity = in.readString();
		this.churchDescription = in.readString();
		this.churchName = in.readString();
		this.memberCount = in.readInt();
		this.churchPastorName = in.readString();
		this.churchMailCountry = in.readString();
		this.churchZip = in.readString();
		this.approvedBy = in.readString();
		this.churchAddress1 = in.readString();
		this.churchState = in.readString();
		this.churchMailCity = in.readString();
		this.id = in.readString();
		this.churchMembers = in.readInt();
		this.churchDenominations = in.readString();
		this.churchCountry = in.readString();
	}

	public static final Parcelable.Creator<Church> CREATOR = new Parcelable.Creator<Church>() {
		@Override
		public Church createFromParcel(Parcel source) {
			return new Church(source);
		}

		@Override
		public Church[] newArray(int size) {
			return new Church[size];
		}
	};
}