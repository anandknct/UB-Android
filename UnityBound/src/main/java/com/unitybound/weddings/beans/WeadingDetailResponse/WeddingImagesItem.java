package com.unitybound.weddings.beans.WeadingDetailResponse;

import com.google.gson.annotations.SerializedName;

public class WeddingImagesItem{

	@SerializedName("image")
	private String image;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("wedding_id")
	private String weddingId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("_id")
	private String id;

	@SerializedName("image_m")
	private String imageM;

	@SerializedName("image_l")
	private String imageL;

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setWeddingId(String weddingId){
		this.weddingId = weddingId;
	}

	public String getWeddingId(){
		return weddingId;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setImageM(String imageM){
		this.imageM = imageM;
	}

	public String getImageM(){
		return imageM;
	}

	public void setImageL(String imageL){
		this.imageL = imageL;
	}

	public String getImageL(){
		return imageL;
	}

	@Override
 	public String toString(){
		return 
			"WeddingImagesItem{" + 
			"image = '" + image + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",wedding_id = '" + weddingId + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",_id = '" + id + '\'' + 
			",image_m = '" + imageM + '\'' + 
			",image_l = '" + imageL + '\'' + 
			"}";
		}
}