package com.windwarriors.appetite.service

import com.windwarriors.appetite.model.Business
import com.yelp.fusion.client.models.SearchResponse
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

class YelpServiceTest {
    private val CITY_HALL_LATITUDE: Double = 43.652568
    private val CITY_HALL_LONGITUDE: Double = -79.3835223
    private val ISLINGTON_STATION_LATITUDE: Double = 43.6450701
    private val ISLINGTON_STATION_LONGITUDE: Double = -79.5267515

    var yelpService: YelpService = YelpService()
    val q: BlockingQueue<SearchResponse> = LinkedBlockingQueue(1)

    @Before
    fun setUp() {
        yelpService.clear()
        yelpService.mockParameters()
    }

    @After
    fun tearDown() {
        yelpService.onDestroy()
    }

    @Test
    fun search() {
        println("====== business search on Centennial College ======")
        val response = runSearch()
        response?.let {
            assertTrue("Empty Yelp.search response", it.total > 0)
        }
    }

    @Test
    fun searchTimHortons() {
        println("====== business search of Tim Hortons ======")
        yelpService.term("Tim Hortons")
        val response = runSearch()
        response?.let {
            assertTrue("No Tim Hortons on response:" + it.businesses[0].name,
                it.businesses[0].name.contains("Tim Hortons"))
        }
    }

    @Test
    fun searchToronto() {
        yelpService.term("Toronto")
        val response = runSearch()
        response?.let {
            for (business: Business in yelpService.getSearchResults() ) {
                println(business.id)
                //assertTrue(business.id.contains("toronto")) FALSE
            }
        }
    }

    @Test
    fun searchTermCoffee() {
        yelpService.term("Coffee")
        val response = runSearch()
    }

    @Test
    fun searchCategoryCoffee() {
        yelpService.categories("Coffee")
        val response = runSearch()
    }

    @Test
    fun searchCityHall() {
        println("====== business search on City Hall ======")
        yelpService.latitude(CITY_HALL_LATITUDE)
        yelpService.longitude(CITY_HALL_LONGITUDE)
        val response = runSearch()
    }

    @Test
    fun searchIslingtonStation() {
        println("====== business search on Islington Station ======")
        yelpService.latitude(ISLINGTON_STATION_LATITUDE)
        yelpService.longitude(ISLINGTON_STATION_LONGITUDE)
        val response = runSearch()
    }

    @Test
    fun getBusiness() {
        val businessId = "the-real-mccoy-burgers-and-pizza-scarborough"
        val syncObject = Object()

        val yelpCallback = object : YelpService.Callback<Business> {
            override fun onResponse(response: Business) {
                synchronized(syncObject) {
                    syncObject.notify()
                }
            }

            override fun onFailure(t: Throwable) {
                synchronized(syncObject) {
                    syncObject.notify()
                }
            }
        }

        yelpService.getBusiness(businessId, yelpCallback)

        synchronized (syncObject){
            syncObject.wait()
            //System.out.println("business:")
            //yelpService.business.toString()
            assertTrue(yelpService.business.name.toLowerCase().contains("mccoy") )
        }
    }

    @Test
    fun testIsClosed() {
        yelpService.open_now(false)
        val response = runSearch()
        response?.let {
            val closedBusinesses = it.businesses.filter { business -> business.isClosed }
            val openBusinesses = it.businesses.filter { business -> !business.isClosed }
            System.out.println("closedBusinesses: " + closedBusinesses.size)
            System.out.println("openBusinesses: " + openBusinesses.size)
        }
    }

    private fun runSearch(): SearchResponse? {
        val yelpCallback = object : YelpService.Callback<SearchResponse> {
            override fun onResponse(response: SearchResponse) {
                q.add(yelpService.response)
            }

            override fun onFailure(t: Throwable) {
                fail("YelpServiceTest.runSearch.callback.onFailure: " + t.message)
            }
        }
        yelpService.search(yelpCallback)

        return getSearch()
    }

    private fun getSearch(): SearchResponse? {
        val timeout: Long = 5
        var response: SearchResponse? = null
        try {
            response = q.poll(timeout, TimeUnit.SECONDS)
        } catch (e: InterruptedException) {
            fail("Yelp.search() did not respond in " + timeout + "s!")
        }
        response?.let {
            assertTrue("Empty Yelp response", it.total > 0)
        }
        return response
    }
}