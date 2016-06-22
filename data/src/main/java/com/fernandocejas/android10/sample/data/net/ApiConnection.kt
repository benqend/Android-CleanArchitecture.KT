/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fernandocejas.android10.sample.data.net

import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

/**
 * Api Connection class used to retrieve data from the cloud.
 * Implements [java.util.concurrent.Callable] so when executed asynchronously can
 * return a value.
 */
class ApiConnection @Throws(MalformedURLException::class)
private constructor(url: String) : Callable<String> {

    private val url: URL
    private var response: String? = null

    init {
        this.url = URL(url)
    }

    /**
     * Do a request to an api synchronously.
     * It should not be executed in the main thread of the application.

     * @return A string response
     */
    fun requestSyncCall(): String? {
        connectToApi()
        return response
    }

    private fun connectToApi() {
        val okHttpClient = this.createClient()
        val request = Request.Builder().url(this.url).addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE_JSON).get().build()

        try {
            this.response = okHttpClient.newCall(request).execute().body().string()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun createClient(): OkHttpClient {
        val okHttpClient = OkHttpClient()
        okHttpClient.setReadTimeout(10000, TimeUnit.MILLISECONDS)
        okHttpClient.setConnectTimeout(15000, TimeUnit.MILLISECONDS)

        return okHttpClient
    }

    @Throws(Exception::class)
    override fun call(): String? {
        return requestSyncCall()
    }

    companion object {

        private val CONTENT_TYPE_LABEL = "Content-Type"
        private val CONTENT_TYPE_VALUE_JSON = "application/json; charset=utf-8"

        @Throws(MalformedURLException::class)
        fun createGET(url: String): ApiConnection {
            return ApiConnection(url)
        }
    }
}
