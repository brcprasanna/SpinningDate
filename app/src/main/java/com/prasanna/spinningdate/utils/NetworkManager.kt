package com.prasanna.spinningdate.utils

import com.google.gson.Gson
import com.prasanna.spinningdate.model.GetTimeResp
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class NetworkManager {
    companion object {
        @Throws(IOException::class, JSONException::class)
        fun doGetRequest(): String {
            val urlConnection: HttpURLConnection?
            val url = URL(AppConstants.SERVICE_TIME_URL)

            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.readTimeout = 3000
            urlConnection.connectTimeout = 5000
            urlConnection.doOutput = true
            urlConnection.connect()

            val bufferedReader = BufferedReader(InputStreamReader(url.openStream()))
            val stringBuilder = StringBuilder()

            var line: String? = bufferedReader.readLine()
            while (line != null) {
                stringBuilder.append(line + "\n")
                line = bufferedReader.readLine()
            }

            bufferedReader.close()

            val jsonString = stringBuilder.toString()
            val responseModel =
                Gson().fromJson<GetTimeResp>(jsonString, GetTimeResp::class.java)
            val date = AppUtils.getDateFromJson(responseModel.datetime!!)
            return AppUtils.getFormattedDate(date)
        }
    }

}