package com.windwarriors.appetite.model;

import com.yelp.fusion.client.models.Location;
import java.util.ArrayList;

public class Business {
        private String id;
        private String name;
        private String totalReviews;
        private String foodCategory;
        private String address;
        private String distance;
        private String imageLink;
        private Boolean isClosed;


    public Business(){
    }

    public Business(String id, String name, String review, String foodCategory, String address, String distance, String imageLink, Boolean isClosed) {
        this.id = id;
        this.name = name;
        this.totalReviews = review;
        this.foodCategory = foodCategory;
        this.address = address;
        this.distance = distance;
        this.imageLink = imageLink;
        this.isClosed = isClosed;
    }

    public Business(com.yelp.fusion.client.models.Business yelpBusiness) {
        // TODO: convert yelp id(string) to our id(int)
        this.id = yelpBusiness.getId();
        this.name = yelpBusiness.getName();
        this.totalReviews = String.valueOf(yelpBusiness.getReviewCount());

        // TODO: change foodCategory to String[] (array)??
        this.foodCategory = yelpBusiness.getCategories().get(0).getTitle(); //getAlias()
        this.address = locationToAddress(yelpBusiness.getLocation());
        this.distance = String.valueOf((int) Math.ceil(yelpBusiness.getDistance())) + "m";
        this.imageLink = yelpBusiness.getImageUrl();
        this.isClosed = yelpBusiness.getIsClosed();
    }

    private String locationToAddress(Location l) {
        StringBuilder address;
        ArrayList<String> displayAddress = l.getDisplayAddress();

        if (displayAddress.isEmpty()) return "Unknown address";

        address = new StringBuilder(displayAddress.get(0));
        displayAddress.remove(0);
        for (String line: l.getDisplayAddress()) {
            address.append(", ").append(line);
        }
        return address.toString();
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

    public String getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(String totalReviews) {
        this.totalReviews = totalReviews;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(boolean closed) {
        isClosed = closed;
    }
}
