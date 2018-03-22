package com.windwarriors.appetite.service

import com.windwarriors.appetite.utils.Constants
import com.yelp.fusion.client.connection.YelpFusionApi
import com.yelp.fusion.client.connection.YelpFusionApiFactory
import com.yelp.fusion.client.models.Business
import com.yelp.fusion.client.models.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class YelpService {
    private val TAG = "Appetite.YelpService"
    lateinit var response: SearchResponse
    lateinit var business: com.windwarriors.appetite.model.Business

    private val params = HashMap<String, String>()

    private val calls: ArrayList< Call <*> > = ArrayList()

    private val apiKey: String = "OSqO95EakOqUFqVniIVRARloq6ayjXnBgyPlbFgdV85RlV_tFcfU-_5cA-p6i0x3cpoQQQ4uhaL1aWX_o0Dom5Hvl7lJt2zYrKAtwUO8EO8c8fLaI2R2iJpKL5mYWnYx"
    private val apiFactory: YelpFusionApiFactory = YelpFusionApiFactory()
    private val yelpFusionApi: YelpFusionApi = apiFactory.createAPI(apiKey)

    interface Callback<T> {
        fun onResponse(response: T)
        fun onFailure(t: Throwable)
    }

    fun search(callback: YelpService.Callback<SearchResponse>) {
        val yelpCall = yelpFusionApi.getBusinessSearch(params)
        //val response = call.execute()

        val yelpCallback = object : retrofit2.Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, yelpResponse: Response<SearchResponse>) {
                response = yelpResponse.body()
                printResponse()

                callback.onResponse(response)
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                System.out.println(TAG + " Error: " + t.message)
                callback.onFailure(t)
            }
        }

        yelpCall.enqueue(yelpCallback)
        calls.add( yelpCall )
    }

    fun sync_search(): SearchResponse {
        //"""YelpService Search at https://www.yelp.com/developers/documentation/v3/business_search"""
        val call = yelpFusionApi.getBusinessSearch(params)
        // blocking call
        val searchResponse: Response<SearchResponse> = call.execute()
        response = searchResponse.body()
        printResponse()
        return response
    }

    fun getBusiness(id: String, callback: YelpService.Callback< com.windwarriors.appetite.model.Business >) {
        val yelpCall = yelpFusionApi.getBusiness(id)

        val yelpCallback = object : retrofit2.Callback<Business> {
            override fun onResponse(call: Call<Business>?, yelpResponse: Response<Business>?) {
                business = com.windwarriors.appetite.model.Business( yelpResponse?.body()!! )
                printBusiness()
                callback.onResponse( business )
            }

            override fun onFailure(call: Call<Business>?, t: Throwable?) {
                System.out.println(TAG + " Error: " + t?.message)
                callback.onFailure(t!!)
            }
        }

        yelpCall.enqueue(yelpCallback)
        calls.add( yelpCall )
    }

    fun sync_getBusiness(id: String): Business {
        val call = yelpFusionApi.getBusiness(id)
        val businessResponse = call.execute()
        business = com.windwarriors.appetite.model.Business( businessResponse.body() )
        printBusiness()
        return businessResponse.body()
    }

    fun getSearchResults(): ArrayList<com.windwarriors.appetite.model.Business> {
        val businessList: ArrayList<com.windwarriors.appetite.model.Business> = ArrayList()
        response.businesses.mapTo(businessList) { com.windwarriors.appetite.model.Business(it) }
        return businessList
    }

    private fun printBusiness() {
        business.toString()
        /*
        System.out.println(TAG + " Business:")
        System.out.println("  id: " + business.id)
        System.out.println("  name: " + business.name)
        System.out.println("  foodCategories: " + business.listFoodCategories())
        */
    }

    private fun printResponse() {
        System.out.println(TAG + " Response:")
        System.out.println(TAG + " " + response.total + " businesses found")
        //System.out.println(TAG + " " + response.businesses.toString())
        System.out.println(TAG + " First Business:" + response.businesses[0].id + ", " +
            response.businesses[0].name)
        //Log.d(TAG, response.body().toString())
    }

    fun mockParameters() {
        val centennialLatitude = Constants.CENTENNIAL_LATITUDE
        val centennialLongitude = Constants.CENTENNIAL_LONGITUDE
        //val radius = "1000" //in meters
        params.clear()
        //yelp.put("term", "indian food")

        latitude(centennialLatitude)
        longitude(centennialLongitude)
        //put("radius", radius)
    }

    fun term(term: String) {
        //"Ex: food, restaurants, Starbucks"

        params.put("term", term)
    }

    fun location(location: String) {
        params.put("location", location)
    }

    fun latitude(latitude: Double) {
        params.put("latitude", latitude.toString())
    }

    fun longitude(longitude: Double) {
        params.put("longitude", longitude.toString())
    }

    fun radius(radius: Int) {
        val radiusInMeters: Int = radius * 1000
        System.out.println(TAG + " setting range to " + radiusInMeters)
        params.put("radius", radiusInMeters.toString())
    }

    fun categories(categories: String) {
        params.put("categories", categories)
    }

    fun locale(locale: String) {
        params.put("locale", locale)
    }

    fun limit(limit: String) {
        params.put("limit", limit)
    }

    fun offset(offset: String) {
        params.put("offset", offset)
    }

    fun sort_by(sort_by: String) {
        //best_match, rating, review_count or distance
        params.put("sort_by", sort_by)
    }

    fun price(price: String) {
        params.put("price", price)
    }

    fun open_now(open_now: Boolean) {
        params.put("open_now", open_now.toString())
    }

    fun open_at(open_at: String) {
        params.put("open_at", open_at)
    }

    fun attributes(attributes: String) {
        params.put("attributes", attributes)
    }

    fun clear() {
        params.clear()
    }

    fun onDestroy() {
        for(call in calls) {
            call.cancel()
        }
    }
}

// TODO Rafael: simplify callbacks with interface (check BusinessListReadyReceiver)