package com.example.karu.Model;

// mode class is used for it to work with firebase

public class Upload {
    private String mName;
    private String mImageUrl;
    private String Category;
    private int RentRate;
    private boolean IsLike;
    private boolean IsListed;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String name, String imageUrl, String category,int rentRate, boolean isLike, boolean isListed) {
      /*  if (name.trim().equals("")) {
            name = "No Name";
        }*/
        mName = name;
        mImageUrl = imageUrl;
        Category = category;
        RentRate = rentRate;
        IsLike = isLike;
        IsListed = isListed;
    }

    public String getName(){
        return mName;
    }

    public void setName(String name){
         mName = name;
    }

    public String getImageUrl(){
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl){
        mImageUrl = imageUrl;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String mCategory) {
        this.Category = mCategory;
    }

    public int getRentRate() {
        return RentRate;
    }

    public void setRentRate(int mRentRate) {
        this.RentRate = mRentRate;
    }

    public boolean isIsLike() {
        return IsLike;
    }

    public void setIsLike(boolean mIsLike) {
        this.IsLike = mIsLike;
    }

    public boolean isIsListed() {
        return IsListed;
    }

    public void setIsListed(boolean mIsListed) {
        this.IsListed = mIsListed;
    }

}
