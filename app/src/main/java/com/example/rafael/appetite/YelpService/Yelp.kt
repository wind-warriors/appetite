package com.example.rafael.appetite.YelpService

import com.yelp.fusion.client.connection.YelpFusionApi
import com.yelp.fusion.client.connection.YelpFusionApiFactory
import com.yelp.fusion.client.models.SearchResponse
import retrofit2.Callback
import retrofit2.Response

class Yelp {
    val TAG = "Appetite.Yelp"
    private lateinit var response: Response<SearchResponse>

    val params = HashMap<String, String>()

    val apiKey: String = "OSqO95EakOqUFqVniIVRARloq6ayjXnBgyPlbFgdV85RlV_tFcfU-_5cA-p6i0x3cpoQQQ4uhaL1aWX_o0Dom5Hvl7lJt2zYrKAtwUO8EO8c8fLaI2R2iJpKL5mYWnYx"
    val apiFactory: YelpFusionApiFactory = YelpFusionApiFactory()
    val yelpFusionApi: YelpFusionApi = apiFactory.createAPI(apiKey)

    fun search(): Response<SearchResponse> {
        val call = yelpFusionApi.getBusinessSearch(params)
        // blocking call
        response = call.execute()
        printResponse()
        return response
    }

    private fun printResponse() {
        System.out.println(TAG + " Yelp Response:")
        System.out.println(TAG + " " + response.body().total)
        System.out.println(TAG + " " + response.body().businesses.toString())
        System.out.println(TAG + " First Business:" + response.body().businesses[0].id)
        //Log.d(TAG, response.body().toString())
    }

    /*
    fun getBusiness(id: String) {
        val call = yelpFusionApi.getBusiness(id)
        val response = call.execute()
    }
    */
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