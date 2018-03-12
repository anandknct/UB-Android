package com.unitybound.login.socialActivity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chetan.joshi on 04-08-2016.
 */
public interface SocialInterface {

    void OnReceiveSocialInfo(SocialInfo socialInfo);

    enum SOCIAL_MEDIUMS {FACEBOOK, LINKEDIN, GOOGLEPLUS,TWITTER}

    class SocialInfo implements Parcelable {

        private String socialId = "";
        private String firstName = "";
        private String lastName = "";
        private String emailId = "";
        private String profilePicture = "";
        private String socialMediaType = "";


        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmailId() {
            return emailId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public String getProfilePicture() {
            return profilePicture;
        }

        public void setProfilePicture(String profilePicture) {
            this.profilePicture = profilePicture;
        }

        public String getSocialId() {
            return socialId;
        }

        public void setSocialId(String socialId) {
            this.socialId = socialId;
        }

        public String getSocialMediaType() {
            return socialMediaType;
        }

        public void setSocialMediaType(String socialMediaType) {
            this.socialMediaType = socialMediaType;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.socialId);
            dest.writeString(this.firstName);
            dest.writeString(this.lastName);
            dest.writeString(this.emailId);
            dest.writeString(this.profilePicture);
            dest.writeString(this.socialMediaType);
        }

        public SocialInfo() {
        }

        protected SocialInfo(Parcel in) {
            this.socialId = in.readString();
            this.firstName = in.readString();
            this.lastName = in.readString();
            this.emailId = in.readString();
            this.profilePicture = in.readString();
            this.socialMediaType = in.readString();
        }

        public static final Creator<SocialInfo> CREATOR = new Creator<SocialInfo>() {
            @Override
            public SocialInfo createFromParcel(Parcel source) {
                return new SocialInfo(source);
            }

            @Override
            public SocialInfo[] newArray(int size) {
                return new SocialInfo[size];
            }
        };
    }
}
