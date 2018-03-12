package com.unitybound.main.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vaibhav.malviya on 22-09-2016.
 */

public class SideMenu implements Parcelable {
    private String name;
    private boolean isSelected;
    private int drawable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public SideMenu(String name, int drawable, boolean isSelected) {
        this.name = name;
        this.isSelected = isSelected;
        this.drawable = drawable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.drawable);
    }

    protected SideMenu(Parcel in) {
        this.name = in.readString();
        this.isSelected = in.readByte() != 0;
        this.drawable = in.readInt();
    }

    public static final Creator<SideMenu> CREATOR = new Creator<SideMenu>() {
        @Override
        public SideMenu createFromParcel(Parcel source) {
            return new SideMenu(source);
        }

        @Override
        public SideMenu[] newArray(int size) {
            return new SideMenu[size];
        }
    };
}
