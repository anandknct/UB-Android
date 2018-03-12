package com.unitybound.main.home.fragment.beans.filterPost;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PostBy implements Parcelable {

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;

	@SerializedName("profileImage")
	private String profileImage;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setProfileImage(String profileImage){
		this.profileImage = profileImage;
	}

	public String getProfileImage(){
		return profileImage;
	}

	@Override
 	public String toString(){
		return 
			"PostBy{" + 
			"name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",profileImage = '" + profileImage + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
		dest.writeString(this.id);
		dest.writeString(this.profileImage);
	}

	public PostBy() {
	}

	protected PostBy(Parcel in) {
		this.name = in.readString();
		this.id = in.readString();
		this.profileImage = in.readString();
	}

	public static final Parcelable.Creator<PostBy> CREATOR = new Parcelable.Creator<PostBy>() {
		@Override
		public PostBy createFromParcel(Parcel source) {
			return new PostBy(source);
		}

		@Override
		public PostBy[] newArray(int size) {
			return new PostBy[size];
		}
	};
}