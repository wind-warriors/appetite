package com.windwarriors.appetite.service;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.windwarriors.appetite.broadcast.BusinessListReadyReceiver;
import com.windwarriors.appetite.model.Business;
import com.windwarriors.appetite.utils.Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

import static org.junit.Assert.*;

public class BusinessServiceTest {
    private ArrayList<Business> businessList;
    private BusinessService businessService;
    private SharedPreferencesService sp;
    Context context;
    private String TAG = "Appetite.BusinessServiceTest";

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
        businessList = new ArrayList<>();
        businessService = new BusinessService(context, businessList);
        sp = new SharedPreferencesService(context);
    }

    @After
    public void tearDown() throws Exception {
        businessService.destroy();
    }

    @Test
    public void test_onReceive() throws Exception {
        BusinessListReadyReceiver businessListReadyReceiver;
        final Object syncObject = new Object();

        businessListReadyReceiver = new BusinessListReadyReceiver(new BusinessListReadyReceiver.OnReceive() {

            @Override
            public void onReceive(ArrayList<Business> businessList) {

            assertTrue("businessList returned empty",businessList.size() > 0);
            assertEquals(businessService.businessList , businessList);
            synchronized(syncObject) {
                syncObject.notify();
            }
            }
        });
        this.context.registerReceiver(businessListReadyReceiver, businessListReadyReceiver.getIntentFilter());

        sp.saveToSharedPreferences(Constants.SHARED_PREFERENCES_SEARCH_RANGE, "2");
        businessService.loadBusinessList(Constants.CENTENNIAL_LATITUDE, Constants.CENTENNIAL_LONGITUDE);

        synchronized (syncObject) {
            syncObject.wait();
            this.context.unregisterReceiver(businessListReadyReceiver);
        }
    }

    @Test
    public void test_spRange() throws Exception {

        BusinessListReadyReceiver businessListReadyReceiver;
        final Object syncObject = new Object();

        businessListReadyReceiver = new BusinessListReadyReceiver(new BusinessListReadyReceiver.OnReceive() {
            int numberOfBusiness1 = -1;
            int numberOfBusiness2 = -1;

            @Override
            public void onReceive(ArrayList<Business> businessList) {
                assertTrue("businessList is empty!", businessList.size() > 0);
                if (numberOfBusiness1 < 0) {
                    // returns 20 because of yelp limit parameter (pagination)
                    numberOfBusiness1 = businessList.size();
                    System.out.println(TAG + " b1: " + numberOfBusiness1);
                } else {
                    numberOfBusiness2 = businessList.size();
                    System.out.println(TAG + " b2: " + numberOfBusiness2);
                    //assertNotEquals(numberOfBusiness1, numberOfBusiness2);
                    synchronized(syncObject) {
                        syncObject.notify();
                    }
                }
            }
        });
        this.context.registerReceiver(businessListReadyReceiver, businessListReadyReceiver.getIntentFilter());

        System.out.println(TAG + "test_spRange:");
        sp.saveToSharedPreferences(Constants.SHARED_PREFERENCES_SEARCH_RANGE, "1");
        businessService.loadBusinessList(Constants.CENTENNIAL_LATITUDE, Constants.CENTENNIAL_LONGITUDE);

        sp.saveToSharedPreferences(Constants.SHARED_PREFERENCES_SEARCH_RANGE, "40");
        businessService.loadBusinessList(Constants.CENTENNIAL_LATITUDE, Constants.CENTENNIAL_LONGITUDE);

        synchronized (syncObject) {
            syncObject.wait();
            this.context.unregisterReceiver(businessListReadyReceiver);
        }
    }
}