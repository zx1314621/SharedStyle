package scu.edu.sharedstyle.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Item {
    private int itemId;
    private String itemName;
    private String itemDesc;
    private String condition;
    private double price;
    private String brand;
    private int Img_url;
    private @ServerTimestamp Date timestamp;
    private ArrayList<String> imgURLs;

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

    public int getImg_url() {
        return Img_url;
    }

    public void setImg_url(int img_url) {
        Img_url = img_url;
    }

    public Date getTimestamp() { return timestamp; }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<String> getImgURLs() { return imgURLs; }

    public void setImgURLs(ArrayList<String> imgURLs) { this.imgURLs = imgURLs; }

    public Item(){}

    public Item(String itemName, String itemDesc, String brand, double price, ArrayList<String> imgURLs){
        this.itemName=itemName;
        this.itemDesc=itemDesc;
        this.brand=brand;
        this.price=price;
        this.imgURLs=imgURLs;
    }
}
