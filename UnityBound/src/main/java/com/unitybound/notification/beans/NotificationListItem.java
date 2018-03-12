package com.unitybound.notification.beans;

import com.google.gson.annotations.SerializedName;

public class NotificationListItem{

	@SerializedName("is_read")
	private int isRead;

	@SerializedName("send_to")
	private String sendTo;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("_id")
	private String id;

	@SerializedName("message")
	private String message;

	@SerializedName("send_by")
	private SendBy sendBy;

	@SerializedName("redirect_to")
	private String redirectTo;

	public void setIsRead(int isRead){
		this.isRead = isRead;
	}

	public int getIsRead(){
		return isRead;
	}

	public void setSendTo(String sendTo){
		this.sendTo = sendTo;
	}

	public String getSendTo(){
		return sendTo;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
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

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setSendBy(SendBy sendBy){
		this.sendBy = sendBy;
	}

	public SendBy getSendBy(){
		return sendBy;
	}

	public void setRedirectTo(String redirectTo){
		this.redirectTo = redirectTo;
	}

	public String getRedirectTo(){
		return redirectTo;
	}

	@Override
 	public String toString(){
		return 
			"NotificationListItem{" + 
			"is_read = '" + isRead + '\'' + 
			",send_to = '" + sendTo + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",_id = '" + id + '\'' + 
			",message = '" + message + '\'' + 
			",send_by = '" + sendBy + '\'' + 
			",redirect_to = '" + redirectTo + '\'' + 
			"}";
		}
}