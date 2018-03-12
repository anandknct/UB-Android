package com.unitybound.weddings.beans.WeadingDetailResponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeddingDetails implements Parcelable {

	@SerializedName("weddingBrideName")
	private String weddingBrideName;

	@SerializedName("weddingLocation")
	private String weddingLocation;

	@SerializedName("latitude")
	private Double latitude;

	@SerializedName("longitude")
	private Double longitude;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("weddingDate")
	private String weddingDate;

	@SerializedName("weddingGroomFatherName")
	private String weddingGroomFatherName;

	@SerializedName("weddingAlbumUrl3")
	private String weddingAlbumUrl3;

	@SerializedName("weddingAlbumUrl2")
	private String weddingAlbumUrl2;

	@SerializedName("weddingAlbumUrl1")
	private String weddingAlbumUrl1;

	@SerializedName("updated_at")
	private String updatedAt;

	@Expose
	@SerializedName("added_by")
	private String addedBy;

	@SerializedName("weddingGiftUrl2")
	private String weddingGiftUrl2;

	@SerializedName("weddingGiftUrl1")
	private String weddingGiftUrl1;

	@SerializedName("wedding_image_s")
	private String weddingImageS;

	@SerializedName("weddingGiftUrl3")
	private String weddingGiftUrl3;

	@SerializedName("weddingBrideFatherName")
	private String weddingBrideFatherName;

	@SerializedName("weddingGiftTitle1")
	private String weddingGiftTitle1;

	@SerializedName("weddingGiftTitle3")
	private String weddingGiftTitle3;

	@SerializedName("weddingGiftTitle2")
	private String weddingGiftTitle2;

	@SerializedName("wedding_image_l")
	private String weddingImageL;

	@SerializedName("weddingBrideMotherName")
	private String weddingBrideMotherName;

	@SerializedName("wedding_image_m")
	private String weddingImageM;

	@SerializedName("weddingGroomMotherName")
	private String weddingGroomMotherName;

	@SerializedName("weddingDescription")
	private String weddingDescription;

	@SerializedName("weddingTime")
	private String weddingTime;

	@SerializedName("weddingGroomName")
	private String weddingGroomName;

	@SerializedName("_id")
	private String id;

	@SerializedName("weddingAlbumTitle1")
	private String weddingAlbumTitle1;

	@SerializedName("weddingAlbumTitle2")
	private String weddingAlbumTitle2;

	@SerializedName("weddingAlbumTitle3")
	private String weddingAlbumTitle3;

	@SerializedName("wedding_image")
	private String weddingImage;

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public void setWeddingBrideName(String weddingBrideName){
		this.weddingBrideName = weddingBrideName;
	}

	public String getWeddingBrideName(){
		return weddingBrideName;
	}

	public void setWeddingLocation(String weddingLocation){
		this.weddingLocation = weddingLocation;
	}

	public String getWeddingLocation(){
		return weddingLocation;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setWeddingDate(String weddingDate){
		this.weddingDate = weddingDate;
	}

	public String getWeddingDate(){
		return weddingDate;
	}

	public void setWeddingGroomFatherName(String weddingGroomFatherName){
		this.weddingGroomFatherName = weddingGroomFatherName;
	}

	public String getWeddingGroomFatherName(){
		return weddingGroomFatherName;
	}

	public void setWeddingAlbumUrl3(String weddingAlbumUrl3){
		this.weddingAlbumUrl3 = weddingAlbumUrl3;
	}

	public String getWeddingAlbumUrl3(){
		return weddingAlbumUrl3;
	}

	public void setWeddingAlbumUrl2(String weddingAlbumUrl2){
		this.weddingAlbumUrl2 = weddingAlbumUrl2;
	}

	public String getWeddingAlbumUrl2(){
		return weddingAlbumUrl2;
	}

	public void setWeddingAlbumUrl1(String weddingAlbumUrl1){
		this.weddingAlbumUrl1 = weddingAlbumUrl1;
	}

	public String getWeddingAlbumUrl1(){
		return weddingAlbumUrl1;
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

	public void setWeddingGiftUrl2(String weddingGiftUrl2){
		this.weddingGiftUrl2 = weddingGiftUrl2;
	}

	public String getWeddingGiftUrl2(){
		return weddingGiftUrl2;
	}

	public void setWeddingGiftUrl1(String weddingGiftUrl1){
		this.weddingGiftUrl1 = weddingGiftUrl1;
	}

	public String getWeddingGiftUrl1(){
		return weddingGiftUrl1;
	}

	public void setWeddingImageS(String weddingImageS){
		this.weddingImageS = weddingImageS;
	}

	public String getWeddingImageS(){
		return weddingImageS;
	}

	public void setWeddingGiftUrl3(String weddingGiftUrl3){
		this.weddingGiftUrl3 = weddingGiftUrl3;
	}

	public String getWeddingGiftUrl3(){
		return weddingGiftUrl3;
	}

	public void setWeddingBrideFatherName(String weddingBrideFatherName){
		this.weddingBrideFatherName = weddingBrideFatherName;
	}

	public String getWeddingBrideFatherName(){
		return weddingBrideFatherName;
	}

	public void setWeddingGiftTitle1(String weddingGiftTitle1){
		this.weddingGiftTitle1 = weddingGiftTitle1;
	}

	public String getWeddingGiftTitle1(){
		return weddingGiftTitle1;
	}

	public void setWeddingGiftTitle3(String weddingGiftTitle3){
		this.weddingGiftTitle3 = weddingGiftTitle3;
	}

	public String getWeddingGiftTitle3(){
		return weddingGiftTitle3;
	}

	public void setWeddingGiftTitle2(String weddingGiftTitle2){
		this.weddingGiftTitle2 = weddingGiftTitle2;
	}

	public String getWeddingGiftTitle2(){
		return weddingGiftTitle2;
	}

	public void setWeddingImageL(String weddingImageL){
		this.weddingImageL = weddingImageL;
	}

	public String getWeddingImageL(){
		return weddingImageL;
	}

	public void setWeddingBrideMotherName(String weddingBrideMotherName){
		this.weddingBrideMotherName = weddingBrideMotherName;
	}

	public String getWeddingBrideMotherName(){
		return weddingBrideMotherName;
	}

	public void setWeddingImageM(String weddingImageM){
		this.weddingImageM = weddingImageM;
	}

	public String getWeddingImageM(){
		return weddingImageM;
	}

	public void setWeddingGroomMotherName(String weddingGroomMotherName){
		this.weddingGroomMotherName = weddingGroomMotherName;
	}

	public String getWeddingGroomMotherName(){
		return weddingGroomMotherName;
	}

	public void setWeddingDescription(String weddingDescription){
		this.weddingDescription = weddingDescription;
	}

	public String getWeddingDescription(){
		return weddingDescription;
	}

	public void setWeddingTime(String weddingTime){
		this.weddingTime = weddingTime;
	}

	public String getWeddingTime(){
		return weddingTime;
	}

	public void setWeddingGroomName(String weddingGroomName){
		this.weddingGroomName = weddingGroomName;
	}

	public String getWeddingGroomName(){
		return weddingGroomName;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setWeddingAlbumTitle1(String weddingAlbumTitle1){
		this.weddingAlbumTitle1 = weddingAlbumTitle1;
	}

	public String getWeddingAlbumTitle1(){
		return weddingAlbumTitle1;
	}

	public void setWeddingAlbumTitle2(String weddingAlbumTitle2){
		this.weddingAlbumTitle2 = weddingAlbumTitle2;
	}

	public String getWeddingAlbumTitle2(){
		return weddingAlbumTitle2;
	}

	public void setWeddingAlbumTitle3(String weddingAlbumTitle3){
		this.weddingAlbumTitle3 = weddingAlbumTitle3;
	}

	public String getWeddingAlbumTitle3(){
		return weddingAlbumTitle3;
	}

	public void setWeddingImage(String weddingImage){
		this.weddingImage = weddingImage;
	}

	public String getWeddingImage(){
		return weddingImage;
	}

	@Override
 	public String toString(){
		return 
			"WeddingDetails{" + 
			"weddingBrideName = '" + weddingBrideName + '\'' + 
			",weddingLocation = '" + weddingLocation + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",weddingDate = '" + weddingDate + '\'' + 
			",weddingGroomFatherName = '" + weddingGroomFatherName + '\'' + 
			",weddingAlbumUrl3 = '" + weddingAlbumUrl3 + '\'' + 
			",weddingAlbumUrl2 = '" + weddingAlbumUrl2 + '\'' + 
			",weddingAlbumUrl1 = '" + weddingAlbumUrl1 + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",added_by = '" + addedBy + '\'' + 
			",weddingGiftUrl2 = '" + weddingGiftUrl2 + '\'' + 
			",weddingGiftUrl1 = '" + weddingGiftUrl1 + '\'' + 
			",wedding_image_s = '" + weddingImageS + '\'' + 
			",weddingGiftUrl3 = '" + weddingGiftUrl3 + '\'' + 
			",weddingBrideFatherName = '" + weddingBrideFatherName + '\'' + 
			",weddingGiftTitle1 = '" + weddingGiftTitle1 + '\'' + 
			",weddingGiftTitle3 = '" + weddingGiftTitle3 + '\'' + 
			",weddingGiftTitle2 = '" + weddingGiftTitle2 + '\'' + 
			",wedding_image_l = '" + weddingImageL + '\'' + 
			",weddingBrideMotherName = '" + weddingBrideMotherName + '\'' + 
			",wedding_image_m = '" + weddingImageM + '\'' + 
			",weddingGroomMotherName = '" + weddingGroomMotherName + '\'' + 
			",weddingDescription = '" + weddingDescription + '\'' + 
			",weddingTime = '" + weddingTime + '\'' + 
			",weddingGroomName = '" + weddingGroomName + '\'' + 
			",_id = '" + id + '\'' + 
			",weddingAlbumTitle1 = '" + weddingAlbumTitle1 + '\'' + 
			",weddingAlbumTitle2 = '" + weddingAlbumTitle2 + '\'' + 
			",weddingAlbumTitle3 = '" + weddingAlbumTitle3 + '\'' + 
			",wedding_image = '" + weddingImage + '\'' + 
			"}";
		}

	public WeddingDetails() {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.weddingBrideName);
		dest.writeString(this.weddingLocation);
		dest.writeValue(this.latitude);
		dest.writeValue(this.longitude);
		dest.writeString(this.createdAt);
		dest.writeString(this.weddingDate);
		dest.writeString(this.weddingGroomFatherName);
		dest.writeString(this.weddingAlbumUrl3);
		dest.writeString(this.weddingAlbumUrl2);
		dest.writeString(this.weddingAlbumUrl1);
		dest.writeString(this.updatedAt);
		dest.writeString(this.addedBy);
		dest.writeString(this.weddingGiftUrl2);
		dest.writeString(this.weddingGiftUrl1);
		dest.writeString(this.weddingImageS);
		dest.writeString(this.weddingGiftUrl3);
		dest.writeString(this.weddingBrideFatherName);
		dest.writeString(this.weddingGiftTitle1);
		dest.writeString(this.weddingGiftTitle3);
		dest.writeString(this.weddingGiftTitle2);
		dest.writeString(this.weddingImageL);
		dest.writeString(this.weddingBrideMotherName);
		dest.writeString(this.weddingImageM);
		dest.writeString(this.weddingGroomMotherName);
		dest.writeString(this.weddingDescription);
		dest.writeString(this.weddingTime);
		dest.writeString(this.weddingGroomName);
		dest.writeString(this.id);
		dest.writeString(this.weddingAlbumTitle1);
		dest.writeString(this.weddingAlbumTitle2);
		dest.writeString(this.weddingAlbumTitle3);
		dest.writeString(this.weddingImage);
	}

	protected WeddingDetails(Parcel in) {
		this.weddingBrideName = in.readString();
		this.weddingLocation = in.readString();
		this.latitude = (Double) in.readValue(Double.class.getClassLoader());
		this.longitude = (Double) in.readValue(Double.class.getClassLoader());
		this.createdAt = in.readString();
		this.weddingDate = in.readString();
		this.weddingGroomFatherName = in.readString();
		this.weddingAlbumUrl3 = in.readString();
		this.weddingAlbumUrl2 = in.readString();
		this.weddingAlbumUrl1 = in.readString();
		this.updatedAt = in.readString();
		this.addedBy = in.readString();
		this.weddingGiftUrl2 = in.readString();
		this.weddingGiftUrl1 = in.readString();
		this.weddingImageS = in.readString();
		this.weddingGiftUrl3 = in.readString();
		this.weddingBrideFatherName = in.readString();
		this.weddingGiftTitle1 = in.readString();
		this.weddingGiftTitle3 = in.readString();
		this.weddingGiftTitle2 = in.readString();
		this.weddingImageL = in.readString();
		this.weddingBrideMotherName = in.readString();
		this.weddingImageM = in.readString();
		this.weddingGroomMotherName = in.readString();
		this.weddingDescription = in.readString();
		this.weddingTime = in.readString();
		this.weddingGroomName = in.readString();
		this.id = in.readString();
		this.weddingAlbumTitle1 = in.readString();
		this.weddingAlbumTitle2 = in.readString();
		this.weddingAlbumTitle3 = in.readString();
		this.weddingImage = in.readString();
	}

	public static final Creator<WeddingDetails> CREATOR = new Creator<WeddingDetails>() {
		@Override
		public WeddingDetails createFromParcel(Parcel source) {
			return new WeddingDetails(source);
		}

		@Override
		public WeddingDetails[] newArray(int size) {
			return new WeddingDetails[size];
		}
	};
}