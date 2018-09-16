package jp.ac.dendai.cdl.raibito.gamecapture

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import jp.ac.dendai.cdl.raibito.gamecapture.floating.FloatingService
import jp.ac.dendai.cdl.raibito.gamecapture.floating.hasOverlayPermission
import jp.ac.dendai.cdl.raibito.gamecapture.floating.requestOverlayPermission

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.qualifiedName
        private val REQUEST_OVERLAY_PERMISSION = 1
    }

    private var enabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        if (hasOverlayPermission()) {
            val intent = Intent(this, FloatingService::class.java)
                    .setAction(FloatingService.ACTION_STOP)
            startService(intent)
            finish()
        } else {
            requestOverlayPermission(REQUEST_OVERLAY_PERMISSION)
        }
    }

    override fun onStop() {
        super.onStop()
        if (enabled && hasOverlayPermission()) {
            val intent = Intent(this, FloatingService::class.java)
                    .setAction(FloatingService.ACTION_START)
            startService(intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_OVERLAY_PERMISSION -> Log.d(TAG, "enable overlay permission")
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //enabled = false // これでbuttonを消すかどうかを決めてる
        return super.onTouchEvent(event)
    }
}
