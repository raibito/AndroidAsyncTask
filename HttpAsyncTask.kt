package jp.ac.dendai.cdl.kouhei.androidasynctask

import android.os.AsyncTask
import android.widget.TextView
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
* Created by raibito on 2018/06/11.
 * Http通信をAsyncTaskで行う
 * textViewのリークとか検査例外とかはとりあえず置いておく
*/
class HttpAsyncTask(private val textView: TextView) : AsyncTask<Void,Void,String>(){

    override fun onPreExecute() { // 前処理
        super.onPreExecute()
    }

    // ここが非同期で行いたい処理
    override fun doInBackground(vararg p0: Void?): String {
        val strUrl = "https://www.dendai.ac.jp/"
        val url = URL(strUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val stringBuilder = StringBuilder()
        for (line in reader.readLines()){
            line.let { stringBuilder.append(line) }
        }
        reader.close()
        return stringBuilder.toString()
    }

    override fun onPostExecute(result: String?) { // 後処理
        super.onPostExecute(result)
        textView.text = result
    }
}