package com.windwarriors.appetite.service;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.windwarriors.appetite.utils.Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SharedPreferencesServiceTest {
    SharedPreferencesService sharedPreferencesService;
    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        sharedPreferencesService = new SharedPreferencesService(context);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void saveToSharedPreferences() throws Exception {
        for (int i = 1; i < 40 ; i++) {
            String expectedSearchRange = String.valueOf(i);
            sharedPreferencesService.saveToSharedPreferences(Constants.SHARED_PREFERENCES_SEARCH_RANGE, expectedSearchRange);
            String actualSearchRange = sharedPreferencesService.getFromSharedPreferences(Constants.SHARED_PREFERENCES_SEARCH_RANGE);
            assertEquals( expectedSearchRange, actualSearchRange);
        }
    }
}