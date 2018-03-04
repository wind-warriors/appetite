package com.windwarriors.appetite.YelpService

import android.widget.Toast
import com.yelp.fusion.client.models.SearchResponse
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class YelpServiceTest {
    var yelpService: YelpService = YelpService()
    @Before
    fun setUp() {
        yelpService = YelpService()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun search() {

        /*
        yelpService.search(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                businessList.addAll(yelpService.getSearchResults())
                //trigger adapter update data?
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Toast.makeText(getApplicationContext(), "Unable to retrieve businesses: " + t.message, Toast.LENGTH_LONG)
            }
        })
        */
    }

    @Test
    fun getBusiness() {
        //val businessId = "the-real-mccoy-burgers-and-pizza-scarborough"
    }

    @Test
    fun mock_sync_search() {
        yelpService.mockParameters()

        val response = yelpService.sync_search()
        assertNotNull(response)
    }

    @Test
    fun sync_getBusiness() {
        val businessId = "the-real-mccoy-burgers-and-pizza-scarborough"
        val business = yelpService.sync_getBusiness(businessId)

        assertNotNull(business)
        assertEquals(businessId, business.id)
    }
}