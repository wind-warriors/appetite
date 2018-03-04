package com.windwarriors.appetite.YelpService

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

    //TODO: get hold of calls to cancel on onDestroy (so it does not memory-leaks)
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
                System.out.println(TAG + " " + t.message)
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

    fun onDestroy() {
        for(call in calls) {
            call.cancel()
        }
    }
}

// TODO: encapsulate yelp's Business class only on this service?
// Encapsulate callback answer might be here
// https://stackoverflow.com/questions/46884715/convert-retrofit-callback-value-to-return-enveloped-object