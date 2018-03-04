package com.windwarriors.appetite.YelpService

import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class YelpTest {
    var yelp: Yelp = Yelp()
    @Before
    fun setUp() {
        yelp = Yelp()
    }

    @After
    fun tearDown() {
    }

    /*
    @Test
    fun test_simple() {
        val centennialLatitude = "43.7844571"
        val centennialLongitude = "-79.2287377"
        //yelp.put("term", "indian food")

        yelp.put("latitude", centennialLatitude)
        yelp.put("longitude", centennialLongitude)

        val response = yelp.search()
        assertNotNull(response)
    }
    */

    @Test
    fun mock_search() {
        yelp.mockParameters()

        val response = yelp.search()
        assertNotNull(response)
    }

    @Test
    fun getBusiness() {
        val businessId = "the-real-mccoy-burgers-and-pizza-scarborough"
        val business = yelp.getBusiness(businessId)

        assertNotNull(business)
        assertEquals(businessId, business.id)
    }
}