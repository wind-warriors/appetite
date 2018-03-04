package com.windwarriors.appetite.YelpService

import com.yelp.fusion.client.connection.YelpFusionApi
import com.yelp.fusion.client.connection.YelpFusionApiFactory
import com.yelp.fusion.client.models.Business
import com.yelp.fusion.client.models.SearchResponse
import retrofit2.Response

class Yelp {
    private val TAG = "Appetite.Yelp"
    private lateinit var response: SearchResponse
    private lateinit var business: Business

    private val params = HashMap<String, String>()

    private val apiKey: String = "OSqO95EakOqUFqVniIVRARloq6ayjXnBgyPlbFgdV85RlV_tFcfU-_5cA-p6i0x3cpoQQQ4uhaL1aWX_o0Dom5Hvl7lJt2zYrKAtwUO8EO8c8fLaI2R2iJpKL5mYWnYx"
    private val apiFactory: YelpFusionApiFactory = YelpFusionApiFactory()
    private val yelpFusionApi: YelpFusionApi = apiFactory.createAPI(apiKey)

    // reviews,

    fun search(): SearchResponse {
        //"""Yelp Search at https://www.yelp.com/developers/documentation/v3/business_search"""
        val call = yelpFusionApi.getBusinessSearch(params)
        // blocking call
        val searchResponse: Response<SearchResponse> = call.execute()
        response = searchResponse.body()
        printResponse()
        return response
    }


    fun getBusiness(id: String): Business {
        val call = yelpFusionApi.getBusiness(id)
        val businessResponse = call.execute()
        business = businessResponse.body()
        printBusiness()
        return business


    }

    private fun printBusiness() {
        System.out.println(TAG + " Yelp Business:")
        System.out.println(TAG + " " + business.id)
    }

    private fun printResponse() {
        System.out.println(TAG + " Yelp Response:")
        System.out.println(TAG + " " + response.total)
        System.out.println(TAG + " " + response.businesses.toString())
        System.out.println(TAG + " First Business:" + response.businesses[0].id)
        //Log.d(TAG, response.body().toString())
    }

    /*
    fun call() {
        //asynchronous call
        callback = Callback<SearchResponse>() {
            @Override
            public void onResponse
        }
    }
    */

    fun mockParameters() {
        val centennialLatitude = "43.7844571"
        val centennialLongitude = "-79.2287377"
        //val radius = "1000" //in meters
        params.clear()
        //yelp.put("term", "indian food")

        put("latitude", centennialLatitude)
        put("longitude", centennialLongitude)
        //put("radius", radius)
    }

    fun clear() {
        params.clear()
    }

    fun put( paramName: String, param: String ) {
        //"""Parameters available at https://www.yelp.com/developers/documentation/v3/business_search"""
        params.put(paramName, param)
    }
}