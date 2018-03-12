package com.unitybound.weddings.beans.WeadingDetailResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeddingDetailResponse{

	@SerializedName("statuscode")
	private String statuscode;

	@SerializedName("msg")
	private String msg;

	@SerializedName("wedding_details")
	private WeddingDetails weddingDetails;

	@SerializedName("WeddingImages")
	private List<WeddingImagesItem> weddingImages;

	@SerializedName("status")
	private String status;

	public void setStatuscode(String statuscode){
		this.statuscode = statuscode;
	}

	public String getStatuscode(){
		return statuscode;
	}

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setWeddingDetails(WeddingDetails weddingDetails){
		this.weddingDetails = weddingDetails;
	}

	public WeddingDetails getWeddingDetails(){
		return weddingDetails;
	}

	public void setWeddingImages(List<WeddingImagesItem> weddingImages){
		this.weddingImages = weddingImages;
	}

	public List<WeddingImagesItem> getWeddingImages(){
		return weddingImages;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"WeddingDetailResponse{" + 
			"statuscode = '" + statuscode + '\'' + 
			",msg = '" + msg + '\'' + 
			",wedding_details = '" + weddingDetails + '\'' + 
			",weddingImages = '" + weddingImages + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}