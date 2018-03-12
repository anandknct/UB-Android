package com.unitybound.utility;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nikku on 22-Feb-18.
 */

public class DownloadModel implements Parcelable {

    public DownloadModel(){

    }

    private int progress;
    private int currentFileSize;
    private int totalFileSize;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getCurrentFileSize() {
        return currentFileSize;
    }

    public void setCurrentFileSize(int currentFileSize) {
        this.currentFileSize = currentFileSize;
    }

    public int getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(int totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.progress);
        dest.writeInt(this.currentFileSize);
        dest.writeInt(this.totalFileSize);
    }

    protected DownloadModel(Parcel in) {
        this.progress = in.readInt();
        this.currentFileSize = in.readInt();
        this.totalFileSize = in.readInt();
    }

    public static final Parcelable.Creator<DownloadModel> CREATOR = new Parcelable.Creator<DownloadModel>() {
        @Override
        public DownloadModel createFromParcel(Parcel source) {
            return new DownloadModel(source);
        }

        @Override
        public DownloadModel[] newArray(int size) {
            return new DownloadModel[size];
        }
    };
}

