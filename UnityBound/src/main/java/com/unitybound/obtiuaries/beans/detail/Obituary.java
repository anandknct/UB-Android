package com.unitybound.obtiuaries.beans.detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Obituary{

	@SerializedName("obituaryAddress2")
	private String obituaryAddress2;

	@SerializedName("obituaryAddress1")
	private String obituaryAddress1;

	@SerializedName("obituaryFuneralDate")
	private String obituaryFuneralDate;

	@SerializedName("obituaryState")
	private String obituaryState;

	@SerializedName("created_at")
	private String createdAt;

	@Expose
	@SerializedName("obituaryLng")
	private double obituaryLng;

	@SerializedName("obituaryName")
	private String obituaryName;

	@SerializedName("obituaryZip")
	private String obituaryZip;

	@SerializedName("obituaryFuneralTime")
	private String obituaryFuneralTime;

	@SerializedName("obituaryCity")
	private String obituaryCity;

	@Expose
	@SerializedName("obituaryLat")
	private double obituaryLat;

	@SerializedName("obituaryBirthDate")
	private String obituaryBirthDate;

	@SerializedName("obituaryCountry")
	private String obituaryCountry;

	@SerializedName("obituaryMapUrl")
	private String obituaryMapUrl;

	@SerializedName("obituary_image")
	private String obituaryImage;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("added_by")
	private String addedBy;

	@SerializedName("obituaryDescription")
	private String obituaryDescription;

	@SerializedName("obituaryDeathDate")
	private String obituaryDeathDate;

	@SerializedName("_id")
	private String id;

	public void setObituaryAddress2(String obituaryAddress2){
		this.obituaryAddress2 = obituaryAddress2;
	}

	public String getObituaryAddress2(){
		return obituaryAddress2;
	}

	public void setObituaryAddress1(String obituaryAddress1){
		this.obituaryAddress1 = obituaryAddress1;
	}

	public String getObituaryAddress1(){
		return obituaryAddress1;
	}

	public void setObituaryFuneralDate(String obituaryFuneralDate){
		this.obituaryFuneralDate = obituaryFuneralDate;
	}

	public String getObituaryFuneralDate(){
		return obituaryFuneralDate;
	}

	public void setObituaryState(String obituaryState){
		this.obituaryState = obituaryState;
	}

	public String getObituaryState(){
		return obituaryState;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setObituaryLng(double obituaryLng){
		this.obituaryLng = obituaryLng;
	}

	public double getObituaryLng(){
		return obituaryLng;
	}

	public void setObituaryName(String obituaryName){
		this.obituaryName = obituaryName;
	}

	public String getObituaryName(){
		return obituaryName;
	}

	public void setObituaryZip(String obituaryZip){
		this.obituaryZip = obituaryZip;
	}

	public String getObituaryZip(){
		return obituaryZip;
	}

	public void setObituaryFuneralTime(String obituaryFuneralTime){
		this.obituaryFuneralTime = obituaryFuneralTime;
	}

	public String getObituaryFuneralTime(){
		return obituaryFuneralTime;
	}

	public void setObituaryCity(String obituaryCity){
		this.obituaryCity = obituaryCity;
	}

	public String getObituaryCity(){
		return obituaryCity;
	}

	public void setObituaryLat(double obituaryLat){
		this.obituaryLat = obituaryLat;
	}

	public double getObituaryLat(){
		return obituaryLat;
	}

	public void setObituaryBirthDate(String obituaryBirthDate){
		this.obituaryBirthDate = obituaryBirthDate;
	}

	public String getObituaryBirthDate(){
		return obituaryBirthDate;
	}

	public void setObituaryCountry(String obituaryCountry){
		this.obituaryCountry = obituaryCountry;
	}

	public String getObituaryCountry(){
		return obituaryCountry;
	}

	public void setObituaryMapUrl(String obituaryMapUrl){
		this.obituaryMapUrl = obituaryMapUrl;
	}

	public String getObituaryMapUrl(){
		return obituaryMapUrl;
	}

	public void setObituaryImage(String obituaryImage){
		this.obituaryImage = obituaryImage;
	}

	public String getObituaryImage(){
		return obituaryImage;
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

	public void setObituaryDescription(String obituaryDescription){
		this.obituaryDescription = obituaryDescription;
	}

	public String getObituaryDescription(){
		return obituaryDescription;
	}

	public void setObituaryDeathDate(String obituaryDeathDate){
		this.obituaryDeathDate = obituaryDeathDate;
	}

	public String getObituaryDeathDate(){
		return obituaryDeathDate;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"Obituary{" + 
			"obituaryAddress2 = '" + obituaryAddress2 + '\'' + 
			",obituaryAddress1 = '" + obituaryAddress1 + '\'' + 
			",obituaryFuneralDate = '" + obituaryFuneralDate + '\'' + 
			",obituaryState = '" + obituaryState + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",obituaryLng = '" + obituaryLng + '\'' + 
			",obituaryName = '" + obituaryName + '\'' + 
			",obituaryZip = '" + obituaryZip + '\'' + 
			",obituaryFuneralTime = '" + obituaryFuneralTime + '\'' + 
			",obituaryCity = '" + obituaryCity + '\'' + 
			",obituaryLat = '" + obituaryLat + '\'' + 
			",obituaryBirthDate = '" + obituaryBirthDate + '\'' + 
			",obituaryCountry = '" + obituaryCountry + '\'' + 
			",obituaryMapUrl = '" + obituaryMapUrl + '\'' + 
			",obituary_image = '" + obituaryImage + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",added_by = '" + addedBy + '\'' + 
			",obituaryDescription = '" + obituaryDescription + '\'' + 
			",obituaryDeathDate = '" + obituaryDeathDate + '\'' + 
			",_id = '" + id + '\'' + 
			"}";
		}
}