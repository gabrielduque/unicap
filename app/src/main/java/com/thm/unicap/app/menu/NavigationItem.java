package com.thm.unicap.app.menu;

public class NavigationItem {

    private String title;
    private int imageResource;
    private Type type = Type.NORMAL;

    public enum Type {
        EXTRA, NORMAL
    }

    public NavigationItem(String title, int imageResource, Type type) {
        this.title = title;
        this.imageResource = imageResource;
        this.type = type;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
