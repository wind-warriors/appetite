package com.windwarriors.appetite.service

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
    lateinit var business: Business

    private val params = HashMap<String, String>()

    private val calls: ArrayList< Call <*> > = ArrayList()

    private val apiKey: String = "OSqO95EakOqUFqVniIVRARloq6ayjXnBgyPlbFgdV85RlV_tFcfU-_5cA-p6i0x3cpoQQQ4uhaL1aWX_o0Dom5Hvl7lJt2zYrKAtwUO8EO8c8fLaI2R2iJpKL5mYWnYx"
    private val apiFactory: YelpFusionApiFactory = YelpFusionApiFactory()
    private val yelpFusionApi: YelpFusionApi = apiFactory.createAPI(apiKey)

    fun search(callback: Callback<SearchResponse>) {
        val yelpCall = yelpFusionApi.getBusinessSearch(params)
        //val response = call.execute()

        val yelpCallback = object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, yelpResponse: Response<SearchResponse>) {
                response = yelpResponse.body()
                printResponse()

                callback.onResponse(call, yelpResponse)
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                System.out.println(TAG + "Error: " + t)
                callback.onFailure(call, t)
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

    fun getBusiness(id: String, callback: Callback<Business>) {
        val yelpCall = yelpFusionApi.getBusiness(id)

        val yelpCallback = object : Callback<Business> {
            override fun onResponse(call: Call<Business>?, yelpResponse: Response<Business>?) {
                business = yelpResponse?.body()!!

                callback.onResponse(call, yelpResponse)
            }

            override fun onFailure(call: Call<Business>?, t: Throwable?) {
                System.out.println(TAG + " " + t?.message)
                callback.onFailure(call, t)
            }
        }

        yelpCall.enqueue(yelpCallback)
        calls.add( yelpCall )
    }

    fun sync_getBusiness(id: String): Business {
        val call = yelpFusionApi.getBusiness(id)
        val businessResponse = call.execute()
        business = businessResponse.body()
        printBusiness()
        return business
    }

    fun getSearchResults(): ArrayList<com.windwarriors.appetite.model.Business> {
        val businessList: ArrayList<com.windwarriors.appetite.model.Business> = ArrayList()
        response.businesses.mapTo(businessList) { com.windwarriors.appetite.model.Business(it) }
        return businessList
    }

    private fun printBusiness() {
        System.out.println(TAG + " YelpService Business:")
        System.out.println(TAG + " " + business.id)
    }

    private fun printResponse() {
        System.out.println(TAG + " YelpService Response:")
        System.out.println(TAG + " " + response.total)
        System.out.println(TAG + " " + response.businesses.toString())
        System.out.println(TAG + " First Business:" + response.businesses[0].id)
        //Log.d(TAG, response.body().toString())
    }

    fun mockParameters() {
        val centennialLatitude = 43.7844571
        val centennialLongitude = -79.2287377
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