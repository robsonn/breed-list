package processo.smartmei.finderdogdetails.util

import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONException
import org.json.JSONObject
import android.graphics.BitmapFactory
import java.io.*
import android.graphics.Bitmap
import javax.net.ssl.HttpsURLConnection


class Http {

    fun get(urlPath: String): JSONArray? {
        var urlConnection: HttpURLConnection? = null
        var jsonArray: JSONArray? = null

        try {
            val url = URL(urlPath)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.setReadTimeout(10000)
            urlConnection.setConnectTimeout(15000)
            urlConnection.setRequestMethod("GET")
            urlConnection.setRequestProperty("Content-Type", "application/json")
            urlConnection.connect()

            var breedJson: JSONObject? = null
            try {
                breedJson = JSONObject(urlConnection.inputStream.bufferedReader().readText())


                if(breedJson.get("message") is JSONArray)
                    jsonArray = breedJson.get("message") as JSONArray
                else
                    jsonArray = (breedJson.get("message") as JSONObject).names()


            } catch (e: JSONException) {}

        } catch (e: Exception) {
            e.printStackTrace()


        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect()
            }
            return jsonArray
        }
    }

    fun getImage(urlPath: String?): Bitmap? {
        var urlConnection: HttpURLConnection? = null
        var bm: Bitmap? = null


        try {
            if(urlPath != null) {
                val url = URL(urlPath)

                urlConnection = url.openConnection() as HttpsURLConnection
                urlConnection.setReadTimeout(10000)
                urlConnection.setConnectTimeout(15000)
                urlConnection.setRequestProperty("Content-Type", "image/jpeg")
                urlConnection.setRequestMethod("GET")

                urlConnection.connect()

                bm = BitmapFactory.decodeStream(BufferedInputStream(urlConnection.inputStream))
            }

        } catch (e: Exception) {
            e.printStackTrace()

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect()
            }

            return bm
        }
    }

}