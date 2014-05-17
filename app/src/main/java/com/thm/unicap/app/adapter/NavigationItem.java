package com.thm.unicap.app.adapter;

public class NavigationItem {

    private String title;
    private int imageResource;
    private Size size = Size.BIG;

    public enum Size {
        SMALL, BIG
    }

    public NavigationItem(String title, int imageResource, Size size) {
        this.title = title;
        this.imageResource = imageResource;
        this.size = size;
    }

    public NavigationItem(String title, int imageResource) {
        this.title = title;
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }
}
