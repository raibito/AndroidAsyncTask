package jp.ac.dendai.cdl.kouhei.androidasynctask

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val atButton = findViewById<Button>(R.id.atButton) // AsyncTask
        val atlButton = findViewById<Button>(R.id.atlButton) // AsyncTaskLoader
        val textView = findViewById<TextView>(R.id.textView)

        atButton.setOnClickListener {
            HttpAsyncTask(textView).execute()
        }


    }
}
