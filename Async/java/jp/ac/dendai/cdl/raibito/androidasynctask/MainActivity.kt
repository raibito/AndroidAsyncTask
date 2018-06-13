package jp.ac.dendai.cdl.raibito.androidasynctask

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var loaderResult: String = "NoData"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val atlButton = findViewById<Button>(R.id.atlButton) // AsyncTaskLoader
        val atButton = findViewById<Button>(R.id.atButton) // AsyncTask
        val textView = findViewById<TextView>(R.id.textView)

        // AsyncTaskLoaderの起動
        atlButton.setOnClickListener {
            if (loaderResult == "NoData"){
                val bundle = Bundle()
                bundle.putString("TEST", "Default文字列")
                supportLoaderManager.initLoader(1, bundle, callback)
            } else {
                textView.text = loaderResult
            }
        }

        // AsyncTaskの起動
        atButton.setOnClickListener {
            HttpAsyncTask(textView).execute()
        }


    }

    // Loaderのコールバック これでViewを別クラスに投げなくて済む
    private val callback : LoaderManager.LoaderCallbacks<String> = object : LoaderManager.LoaderCallbacks<String> {

        override fun onCreateLoader(id: Int, args: Bundle?) : Loader<String> {
            val param = args?.getString("TEST") // 今回は使わない URLをUIから指定とかしたいときは使う
            return HttpAsyncTaskLoader(this@MainActivity) // Loaderがv4なのでAsyncTaskLoaderもv4
        }

        override fun onLoadFinished(loader: Loader<String>?, data: String?) {
            supportLoaderManager.destroyLoader(loader?.id ?: 1)
            loaderResult = data.toString() // dataにloadInBackgroundのreturn値が入っている
            textView.text = loaderResult
        }

        override fun onLoaderReset(loader: Loader<String>?) {
            // 今回は何もしない
        }


    }
}
