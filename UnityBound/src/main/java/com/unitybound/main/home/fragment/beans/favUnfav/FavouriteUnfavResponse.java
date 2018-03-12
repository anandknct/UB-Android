package com.unitybound.main.home.fragment.beans.favUnfav;

import com.google.gson.annotations.SerializedName;

public class FavouriteUnfavResponse{

	@SerializedName("statuscode")
	private String statuscode;

	@SerializedName("msg")
	private String msg;

	@SerializedName("status")
	private String status;

	@SerializedName("favoriteCount")
	private int favoriteCount;

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

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setFavoriteCount(int favoriteCount){
		this.favoriteCount = favoriteCount;
	}

	public int getFavoriteCount(){
		return favoriteCount;
	}

	@Override
 	public String toString(){
		return 
			"FavouriteUnfavResponse{" + 
			"statuscode = '" + statuscode + '\'' + 
			",msg = '" + msg + '\'' + 
			",status = '" + status + '\'' + 
			",favoriteCount = '" + favoriteCount + '\'' + 
			"}";
		}
}