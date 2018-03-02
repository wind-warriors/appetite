package com.example.rafael.appetite.YelpService

import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class YelpTest {
    val yelp: Yelp = Yelp()
    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun test_simple() {
        val centennialLatitude = "43.7844571"
        val centennialLongitude = "-79.2287377"
        //yelp.put("term", "indian food")
        yelp.put("latitude", centennialLatitude)
        yelp.put("longitude", centennialLongitude)

        val response = yelp.search()
        assertNotNull(response)
        assertNotNull(response.body())
    }
}