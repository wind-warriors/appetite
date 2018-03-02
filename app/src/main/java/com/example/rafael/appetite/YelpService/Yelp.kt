package com.example.rafael.appetite.YelpService

import com.yelp.fusion.client.connection.YelpFusionApi
import com.yelp.fusion.client.connection.YelpFusionApiFactory
import com.yelp.fusion.client.models.Business
import com.yelp.fusion.client.models.SearchResponse
import retrofit2.Callback
import retrofit2.Response

class Yelp {
    private val TAG = "Appetite.Yelp"
    private lateinit var response: Response<SearchResponse>
    private lateinit var business: Business

    private val params = HashMap<String, String>()

    private val apiKey: String = "OSqO95EakOqUFqVniIVRARloq6ayjXnBgyPlbFgdV85RlV_tFcfU-_5cA-p6i0x3cpoQQQ4uhaL1aWX_o0Dom5Hvl7lJt2zYrKAtwUO8EO8c8fLaI2R2iJpKL5mYWnYx"
    private val apiFactory: YelpFusionApiFactory = YelpFusionApiFactory()
    private val yelpFusionApi: YelpFusionApi = apiFactory.createAPI(apiKey)

    // reviews,

    fun search(): Response<SearchResponse> {
        val call = yelpFusionApi.getBusinessSearch(params)
        // blocking call
        response = call.execute()
        printSearchResponse()
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

    private fun printSearchResponse() {
        System.out.println(TAG + " Yelp Response:")
        System.out.println(TAG + " " + response.body().total)
        System.out.println(TAG + " " + response.body().businesses.toString())
        System.out.println(TAG + " First Business:" + response.body().businesses[0].id)
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

    fun clear() {
        params.clear()
    }

    fun put( paramName: String, param: String ) {
        params.put(paramName, param)
    }
}