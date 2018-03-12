package com.unitybound.settings.fragment.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Friends implements Parcelable {

	@SerializedName("testimonials")
	private int testimonials;

	@SerializedName("comments")
	private int comments;

	@SerializedName("devotionals")
	private int devotionals;

	@SerializedName("prayerrequest")
	private int prayerrequest;

	@SerializedName("praises")
	private int praises;

	@SerializedName("likes")
	private int likes;

	public void setTestimonials(int testimonials){
		this.testimonials = testimonials;
	}

	public int getTestimonials(){
		return testimonials;
	}

	public void setComments(int comments){
		this.comments = comments;
	}

	public int getComments(){
		return comments;
	}

	public void setDevotionals(int devotionals){
		this.devotionals = devotionals;
	}

	public int getDevotionals(){
		return devotionals;
	}

	public void setPrayerrequest(int prayerrequest){
		this.prayerrequest = prayerrequest;
	}

	public int getPrayerrequest(){
		return prayerrequest;
	}

	public void setPraises(int praises){
		this.praises = praises;
	}

	public int getPraises(){
		return praises;
	}

	public void setLikes(int likes){
		this.likes = likes;
	}

	public int getLikes(){
		return likes;
	}

	@Override
 	public String toString(){
		return 
			"Friends{" + 
			"testimonials = '" + testimonials + '\'' + 
			",comments = '" + comments + '\'' + 
			",devotionals = '" + devotionals + '\'' + 
			",prayerrequest = '" + prayerrequest + '\'' + 
			",praises = '" + praises + '\'' + 
			",likes = '" + likes + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.testimonials);
		dest.writeInt(this.comments);
		dest.writeInt(this.devotionals);
		dest.writeInt(this.prayerrequest);
		dest.writeInt(this.praises);
		dest.writeInt(this.likes);
	}

	public Friends() {
	}

	protected Friends(Parcel in) {
		this.testimonials = in.readInt();
		this.comments = in.readInt();
		this.devotionals = in.readInt();
		this.prayerrequest = in.readInt();
		this.praises = in.readInt();
		this.likes = in.readInt();
	}

	public static final Parcelable.Creator<Friends> CREATOR = new Parcelable.Creator<Friends>() {
		@Override
		public Friends createFromParcel(Parcel source) {
			return new Friends(source);
		}

		@Override
		public Friends[] newArray(int size) {
			return new Friends[size];
		}
	};
}