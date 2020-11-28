package scu.edu.sharedstyle.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Item {
    private int itemId;
    private String userID;
    private String itemName;
    private String itemDesc;
    private String condition;
    private double price;
    private String brand;
    private String Img_url;
    private @ServerTimestamp Date timestamp;
    private ArrayList<String> imgURLs;
    private ArrayList<String> search_keywords;

    public ArrayList<String> getSearch_keywords() {
        return search_keywords;
    }

    public void setSearch_keywords(ArrayList<String> search_keywords) {
        this.search_keywords = search_keywords;
    }

    public String getUserID(){ return userID; }

    public void setUserID(String userID){ this.userID=userID; }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getImg_url() {
        return Img_url;
    }

    public void setImg_url(String img_url) {
        Img_url = img_url;
    }

    public Date getTimestamp() { return timestamp; }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<String> getImgURLs() { return imgURLs; }

    public void setImgURLs(ArrayList<String> imgURLs) { this.imgURLs = imgURLs; }

    public Item(){}

    public Item(String itemName, String itemDesc, String brand, double price, String img_url,ArrayList<String> imgURLs,String userID,ArrayList<String> search_keywords){
        this.itemName=itemName;
        this.itemDesc=itemDesc;
        this.brand=brand;
        this.price=price;
        this.imgURLs=imgURLs;
        this.Img_url=img_url;
        this.search_keywords = search_keywords;
        this.userID=userID;
    }
}
