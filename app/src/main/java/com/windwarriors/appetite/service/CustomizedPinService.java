package com.windwarriors.appetite.service;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.windwarriors.appetite.R;
import com.windwarriors.appetite.model.Business;

public class CustomizedPinService {
    private Business business;

    public CustomizedPinService(Business business) {
        this.business = business;
    }

    public static BitmapDescriptor herePin() {
        return BitmapDescriptorFactory.fromResource(R.drawable.here);
    }

    public BitmapDescriptor getCustomMapPin() {

        // Example of Paid Pin Feature
        if (isTimHortons()) return TimHortonsPin();

        if (isBrazilian()) return brazilianPin();
        if (isChinese()) return chinesePin();
        if (isJapanese()) return japanesePin();
        if (isBurger()) return burgerPin();
        if (isSeafood()) return seafoodPin();
        if (isCoffee()) return coffeePin();

        return BitmapDescriptorFactory.fromResource(R.drawable.defaulticon);
    }

    private boolean isTimHortons() {
        return business.getName().contains("Tim Hortons");
    }

    @NonNull
    private BitmapDescriptor TimHortonsPin() {
        return BitmapDescriptorFactory.fromResource(R.drawable.timhortonsred);
    }

    private boolean isBrazilian() {
        String[] foodCategories = business.getFoodCategory();
        for (String category: foodCategories ) {
            if (category.toLowerCase().contains("brazil")) return true;
        }
        return false;
    }

    @NonNull
    private BitmapDescriptor brazilianPin() {
        return BitmapDescriptorFactory.fromResource(R.drawable.brazil);
    }

    private boolean isChinese() {
        String[] foodCategories = business.getFoodCategory();
        for (String category: foodCategories ) {
            if (category.toLowerCase().contains("chinese")) return true;
        }
        return false;
    }

    @NonNull
    private BitmapDescriptor chinesePin() {
        return BitmapDescriptorFactory.fromResource(R.drawable.china);
    }

    private boolean isJapanese() {
        String[] foodCategories = business.getFoodCategory();
        for (String category: foodCategories ) {
            if (category.toLowerCase().contains("japanese")) return true;
        }
        return false;
    }

    @NonNull
    private BitmapDescriptor japanesePin() {
        return BitmapDescriptorFactory.fromResource(R.drawable.japan);
    }

    private boolean isBurger() {
        String[] foodCategories = business.getFoodCategory();
        for (String category: foodCategories ) {
            if (category.toLowerCase().contains("burger")) return true;
        }
        return false;
    }

    @NonNull
    private BitmapDescriptor burgerPin() {
        return BitmapDescriptorFactory.fromResource(R.drawable.burger);
    }

    private boolean isCoffee() {
        String[] foodCategories = business.getFoodCategory();
        for (String category: foodCategories ) {
            category = category.toLowerCase();
            if (category.contains("coffee") ||
                category.contains("cafe")) return true;
        }
        return false;
    }

    @NonNull
    private BitmapDescriptor coffeePin() {
        return BitmapDescriptorFactory.fromResource(R.drawable.coffee);
    }

    private boolean isSeafood() {
        String[] foodCategories = business.getFoodCategory();
        for (String category: foodCategories ) {
            category = category.toLowerCase();
            if (category.contains("seafood")) return true;
        }
        return false;
    }

    @NonNull
    private BitmapDescriptor seafoodPin() {
        return BitmapDescriptorFactory.fromResource(R.drawable.seafood);
    }
}
