package com.windwarriors.appetite.broadcast;

import android.content.IntentFilter;

import com.windwarriors.appetite.model.Business;
import com.windwarriors.appetite.utils.Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BusinessListReadyReceiverTest {
    private BusinessListReadyReceiver businessListReadyReceiver;

    @Before
    public void setUp() throws Exception {
        businessListReadyReceiver = new BusinessListReadyReceiver(new BusinessListReadyReceiver.OnReceive() {
            @Override
            public void onReceive(ArrayList<Business> businessList) {
            }
        });
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getIntentFilter() throws Exception {
        IntentFilter intentFilter = businessListReadyReceiver.getIntentFilter();
        assertEquals(1, intentFilter.countActions());
        assertEquals( Constants.BROADCAST_BUSINESS_LIST_READY, intentFilter.getAction(0) );
    }

}