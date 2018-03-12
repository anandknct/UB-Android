package com.unitybound.main.search.beans;

public class AllTypeData {
    private String name = null;

    private String phone = null;

    private String id = null;
    private String image;


    public AllTypeData(String name, String phone, String id, String image) {
        this.name = name;
        this.phone = phone;
        this.id = id;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}