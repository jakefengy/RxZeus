package com.xm.zeus.utils.entity;

/**
 * 已选择的图片
 */
public class Image {

    private String Id;
    private String Name;
    private String ImagePath;
    private long DateAdded;
    private boolean IsSelected = false;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public boolean isSelected() {
        return IsSelected;
    }

    public void setIsSelected(boolean isSelected) {
        IsSelected = isSelected;
    }

    public long getDateAdded() {
        return DateAdded;
    }

    public void setDateAdded(long dateAdded) {
        DateAdded = dateAdded;
    }
}
