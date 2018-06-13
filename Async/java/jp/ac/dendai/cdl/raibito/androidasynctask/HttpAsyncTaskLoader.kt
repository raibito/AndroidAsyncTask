package jp.ac.dendai.cdl.raibito.androidasynctask

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by raibito on 2018/06/12.
 * AsyncTaskLoaderの実際の処理
 *
 */
class HttpAsyncTaskLoader(context: Context) : AbstractAsyncTaskLoader<String>(context) {

    // ここに非同期処理
    override fun loadInBackground(): String {
        val strUrl = "https://www.dendai.ac.jp/"
        val url = URL(strUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val stringBuilder = StringBuilder()
        for (line in reader.readLines()) {
            line.let { stringBuilder.append(line) }
        }
        reader.close()
        return stringBuilder.toString()
    }
}