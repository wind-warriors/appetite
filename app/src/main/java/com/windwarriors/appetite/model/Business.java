package com.windwarriors.appetite.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.yelp.fusion.client.models.Location;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Business implements Parcelable {
        private String id;
        private String name;
        private String totalReviews;
        private String foodCategory;
        private String address;
        private String distance;
        private String imageLink;
//        private Boolean isClosed;
        private Double rating;
        private Double latitude;
        private Double longitude;

    public Business(){
    }

    public Business(String id, String name, String review, String foodCategory, String address,
                    String distance, String imageLink
            //, Boolean isClosed
            , Double rating, Double latitude, Double longitude) {
        this.id = id;
        this.name = name;
        this.totalReviews = review;
        this.foodCategory = foodCategory;
        this.address = address;
        this.distance = distance;
        this.imageLink = imageLink;
//        this.isClosed = isClosed;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Business(com.yelp.fusion.client.models.Business yelpBusiness) {
        this.id = yelpBusiness.getId();
        this.name = yelpBusiness.getName();
        this.totalReviews = String.valueOf(yelpBusiness.getReviewCount());

        // TODO: change foodCategory to String[] (array)??
        this.foodCategory = yelpBusiness.getCategories().get(0).getTitle(); //getAlias()
        this.address = locationToAddress(yelpBusiness.getLocation());
        DecimalFormat f = new DecimalFormat("0.00");
        this.distance = (f.format(yelpBusiness.getDistance() / 1000.0)) + " km";
        this.imageLink = yelpBusiness.getImageUrl();

//        this.isClosed = yelpBusiness.getIsClosed();
        this.rating = yelpBusiness.getRating();

        this.latitude = yelpBusiness.getCoordinates().getLatitude();
        this.longitude = yelpBusiness.getCoordinates().getLongitude();
    }


    protected Business(Parcel in) {
        id = in.readString();
        name = in.readString();
        totalReviews = in.readString();
        foodCategory = in.readString();
        address = in.readString();
        distance = in.readString();
        imageLink = in.readString();
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readDouble();
        }
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(totalReviews);
        dest.writeString(foodCategory);
        dest.writeString(address);
        dest.writeString(distance);
        dest.writeString(imageLink);
        if (rating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(rating);
        }
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
    }

    public static final Creator<Business> CREATOR = new Creator<Business>() {
        @Override
        public Business createFromParcel(Parcel in) {
            return new Business(in);
        }

        @Override
        public Business[] newArray(int size) {
            return new Business[size];
        }
    };

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


    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj.getClass() == Business.class)) return super.equals(obj);

        Business b2 = (Business) obj;
        return this.id.equals(b2.id) &&
               this.name.equals(b2.name) &&
               this.totalReviews.equals(b2.totalReviews) &&
               this.foodCategory.equals(b2.foodCategory) &&
               this.address.equals(b2.address) &&
               this.distance.equals(b2.distance) &&
               this.imageLink.equals(b2.imageLink) &&
                this.rating.equals(b2.rating) &&
               this.latitude.equals(b2.latitude) &&
               this.longitude.equals(b2.longitude);
    }
}
